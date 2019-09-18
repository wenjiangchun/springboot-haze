package com.haze.system.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.haze.system.entity.Resource;
import com.haze.system.entity.Role;
import com.haze.system.service.ResourceService;
import com.haze.system.service.RoleService;
import com.haze.system.utils.Status;
import com.haze.web.BaseCrudController;
import com.haze.web.utils.WebMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 角色管理Controller
 * @author sofar
 *
 */
@Controller
@RequestMapping(value = "/system/role")
public class RoleController extends BaseCrudController<Role, Long> {

	private RoleService roleService;
	
	private ResourceService resourceService;

	public RoleController(RoleService roleService, ResourceService resourceService) {
		super("system", "role", "角色信息", roleService);
		this.roleService = roleService;
		this.resourceService = resourceService;
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
		if (queryVariables != null && queryVariables.get("status") != null) {
			String value = (String) queryVariables.get("status");
			queryVariables.put("status", Status.valueOf(value));
		}
	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("statuss", Status.values());
	}

	/*@GetMapping(value = "view")
	public String list(Model model) {
		model.addAttribute("statuss", Status.values());
		return "system/role/roleList";
	}*/
	
	/*@RequestMapping(value = "search")
	@ResponseBody
	public DataTablePage search(DataTableParams dataTableParams) {
		PageRequest p = dataTableParams.getPageRequest();
		Map<String, Object> queryVaribles = dataTableParams.getQueryVairables();
		if (queryVaribles != null && queryVaribles.get("status") != null) {
			String value = (String) queryVaribles.get("status");
			queryVaribles.put("status", Status.valueOf(value));
		}
		Page<Role> roleList = this.roleService.findPage(p, dataTableParams.getQueryVairables());
		DataTablePage dtp = DataTablePage.generateDataTablePage(roleList, dataTableParams);
		return dtp;
	}*/

	/*@GetMapping(value = "add")
	public String add(Model model) {
		model.addAttribute("statuss", Status.values());
		return "system/role/addRole";
	}*/
	
	/*@PostMapping(value = "save")
    @ResponseBody
	public WebMessage save(Role role) {
		try {
			this.roleService.saveOrUpdate(role);
            return WebMessage.createSuccessWebMessage();
		} catch (Exception e) {
			return WebMessage.createErrorWebMessage(e.getMessage());
		}
	}*/
	
	/*@PostMapping(value = "delete/{ids}")
    @ResponseBody
	public WebMessage delete(@PathVariable("ids") Long[] ids) {
        try {
            this.roleService.batchDelete(ids);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
	}*/
	
	/**
	 * 进入对角色添加资源管理权限页面
	 * @param id 角色Id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "addResources/{id}")
	public String addResources(@PathVariable("id") Long id, Model model) {
		Role role = this.roleService.findById(id);
		List<Resource> menus = this.resourceService.findMenuResources();
		List<Resource> newMenus = new ArrayList<Resource>();
		model.addAttribute("role",role);
		model.addAttribute("menus", menus);
		return "system/role/addResource";
	}
	
	/**
	 * 对角色添加资源管理权限
	 * @param id 角色Id
	 * @param resourceIds 系统资源Id数组
	 * @return 返回列表页面
	 */
	@PostMapping(value = "saveResources")
    @ResponseBody
	public WebMessage saveResources(@RequestParam("roleId") Long id,@RequestParam(value = "resourceIds[]", required = false) Long[] resourceIds) {
        try {
            this.roleService.addResources(id, resourceIds);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }
	
	/**
	 * 进入角色编辑页面
	 * @param id 用户Id
	 * @param model
	 * @return
	 *//*
	@GetMapping(value = "edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		model.addAttribute("statuss", Status.values());
		Role role = this.roleService.findById(id);
		model.addAttribute("role", role);
		return "system/role/editRole";
	}*/
	
	/**
	 * 更新角色信息
	 * @param role 角色信息
	 * @return 返回角色列表页面
	 */
	@PostMapping(value = "update")
    @ResponseBody
	public WebMessage update(Role role) {
		Role r = this.roleService.findById(role.getId());
		r.setName(role.getName());
		r.setEnabled(role.getEnabled());
		try {
			this.roleService.saveOrUpdate(r);
            /*return WebMessage.createLocaleSuccessWebMessage(RequestContextUtils.getLocale(request));*/
            return WebMessage.createSuccessWebMessage();
		} catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
		}
	}
	
	/**
	 * 判断角色英文名是否存在
	 * @param roleName 角色英文名称
	 * @return true/false 不存在返回true,否则返回false
	 */
	@GetMapping(value = "notExistRoleName")
	@ResponseBody
	public Boolean notExistRoleName(String roleName) {
		Boolean isExist = this.roleService.existCode(roleName);
		return !isExist;
	}
}
