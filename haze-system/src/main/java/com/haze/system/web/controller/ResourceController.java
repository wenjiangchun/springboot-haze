package com.haze.system.web.controller;

import java.util.*;

import com.haze.core.spring.SpringContextUtils;
import com.haze.system.entity.Group;
import com.haze.system.entity.Resource;
import com.haze.system.service.ResourceService;
import com.haze.system.utils.ResourceType;
import com.haze.web.BaseController;
import com.haze.web.datatable.DataTablePage;
import com.haze.web.datatable.DataTableParams;
import com.haze.web.utils.TreeNode;
import com.haze.web.utils.WebMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 资源操作Controller
 * @author Sofar
 *
 */
@Controller
@RequestMapping(value = "/system/resource")
public class ResourceController extends BaseController {
	
	@Autowired
	private ResourceService resourceService;
	
	@GetMapping(value = "view")
	public String list(Model model) {
		model.addAttribute("resourceTypes", ResourceType.values());
		return "system/resource/resourceList";
	}
	
	@RequestMapping(value = "search")
	@ResponseBody
	public DataTablePage search(DataTableParams dataTableParams) {
		PageRequest p = dataTableParams.getPageRequest();
		Map<String, Object> queryVaribles = dataTableParams.getQueryVairables();
		if (queryVaribles != null && queryVaribles.get("resourceType") != null) {
			String value = (String) queryVaribles.get("resourceType");
			queryVaribles.put("resourceType",ResourceType.valueOf(value));
		}
		if (queryVaribles.containsKey("parent.id") && queryVaribles.get("parent.id") != null){
			Group g = new Group();
			g.setId(Long.valueOf(queryVaribles.get("parent.id").toString()));
			queryVaribles.put("parent.id", Long.valueOf(queryVaribles.get("parent.id").toString()));
		} else {
			queryVaribles.put("parent_isNull", null); //默认查询顶级字典列表
		}
		Page<Resource> resourceList = this.resourceService.findPage(p, dataTableParams.getQueryVairables());
		return DataTablePage.generateDataTablePage(resourceList, dataTableParams);
	}

	@GetMapping(value = "add")
	public String add(Model model, @RequestParam(required = false) Long parentId) {
		model.addAttribute("resourceTypes",ResourceType.values());
		List<Resource> resources = this.resourceService.findMenuResources();
		model.addAttribute("resources",resources);
		RequestMappingHandlerMapping requestMappingBean = SpringContextUtils.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingBean.getHandlerMethods();
		Set<String> result = new LinkedHashSet<>();
		for (RequestMappingInfo requestMappingInfo : handlerMethods.keySet()) {
			PatternsRequestCondition pc = requestMappingInfo.getPatternsCondition();
			Set<String> pSet = pc.getPatterns();
			result.addAll(pSet);
		}
		model.addAttribute("urlList",result);
		if (parentId != null) {
			Resource parent = this.resourceService.findById(parentId);
			model.addAttribute("parent", parent);
			model.addAttribute("parentId", parentId);
			model.addAttribute("num", parent.getChilds().size() + 1);
		} else {
			List<Resource> roots = this.resourceService.findByProperty("parent", null);
			model.addAttribute("num", roots.size() + 1);
		}
		return "system/resource/addResource";
	}
	
	@PostMapping(value = "save")
	@ResponseBody
	public WebMessage save(Resource resource) {
		try {
			this.resourceService.saveOrUpdate(resource);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
	}
	
	@RequestMapping(value = "getResourcesTree")
	@ResponseBody
	public List<TreeNode> getResources() {
		List<Resource> resourceList = this.resourceService.findAllBySn(true);
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		TreeNode root = new TreeNode();
		root.setName("系统资源树");
		root.setId(0L);
		root.setParentId(null);
		treeNodeList.add(root);
		for (Resource resource : resourceList) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(resource.getId());
			treeNode.setName(resource.getName());
			treeNode.setParentId(resource.getParent() != null ? resource.getParent().getId() : root.getId());
			treeNodeList.add(treeNode);
		}
		return treeNodeList;
	}
	@GetMapping(value = "edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		model.addAttribute("resourceTypes",ResourceType.values());
		List<Resource> menuResources = new ArrayList<Resource>();
		Resource resource = this.resourceService.findById(id);
		if (resource.getParent() != null) {
			menuResources = this.resourceService.findMenuResources();
		}
		model.addAttribute("resource",resource);
		model.addAttribute("menuResources",menuResources);
		RequestMappingHandlerMapping requestMappingBean = SpringContextUtils.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingBean.getHandlerMethods();
		Set<String> result = new LinkedHashSet<>();
		for (RequestMappingInfo requestMappingInfo : handlerMethods.keySet()) {
			PatternsRequestCondition pc = requestMappingInfo.getPatternsCondition();
			Set<String> pSet = pc.getPatterns();
			result.addAll(pSet);
		}
		model.addAttribute("urlList",result);
		return "system/resource/editResource";
	}
	
	@PostMapping(value = "update")
	public String update(Resource resource) throws Exception {
		if (resource.getParent() != null && resource.getParent().getId() == null) {
			resource.setParent(null);
		}
		this.resourceService.saveOrUpdate(resource);
		return "redirect:/system/resource/view/";
	}

	/**
	 * 批量删除资源,同时会删除已授权角色的资源信息
	 * @param ids 资源ID数组
	 * @return 返回操作对象
	 */
	@PostMapping(value = "delete/{ids}")
	@ResponseBody
	public WebMessage delete(@PathVariable("ids") Long[] ids) {
		try{
			this.resourceService.batchDelete(ids);
			logger.debug("资源删除成功, ids={}", ids);
			return WebMessage.createSuccessWebMessage();
		} catch (Exception e) {
			logger.error("资源删除失败", e);
			return WebMessage.createErrorWebMessage(e.getMessage());
		}
	}
}
