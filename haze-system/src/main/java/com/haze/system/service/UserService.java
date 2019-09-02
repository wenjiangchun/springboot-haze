package com.haze.system.service;

import java.util.*;


import com.haze.common.Digests;
import com.haze.common.EncodeUtils;
import com.haze.common.HazeStringUtils;
import com.haze.core.service.AbstractBaseService;
import com.haze.core.spring.SpringContextUtils;
import com.haze.system.dao.UserDao;
import com.haze.system.entity.Group;
import com.haze.system.entity.Role;
import com.haze.system.entity.User;
import com.haze.system.event.UserChangeGroupEvent;
import com.haze.system.event.UserRoleChangeEvent;
import com.haze.system.exception.UserLoginNameExistException;
import com.haze.system.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * 用户业务操作类
 *
 * @author sofar
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends AbstractBaseService<User, Long> {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        Assert.notNull(userDao, "userDao不能为null!");
        this.userDao = userDao;
        super.setDao(userDao);
    }

    /**
     * 根据用户登录名称查找用户对象
     *
     * @param loginName 登录名称
     * @return 用户对象
     */
    @Transactional(readOnly = true)
    public User findByLoginName(String loginName) {
        return this.userDao.findByLoginName(loginName);
    }

    @Transactional(readOnly = true)
    public User findByLoginName(String loginName, boolean initialRole) {
        User user = this.findByLoginName(loginName);
        if (initialRole) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                role.getResources();
                role.getAllPermissons();
            }
        }
        return user;
    }

    /**
     * 添加或更新用户对象，如果ID不存在则添加用户否则更新用户信息
     *
     * @param user 用户对象
     * @return 保存或更新后的用户对象
     * @throws Exception 如果登录名称已存在则抛出该异常
     */
    public User saveOrUpdate(User user) throws Exception {
        Assert.notNull(user);
        Date date = new Date();
        if (user.isNew()) {
            User u = this.userDao.findByLoginName(user.getLoginName());
            if (u != null) {
                logger.error("登陆名{}已存在，请重试！", user.getLoginName());
                throw new UserLoginNameExistException("登陆名" + user.getLoginName() + "已存在，请重试");
            } else {
                user.setPassword(User.DEFAULT_PASSWORD); //设置默认密码。
                entryptPassword(user); //加密用户密码
                user.setCreateTime(date);
                user.setUpdateTime(date);
                logger.debug("保存用户，用户信息为：{}", user);
            }
        } else {
            //更新用户对象
            //查询之前存在的密码
            User u = this.findById(user.getId());
            user.setPassword(u.getPassword());
            user.setSalt(u.getSalt());
            user.setUpdateTime(date);
            logger.debug("更新用户，用户信息为：{}", user);
            //发布用户变更事件
            SpringContextUtils.publishEvent(new UserRoleChangeEvent(user));
        }
        user = this.userDao.save(user);
        return user;
    }

    @Override
    public User save(User t) throws Exception {
        return saveOrUpdate(t);
    }

    /**
     * 对用户赋角色权限，同时更新权限缓存中用户信息
     *
     * @param id   用户Id
     * @param role 角色对象
     */
    @CacheEvict(value = "shiroCache", allEntries = true)
    public void addRole(Long id, Role role) throws Exception {
        User user = this.userDao.getOne(id);
        Assert.notNull(user);
        user.addRole(role);
        this.saveOrUpdate(user);
    }

    /**
     * 对用户批量赋角色权限，同时更新权限缓存中用户信息
     *
     * @param id    用户Id
     * @param roles 角色对象集合
     */
    @CacheEvict(value = "shiroCache", allEntries = true)
    public void addRoles(Long id, Set<Role> roles) throws Exception {
        User user = this.findById(id);
        Assert.notNull(user);
        user.setRoles(roles);
        this.saveOrUpdate(user);
    }

    /**
     * 对用户添加或更新组织机构对象
     *
     * @param id    用户Id
     * @param group 组织机构
     */
    public void addOrUpdateGroup(Long id, Group group) throws Exception {
        User user = this.userDao.getOne(id);
        Assert.notNull(user);
        if (user.getGroup() == null || user.getGroup().getId() != group.getId()) {
            user.setGroup(group);
        }
        this.saveOrUpdate(user);
		SpringContextUtils.publishEvent(new UserChangeGroupEvent(user));
    }

    /**
     * 更新用户密码
     *
     * @param id       用户ID
     * @param password 密码
     * @return 更新后的用户对象
     */
    public User updatePassword(Long id, String password) throws Exception {
        Assert.notNull(id, "用户ID不能为空");
        User user = this.findById(id);
        user.setPassword(password);
        entryptPassword(user);
        this.saveOrUpdate(user);
        return user;
    }

    /**
     * 重置用户密码，将用户密码重置为{@code User.DEFAULT_PASSWORD}
     *
     * @param id 用户ID
     * @return 重置密码后的用户对象
     */
    public User resetPassword(Long id) throws Exception {
        return updatePassword(id, User.DEFAULT_PASSWORD);
    }

    /**
     * 禁用用户
     *
     * @param id 用户ID
     * @return 禁用状态的用户对象
     * @throws Exception
     */
    public User disableUser(Long id) throws Exception {
        return changeStatus(id, Status.DISABLE);
    }

    /**
     * 启用用户
     *
     * @param id 用户ID
     * @return 启用状态的用户对象
     * @throws Exception
     */
    public User enableUser(Long id) throws Exception {
        return changeStatus(id, Status.ENABLE);
    }

    /**
     * 更改用户状态
     *
     * @param id     用户ID
     * @param status 状态对象
     * @return 更改状态后的用户对象
     * @throws Exception
     */
    public User changeStatus(Long id, Status status) throws Exception {
        Assert.notNull(id, "用户ID不能为空");
        User user = this.findById(id);
        user.setStatus(status);
        this.saveOrUpdate(user);
        return user;
    }

    /**
     * 根据登录名称判断用户是否存在
     *
     * @param loginName 登录名称
     * @return 存在返回true，否则返回false
     */
    @Transactional(readOnly = true)
    public Boolean existLoginName(String loginName) {
        User u = this.userDao.findByLoginName(loginName);
        return u != null;
    }

    /**
     * 根据查询条件查询用户信息列表
     *
     * @param pageable       分页参数
     * @param queryVirables  查询参数
     * @param isContainAdmin 是否包含"admin"对象
     * @return Page<User> 分页对象
     */
	@Transactional(readOnly = true)
    public Page<User> findPage(Pageable pageable, Map<String, Object> queryVirables, boolean isContainAdmin) {
        if (!isContainAdmin) {
            if (queryVirables == null) {
                queryVirables = new HashMap<>();
            }
            queryVirables.put("loginName_notEqual", User.ADMIN);
        }
        return this.findPage(pageable, queryVirables);
    }

    /**
     * 对用户密码进行加密，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(EncodeUtils.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(EncodeUtils.encodeHex(hashPassword));
    }

	@Transactional(readOnly = true)
    public List<User> findByText(String text) {
        if (HazeStringUtils.isBlank(text)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", Status.ENABLE);
            map.put("group.name_asc", null);
            map.put("sn_asc", null);
            map.put("loginName_notEqual", User.ADMIN);
            return this.findAll(map);
        } else {
            return this.userDao.findByUserNameOrGroupName("%" + text + "%", Status.ENABLE);
        }
    }
}
