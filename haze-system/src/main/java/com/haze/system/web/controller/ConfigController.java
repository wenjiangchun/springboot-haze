package com.haze.system.web.controller;

import java.util.Map;

import com.haze.system.entity.Config;
import com.haze.system.service.ConfigService;
import com.haze.system.utils.ConfigType;
import com.haze.web.BaseCrudController;
import com.haze.web.utils.WebMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
		//queryVariables.put("configType", "S");
	}

	@Override
	protected void setModel(Model model, HttpServletRequest request) {
		super.setModel(model, request);
		model.addAttribute("configTypes", ConfigType.values());
	}

	@PostMapping(value="updateConfigValue")
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
	@PostMapping(value = "notExistCode")
	@ResponseBody
	public Boolean notExistCode(String code) {
		Boolean isExist = this.configService.existCode(code);
		return !isExist;
	}
}
