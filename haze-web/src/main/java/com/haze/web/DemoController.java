package com.haze.web;

import com.haze.demo.service.TowerService;
import com.haze.spatial.shapefile.ShapeFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/demo")
public class DemoController {

    private TowerService towerService;

    @Autowired
    public void setTowerService(TowerService towerService) {
        this.towerService = towerService;
    }

    @RequestMapping("/saveTower")
    @ResponseBody
    public String saveTower(Model model, @RequestParam String siteNum, @RequestParam String name, @RequestParam String x, @RequestParam String y) {
        towerService.saveTower(siteNum, name, x, y);
        return "success";
    }

    @RequestMapping("/testShape")
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
        //ShapefileUtils.import2Database(params, "d:\\world\\test.shp");
        //ShapeFileUtils.createTable(params);
        //ShapeFileUtils.importToDatabase(params,"d:\\world1\\world_adm0.shp");
         ShapeFileUtils.importToDatabase(params,"D:\\world\\jz1\\行政区划\\11.shp");
        //ShapeFileUtils.exportFromDatabase(params, "world_adm0", "d:\\world1\\");
        return "success";
    }
}
