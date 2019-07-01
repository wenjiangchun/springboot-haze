package com.haze.demo.service;

import com.haze.core.service.AbstractBaseService;
import com.haze.demo.dao.TowerDao;
import com.haze.demo.entity.Tower;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Tower tower = new Tower();
        tower.setSiteNum(siteNum);
        tower.setName(name);
        GeometryFactory geometryFactory = new GeometryFactory();
        Point p = geometryFactory.createPoint(new Coordinate(Double.valueOf(x),Double.valueOf(y)));
        p.setSRID(4326);
        tower.setLocation(p);
        towerDao.save(tower);
        logger.debug("save tower【{}】", tower);
    }
}
