package com.haze.kettle.web.controller;

import com.haze.web.BaseController;
import com.haze.kettle.entity.KettleRepository;
import com.haze.kettle.service.KettleRepositoryService;
import com.haze.web.utils.AlertType;
import com.haze.web.utils.WebMessage;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/kettle")
public class KettleController extends BaseController {

    @Autowired
    private KettleRepositoryService kettleRepositoryService;

    @RequestMapping("/repository")
    public String list(Model model) {
        List<KettleRepository> kettleRepositoryList = kettleRepositoryService.findAll();
        model.addAttribute("kettleRepositoryList", kettleRepositoryList);
        return "kettle/repository/list";
    }

    @RequestMapping("/repository/add")
    public String addRepository(Model model) {
        List<IDatabaseType> databaseTypeList = kettleRepositoryService.getDatabaseTypes();
        model.addAttribute("dbTypeList", databaseTypeList);
        return "kettle/repository/add";
    }

    @RequestMapping("/repository/save")
    @ResponseBody
    public WebMessage saveRepository(Model model, KettleRepository kettleRepository) {
        try {
            kettleRepositoryService.save(kettleRepository);
            if (kettleRepositoryService.isInit(kettleRepository.getId())) {
                kettleRepositoryService.connectionRepository(kettleRepository.getId());
            }
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    @RequestMapping("/repository/testConnection/{id}")
    @ResponseBody
    public WebMessage testConnection(Model model, @PathVariable  Long id) {
        try {
            //kettleRepositoryService.testConnection(id);
            WebMessage webMessage = new WebMessage();
            webMessage.setAlertType(AlertType.SUCCESS);
            webMessage.setContent("");
            return webMessage;
        } catch (Exception e) {
            logger.error("", e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    @RequestMapping("/repository/createSchema/{id}")
    @ResponseBody
    public WebMessage createSchema(Model model, @PathVariable  Long id) {
        try {
            WebMessage webMessage = new WebMessage();
            webMessage.setAlertType(AlertType.SUCCESS);
            if (kettleRepositoryService.isInit(id)) {
                webMessage.setContent("资源库表已存在");
            } else {
                kettleRepositoryService.createSchema(id);
                webMessage.setContent("创建成功");
            }
            return webMessage;
        } catch (Exception e) {
            logger.error("", e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }



    @RequestMapping("/trans/view")
    public String index(Model model, Long repositoryId) {
        try {
            List<KettleRepository> repositoryList = kettleRepositoryService.getConnectedRepository();
            List<RepositoryElementMetaInterface> elementList = new ArrayList<>();
            if (!repositoryList.isEmpty()) {
                if (repositoryId == null) {
                    repositoryId = repositoryList.get(0).getId();
                }
                elementList = kettleRepositoryService.getAllTrans(repositoryId, "/");
            } else {
                model.addAttribute("error", "已连接资源库为空");
            }
            model.addAttribute("repositoryId", repositoryId);
            model.addAttribute("repositoryList", repositoryList);
            model.addAttribute("transList", elementList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "kettle/trans/index";
    }
}
