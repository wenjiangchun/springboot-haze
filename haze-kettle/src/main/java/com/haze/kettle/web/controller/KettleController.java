package com.haze.kettle.web.controller;

import com.haze.kettle.KettleParams;
import com.haze.kettle.StepWrapper;
import com.haze.kettle.entity.KettleLog;
import com.haze.web.BaseController;
import com.haze.kettle.entity.KettleRepository;
import com.haze.kettle.service.KettleRepositoryService;
import com.haze.web.utils.WebMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.RepositoryObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "ETL数据操作接口")
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
    public WebMessage testConnection(Model model, @PathVariable Long id) {
        try {
            //kettleRepositoryService.testConnection(id);
            WebMessage webMessage = new WebMessage();
            webMessage.setContent("");
            return webMessage;
        } catch (Exception e) {
            logger.error("", e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    @RequestMapping("/repository/createSchema/{id}")
    @ResponseBody
    public WebMessage createSchema(Model model, @PathVariable Long id) {
        try {
            WebMessage webMessage = new WebMessage();
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

    @GetMapping("/getElementInfo/{repositoryId}/{objectId}/{type}")
    public String getElementInfo(@PathVariable Long repositoryId, @PathVariable String objectId, @PathVariable RepositoryObjectType type, Model model) {
        String page = "kettle/";
        try {
            switch (type) {
                case TRANSFORMATION:
                    page += "trans/transMetaInfo";
                    model.addAttribute("transMeta", kettleRepositoryService.getTransMetaByObjectId(repositoryId, objectId));
                    break;
                case JOB:
                    page += "job/jobMetaInfo";
                    model.addAttribute("jobMeta", kettleRepositoryService.getJobMetaByObjectId(repositoryId, objectId));
                    break;
                case DATABASE:
                    break;
                case SLAVE_SERVER:
                    break;
                case CLUSTER_SCHEMA:
                    break;
                case PARTITION_SCHEMA:
                    break;
                case STEP:
                    break;
                case JOB_ENTRY:
                    break;
                case TRANS_DATA_SERVICE:
                    break;
                case PLUGIN:
                    break;
                case UNKNOWN:
                    break;
            }

        } catch (KettleException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return page;
    }

    @GetMapping("/previewElementInfo/{repositoryId}/{objectId}/{type}")
    public String previewTrans(@PathVariable Long repositoryId, @PathVariable String objectId, @PathVariable RepositoryObjectType type, Model model) {
        String page = "kettle/";
        model.addAttribute("objectId", objectId);
        model.addAttribute("repositoryId", repositoryId);
        switch (type) {
            case TRANSFORMATION:
                page += "trans/preview";
                break;
            case JOB:
                page += "job/preview";
                break;
        }
        return page;
    }

        //---------------------------------------------------------------------
        // trans相关
        //---------------------------------------------------------------------

        @RequestMapping("/trans/view")
        public String listTrans(Model model, Long repositoryId){
            try {
                List<KettleRepository> repositoryList = kettleRepositoryService.getConnectedRepository();
                List<RepositoryElementMetaInterface> elementList = new ArrayList<>();
                if (!repositoryList.isEmpty()) {
                    if (repositoryId == null) {
                        repositoryId = repositoryList.get(0).getId();
                    }
                    elementList = kettleRepositoryService.getAllTrans(repositoryId, "/");
                }
                model.addAttribute("repositoryId", repositoryId);
                model.addAttribute("repositoryList", repositoryList);
                model.addAttribute("transList", elementList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "kettle/trans/index";
        }

        @PostMapping("/previewTrans/{repositoryId}/{objectId}")
        @ResponseBody
        public StepWrapper previewTrans(@PathVariable Long repositoryId, @PathVariable String objectId, Model model){
            try {
                return kettleRepositoryService.getTransStepWrapper(repositoryId, objectId);
            } catch (KettleException e) {
                e.printStackTrace();
            }
            return null;
        }

    @ApiOperation(value = "执行ETL转换")
    @ApiImplicitParams({@ApiImplicitParam(name = "repositoryId", value = "资源库ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "objectId", value = "转换ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "params", value = "params", required = true, dataType = "Object")})
    @PostMapping("/job/run")
    @ResponseBody
    public KettleLog runTrans(Long repositoryId, String objectId, KettleParams params){
        try {
            return kettleRepositoryService.runJob(repositoryId, objectId, params.getKettleParams());
        } catch (KettleException e) {
            return KettleLog.createErrorLog(repositoryId, objectId, e.getMessage());
        } catch (Exception e) {
            return KettleLog.createErrorLog(repositoryId, objectId, "执行失败:" + e.getMessage());
        }
    }

        //---------------------------------------------------------------------
        // Job相关
        //---------------------------------------------------------------------

        @RequestMapping("/job/view")
        public String listJobs (Model model, Long repositoryId){
            try {
                List<KettleRepository> repositoryList = kettleRepositoryService.getConnectedRepository();
                List<RepositoryElementMetaInterface> elementList = new ArrayList<>();
                if (!repositoryList.isEmpty()) {
                    if (repositoryId == null) {
                        repositoryId = repositoryList.get(0).getId();
                    }
                    elementList = kettleRepositoryService.getAllJobs(repositoryId, "/");
                }
                model.addAttribute("repositoryId", repositoryId);
                model.addAttribute("repositoryList", repositoryList);
                model.addAttribute("transList", elementList);
            } catch (Exception e) {
                model.addAttribute("error", "获取资源库信息失败！");
            }
            return "kettle/job/index";
        }

        @ApiOperation(value = "执行ETL作业")
        @ApiImplicitParams({@ApiImplicitParam(name = "repositoryId", value = "资源库ID", required = true, dataType = "Long"),
                @ApiImplicitParam(name = "objectId", value = "作业ID", required = true, dataType = "String"),
                @ApiImplicitParam(name = "params", value = "params", required = true, dataType = "Object")})
        @PostMapping("/job/run")
        @ResponseBody
        public KettleLog runJob (Long repositoryId, String objectId, KettleParams params){
            try {
                return kettleRepositoryService.runJob(repositoryId, objectId, params.getKettleParams());
            } catch (KettleException e) {
                return KettleLog.createErrorLog(repositoryId, objectId, e.getMessage());
            } catch (Exception e) {
                return KettleLog.createErrorLog(repositoryId, objectId, "执行失败:" + e.getMessage());
            }
        }


    }
