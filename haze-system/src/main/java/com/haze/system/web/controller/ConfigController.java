package com.haze.system.web.controller;

import java.util.Map;

import com.haze.system.entity.Config;
import com.haze.system.service.ConfigService;
import com.haze.system.utils.ConfigType;
import com.haze.web.BaseCrudController;
import com.haze.web.utils.WebMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统配置Controller
 *
 * @author sofar
 *
 */
@Controller
@RequestMapping(value = "/system/config")
public class ConfigController extends BaseCrudController<Config, Long> {

	private ConfigService configService;

	public ConfigController(ConfigService configService) {
		super("system", "config", "配置信息", configService);
		this.configService = configService;
	}

	@Override
	protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
		if (queryVariables != null && queryVariables.get("configType") != null) {
			String value = (String) queryVariables.get("configType");
			queryVariables.put("configType", ConfigType.valueOf(value));
		}
	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("configTypes", ConfigType.values());
	}

	/*@RequestMapping(value = "view")
	public String list(Model model) {
		model.addAttribute("configTypes", ConfigType.values());
		return "system/config/configList";
	}*/
	
	/*@RequestMapping(value = "search")
	@ResponseBody
	public DataTablePage search(DataTableParams dataTableParams) {
		PageRequest p = dataTableParams.getPageRequest();
		Map<String, Object> queryVaribles = dataTableParams.getQueryVairables();
		if (queryVaribles != null && queryVaribles.get("configType") != null) {
			String value = (String) queryVaribles.get("configType");
			queryVaribles.put("configType", ConfigType.valueOf(value));
		}
		Page<Config> configList = this.configService.findPage(p, dataTableParams.getQueryVairables());
		DataTablePage dtp = DataTablePage.generateDataTablePage(configList, dataTableParams);
		return dtp;
	}*/
	
	/*@RequestMapping(value = "add")
	public String add(Model model) {
		return "system/config/addConfig";
	}*/
	
	/*@RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
	public WebMessage save(Config config) {
		config.setConfigType(ConfigType.B);
		try {
			this.configService.saveOrUpdate(config);
            return WebMessage.createSuccessWebMessage();
		} catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
		}

	}*/

    /*@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        Config config = this.configService.findById(id);
        model.addAttribute("config", config);
        return "system/config/editConfig";
    }*/

    /*@RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public WebMessage update(Config config) {
        try {
            Config g = this.configService.findById(config.getId());
            g.setDescription(config.getDescription());
            g.setValue(config.getValue());
            g.setName(config.getName());
            this.configService.saveOrUpdate(g);
            //return WebMessage.createLocaleSuccessWebMessage(RequestContextUtils.getLocale(request));
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

	@RequestMapping(value = "delete/{ids}")
    @ResponseBody
	public WebMessage delete(@PathVariable("ids") Long[] ids) {
		try {
			this.configService.deleteConfigs(ids);
			return WebMessage.createSuccessWebMessage();
		} catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
		}
	}*/
	
	@RequestMapping(value="updateConfigValue")
    @ResponseBody 
	public WebMessage updateConfigValue(@RequestParam(value="id")Long id,@RequestParam(value="value") String value) {
        try {
            Config config = this.configService.findById(id);
            config.setValue(value);
            this.configService.updateConfig(config);
            //return WebMessage.createLocaleSuccessWebMessage(RequestContextUtils.getLocale(request));
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }
	
	/**
	 * 判断配置代码是否存在
	 * @param code 配置名称
	 * @return 不存在返回 true / 存在返回 false
	 */
	@RequestMapping(value = "notExistCode", method = RequestMethod.POST)
	@ResponseBody
	public Boolean notExistCode(String code) {
		Boolean isExist = this.configService.existCode(code);
		return !isExist;
	}
}
