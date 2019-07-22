package com.haze.web;

import com.haze.demo.service.TowerService;
import com.haze.spatial.epsg.crs.CRSUtils;
import com.haze.spatial.shapefile.ShapeFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(value = "铁塔接口 ")
@RestController
@RequestMapping("/demo")
public class DemoController {

    private TowerService towerService;

    @Autowired
    public void setTowerService(TowerService towerService) {
        this.towerService = towerService;
    }

    @ApiOperation(value = "保存铁塔")
    @ApiImplicitParams({@ApiImplicitParam(name = "siteNum", value = "站址编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "站址名称", required = true, dataType = "String")})
    @PostMapping("/saveTower")
    @ResponseBody
    public String saveTower(Model model, @RequestParam String siteNum, @RequestParam String name, @RequestParam String x, @RequestParam String y) {
        towerService.saveTower(siteNum, name, x, y);
        return "success";
    }

    @GetMapping("/testShape")
    @ResponseBody
    public String testShape() {
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "postgis");
        params.put("host", "localhost");
        params.put("port", 5432);
        params.put("schema", "public");
        params.put("database", "gis");
        params.put("user", "postgres");
        params.put("passwd", "1234");
        ShapeFileUtils.importToDatabase(params,"D:\\world\\jz1\\基础数据\\湖.shp");
        return "success";
    }
}
