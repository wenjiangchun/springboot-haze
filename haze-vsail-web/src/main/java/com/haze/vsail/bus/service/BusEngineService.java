package com.haze.vsail.bus.service;

import com.haze.core.service.AbstractLogicDeletedService;
import com.haze.core.service.NameAlreadyExistException;
import com.haze.vsail.bus.dao.BusEngineDao;
import com.haze.vsail.bus.entity.BusEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
public class BusEngineService extends AbstractLogicDeletedService<BusEngine, Long> {

    private BusEngineDao busEngineDao;

    public BusEngineService(BusEngineDao busEngineDao) {
        super(busEngineDao);
        this.busEngineDao = busEngineDao;
    }

    public BusEngine saveOrUpdate(BusEngine busEngine) throws NameAlreadyExistException {
        Objects.requireNonNull(busEngine, "车辆主机信息不能为空");
        Date date = new Date();
        if (busEngine.isNew()) {
            BusEngine u = this.busEngineDao.findByName(busEngine.getName());
            if (u != null) {
                logger.error("车辆主机名称{}已存在", busEngine.getName());
                throw new NameAlreadyExistException("车辆主机名称" + busEngine.getName() + "已存在");
            } else {
                busEngine.setCreateTime(date);
                busEngine.setUpdateTime(date);
                logger.debug("车辆主机信息：{}", busEngine);
            }
        } else {
            busEngine.setUpdateTime(date);
            logger.debug("更新车辆主机信息：{}", busEngine);
        }
        busEngine = this.busEngineDao.save(busEngine);
        return busEngine;
    }

    @Override
    public BusEngine save(BusEngine t) throws Exception {
        return saveOrUpdate(t);
    }

}
