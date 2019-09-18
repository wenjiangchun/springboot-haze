package com.haze.web;

import com.haze.core.jpa.entity.BaseEntity;
import com.haze.core.service.BaseService;
import com.haze.web.datatable.DataTablePage;
import com.haze.web.datatable.DataTableParams;
import com.haze.web.utils.WebMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

public class BaseCrudController<T extends BaseEntity, PK extends Serializable> extends BaseController {

    /**
     * 模块英文名称
     */
    private String moduleName;

    /**
     * 子功能英文名称
     */
    private String name;

    /**
     * 子功能中文说明
     */
    private String cname;

    /**
     * 对应业务类 具体在字类注入
     */
    private BaseService<T, PK> service;

    public BaseCrudController(String moduleName, String name, String cname, BaseService<T, PK> service) {
        this.moduleName = moduleName;
        this.name = name;
        this.cname = cname;
        this.service = service;
    }

    public BaseService<T, PK> getService() {
        return this.service;
    }

    @GetMapping(value = "view")
    public String list(Model model, HttpServletRequest request) {
        setModel(model, request);
        return moduleName + "/" + name + "/" + "list";
    }

    @PostMapping(value = "search")
    @ResponseBody
    public DataTablePage search(DataTableParams dataTableParams, HttpServletRequest request) {
        PageRequest p = dataTableParams.getPageRequest();
        setPageQueryVariables(dataTableParams.getQueryVairables(), request);
        Page<T> list = this.service.findPage(p, dataTableParams.getQueryVairables());
        return DataTablePage.generateDataTablePage(list, dataTableParams);
    }

    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        setModel(model, request);
        return moduleName + "/" + name + "/" + "add";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public WebMessage save(T t, HttpServletRequest request) {
        try {
            this.service.save(t);
            logger.debug("保存/更新{}成功,{}", cname, t);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            logger.error("保存/更新{}失败,{},e={}", cname, t, e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    @GetMapping(value = "edit/{id}")
    public String edit(@PathVariable PK id, Model model, HttpServletRequest request) {
        T t = this.service.findById(id);
        model.addAttribute(name, t);
        setModel(model, request);
        return moduleName + "/" + name + "/" + "edit";
    }

    @RequestMapping(value = "delete/{ids}")
    @ResponseBody
    public WebMessage delete(@PathVariable("ids") PK[] ids, HttpServletRequest request) {
        try {
            this.service.deleteIds(ids);
            logger.debug("删除{}成功,ids=[{}]", cname, ids);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception e) {
            logger.debug("删除{}失败,ids=[{}], e={}", cname, ids, e);
            return WebMessage.createErrorWebMessage(e.getMessage());
        }
    }

    /**
     * 分页参数处理
     * <pre>
     *     对一些需要把接受到的参数数据进一步处理的需要，由字类重写该方法
     * </pre>
     * @param queryVariables 分页参数
     * @param request HttpServletRequest
     */
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {
        //默认不实现 交由字类完成
    }

    /**
     * 页面参数接收赋值
     * @param model Model
     * @param request HttpServletRequest
     */
    protected void setModel(Model model, HttpServletRequest request) {
        model.addAttribute("cname", cname);
        model.addAttribute("name", name);
    }
}
