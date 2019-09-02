package com.haze.system.web.controller;

import java.util.*;

import com.haze.system.entity.Dictionary;
import com.haze.system.entity.Group;
import com.haze.system.service.DictionaryService;
import com.haze.system.service.GroupService;
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

/**
 * 组织机构Controller
 *
 * @author wenjiangchun
 */
@Controller
@RequestMapping(value = "/system/group")
public class GroupController extends BaseController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping(value = "view")
    public String list(Model model, @RequestParam(required = false) Long parentId) {
        List<Dictionary> groupTypeList = this.dictionaryService.findChildsByRootCode(Group.GROUP_TYPE);
        model.addAttribute("groupTypeList", groupTypeList);
        if (parentId != null) {
            model.addAttribute("parent", groupService.findById(parentId));
        }
        return "system/group/groupList";
    }

    @RequestMapping(value = "search")
    @ResponseBody
    public DataTablePage search(DataTableParams dataTableParams) {
        PageRequest p = dataTableParams.getPageRequest(); //根据dataTableParames对象获取JPA分页查询使用的PageRequest对象
        Map<String, Object> map = dataTableParams.getQueryVairables();
        if (map.get("parent.id") != null) {
            Group g = new Group();
            g.setId(Long.valueOf(map.get("parent.id").toString()));
            map.put("parent.id", Long.valueOf(map.get("parent.id").toString()));
        } else {
            map.put("parent_isNull", null); //默认查询顶级字典列表
        }
        if (map.get("groupType.id") != null) {
            String value = (String) map.get("groupType.id");
            map.put("groupType.id", Long.valueOf(value));
        }
        Page<Group> groupList = this.groupService.findPage(p, dataTableParams.getQueryVairables());
        DataTablePage dtp = DataTablePage.generateDataTablePage(groupList, dataTableParams); //将查询结果封装成前台使用的DataTablePage对象
        return dtp;
    }

    @RequestMapping(value = "getTopGroups")
    @ResponseBody
    public List<Group> getTopGroups() {
        List<Group> groups = groupService.findAll();
        Set<Group> newGroup = new HashSet<>();
        Group root = new Group();
        root.setFullName("组织机构树");
        for (Group g : groups) {
            g.setUsers(null);
            g.setChilds(null);
            g.setGroupType(null);
            if (g.getPid() == null) {
                g.setParent(root);
            }
            newGroup.add(g);
        }
        newGroup.add(root);
        return new ArrayList<>(newGroup);
    }

    @GetMapping(value = "add")
    public String add(@RequestParam(value = "parentId", required = false) Long parentId, Model model) {
        if (parentId != null) {
            Group parent = this.groupService.findById(parentId);
            model.addAttribute("parent", parent);
            model.addAttribute("parentId", parentId);
        }
        List<Dictionary> groupTypeList = this.dictionaryService.findChildsByRootCode(Group.GROUP_TYPE);
        model.addAttribute("groupTypeList", groupTypeList);
        return "system/group/addGroup";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public WebMessage save(Group group) {
        try {
            Date date = new Date();
			group.setUpdateTime(date);
            if (group.isNew()) {
            	group.setCreateTime(date);
			}
            this.groupService.save(group);
            logger.debug("机构添加/更新成功, group={}", group);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            logger.error("机构添加/更新失败", e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    @GetMapping(value = "delete/{ids}")
    @ResponseBody
    public WebMessage delete(@PathVariable("ids") Long[] ids) {
        try {
            this.groupService.batchDelete(ids);
            logger.debug("机构删除成功, ids={}", ids);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            logger.error("机构删除失败", e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    @GetMapping(value = "edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Group group = this.groupService.findById(id);
        model.addAttribute("group", group);
        List<Dictionary> groupTypeList = this.dictionaryService.findChildsByRootCode(Group.GROUP_TYPE);
        model.addAttribute("groupTypeList", groupTypeList);
        if (group.getParent() != null) {
            model.addAttribute("parentId", group.getParent().getId());
        }
        return "system/group/editGroup";
    }

    @PostMapping(value = "update")
    @ResponseBody
    public WebMessage update(Group group) {
        try {
            this.groupService.save(group);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    @PostMapping(value = "getTreeNode")
    @ResponseBody
    public List<TreeNode> getTreeNode() {
        return this.groupService.getTreeNode();
    }

	/*@RequestMapping(value = "getTreeNodeByUser", method = RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> getTreeNodeByUser() {
		try {
			String userId = HazeUtils.getCurrentUser().getUserId();
			Group g = userService.findById(Long.valueOf(userId)).getGroup();
			return this.groupService.getTreeNode(g.getId());
		} catch (UserNotFoundException e) {
			return this.groupService.getTreeNode();
		}
	}*/
}
