package com.haze.demo.service;

import com.haze.core.service.AbstractBaseService;
import com.haze.demo.dao.TowerDao;
import com.haze.demo.entity.Tower;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TowerService extends AbstractBaseService<Tower, Long>  {

    private TowerDao towerDao;

    @Autowired
    public void setTowerDao(TowerDao towerDao) {
        this.towerDao = towerDao;
        super.setDao(towerDao);
    }

    public void saveTower(String siteNum, String name, String x, String y) {
        List<Tower> rs =  this.towerDao.findByExample();
        Tower tower = new Tower();
        tower.setSiteNum(siteNum);
        tower.setName(name);
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point p = geometryFactory.createPoint(new Coordinate(Double.valueOf(x),Double.valueOf(y)));
        //p.setSRID(4326);
        tower.setGeom(p);
        towerDao.save(tower);
        logger.debug("save tower【{}】", tower);
    }
}
