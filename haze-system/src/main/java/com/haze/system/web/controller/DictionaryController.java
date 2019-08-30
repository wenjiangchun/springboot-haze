package com.haze.system.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.haze.system.entity.Dictionary;
import com.haze.system.service.DictionaryService;
import com.haze.web.datatable.DataTablePage;
import com.haze.web.datatable.DataTableParams;
import com.haze.web.utils.WebMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



/**
 * 字典对象Controller
 * 
 * @author sofar
 */
@Controller
@RequestMapping(value = "/system/dictionary")
public class DictionaryController {

	@Autowired
	private DictionaryService dictionaryService;
	
	@GetMapping(value = "view")
	public String list(Model model, @RequestParam(required = false) String parentId) {
		if(StringUtils.isNotBlank(parentId)){
			model.addAttribute("parentId", parentId);
		}
		return "system/dictionary/dictionaryList";
	}
	
	@PostMapping(value = "search")
	@ResponseBody
	public DataTablePage search(DataTableParams dataTableParams) {
		PageRequest p = dataTableParams.getPageRequest(); //根据dataTableParames对象获取JPA分页查询使用的PageRequest对象
		Map<String, Object> map = dataTableParams.getQueryVairables();
		if(map.containsKey("parent.id") && map.get("parent.id") != null){ //查询parent字典下面的所有子字典列表
			Dictionary d = new Dictionary();
			d.setId(Long.valueOf(map.get("parent.id").toString()));
			map.put("parent.id", Long.valueOf(map.get("parent.id").toString()));
		} else {
			map.put("parent_isNull", null); //默认查询顶级字典列表
		}
		if ( map.get("isEnabled") != null) {
			String value = (String) map.get("isEnabled");
			map.put("isEnabled", Boolean.valueOf(value));
		}
		Page<Dictionary> dictionaryList = this.dictionaryService.findPage(p,map);
		DataTablePage dtp = DataTablePage.generateDataTablePage(dictionaryList, dataTableParams); //将查询结果封装成前台使用的DataTablePage对象
		return dtp;
	}
	
	@PostMapping(value = "getDictionaryTree")
	@ResponseBody
	public List<Dictionary> getDictionaryTree() {
		List<Dictionary> dictionarys =  dictionaryService.findAll();
		Set<Dictionary> newDictionary = new HashSet<Dictionary>();
		Dictionary root = new Dictionary();
		root.setName("字典树");
		for (Dictionary g : dictionarys) {
			g.setChilds(null);
			if (g.getPid() == null) {
				g.setParent(root);
			}
			newDictionary.add(g);
		}
		newDictionary.add(root);
		if(dictionarys.size() > 0) {
			newDictionary.add(root);
		}
		return new ArrayList<Dictionary>(newDictionary);
	}
	
	@GetMapping(value = "add")
	public String add(@RequestParam(value="parentId",required=false) Long parentId, Model model) {
		if (parentId != null) {
			Dictionary parent = this.dictionaryService.findById(parentId);
			model.addAttribute("parent", parent);
			model.addAttribute("num", parent.getChilds().size() + 1);
		} else {
			List<Dictionary> roots = this.dictionaryService.findByProperty("parent", null);
			model.addAttribute("num", roots.size() + 1);
		}
		return "system/dictionary/addDictionary";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public WebMessage save(Dictionary dictionary, RedirectAttributes redirectAttributes) {
		try {
			this.dictionaryService.saveOrUpdate(dictionary);
			return WebMessage.createSuccessWebMessage();
		} catch (Exception e) {
			return WebMessage.createErrorWebMessage(e.getMessage());
		}
	}
	
	@RequestMapping(value = "delete/{ids}")
	@ResponseBody
	public WebMessage delete(@PathVariable("ids") Long[] ids) {
		try {
            this.dictionaryService.batchDelete(ids);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
	}
	
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, Model model) {
		Dictionary dictionary = this.dictionaryService.findById(id);
		model.addAttribute("dictionary", dictionary);
		return "system/dictionary/editDictionary";
	}
	
	@RequestMapping(value = "update")
	@ResponseBody
	public WebMessage update(Dictionary dictionary) {
		try {
			this.dictionaryService.saveOrUpdate(dictionary);
			return WebMessage.createSuccessWebMessage();
		} catch (Exception e) {
			return WebMessage.createErrorWebMessage(e.getMessage());
		}
	}
	
	/**
	 * 设置字典启用状态
	 * @param id 字典Id
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping(value = "updateDictionaryIsEnabled/{id}")
	public String updateDictionaryIsEnabled(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		Dictionary dictionary = this.dictionaryService.findById(id);
		this.dictionaryService.updateDictionaryIsEnabled(dictionary, !dictionary.getEnabled());
		if (dictionary.getParent() != null) {
			redirectAttributes.addFlashAttribute("parentId", dictionary.getParent().getId());
		}
		return "redirect:/system/dictionary/view";
	}
	
	/**
	 * 判断字典代码是否存在  
	 * @param parentId 字典所在节点Id
	 * @param code 字典代码
	 * @return true/false
	 */
	@PostMapping(value = "isNotExistCode")
	@ResponseBody
	public Boolean isNotExistCode(@RequestParam(required=false)Long parentId, String code) {
		String pa = null;
		String[] codes = code.split(",");
		if (codes.length >= 2) {
			pa = codes[1];
		}
		Boolean isExist = this.dictionaryService.isExistDictionaryCode(parentId, codes[0]);
		return !isExist;
	}
	
	/**
	 * 根据代码名称获取代码字典中所有子字典
	 * @param codeName 字典代码名称
	 * @return List<Dictionary>
	 */
	@GetMapping(value = "getChilds/{codeName}")
	@ResponseBody
	public List<Dictionary> getChilds(@PathVariable String codeName) {
		return this.dictionaryService.findChildsByRootCode(codeName);
	}
}
