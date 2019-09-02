package com.haze.shiro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.haze.common.HazeStringUtils;
import com.haze.system.entity.Resource;
import com.haze.system.entity.Role;
import com.haze.system.service.ResourceService;
import com.haze.system.service.RoleService;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


public class ShiroFilterChainDBDefinition {

	//private static final String PERMS = "perms\\[(.*?)\\]";
	
	@Autowired
	private ResourceService resourceService;

	@Autowired
	private RoleService roleService;

	public Map<String, String> getFilterChainDefinitions() {
		Map<String, String> filterChainDefinitions = new HashMap<String, String>();
        List<Resource> resources = resourceService.findAll();
        List<Role> roles = roleService.findAll();

        //循环数据库资源的url
		for (Resource resource : resources) {
			if (HazeStringUtils.isNotEmpty(resource.getUrl()) && HazeStringUtils.isNotEmpty(resource.getPermission())) {
				filterChainDefinitions.put(resource.getUrl(), "perms[" + resource.getPermission() + "]");
			}
		}
        
        //循环数据库组的url
        for (Iterator<Role> it = roles.iterator(); it.hasNext();) {
        	Role role = it.next();
        	if(HazeStringUtils.isNotEmpty(role.getName()) && HazeStringUtils.isNotEmpty(role.getName())) {
        		//section.put(role.getCname(), role.getName());
        	}
        }
        //section.put("/**", "authc");  //对所有资源请求加上认证访问权限
		return filterChainDefinitions;
	}
}
