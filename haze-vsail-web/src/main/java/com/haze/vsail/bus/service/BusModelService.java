package com.haze.vsail.bus.service;

import com.haze.core.service.AbstractLogicDeletedService;
import com.haze.core.service.NameAlreadyExistException;
import com.haze.vsail.bus.dao.BusModelDao;
import com.haze.vsail.bus.entity.BusModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BusModelService extends AbstractLogicDeletedService<BusModel, Long> {

    private BusModelDao busModelDao;

    public BusModelService(BusModelDao busModelDao) {
        super(busModelDao);
        this.busModelDao = busModelDao;
    }

    @Transactional
    public BusModel saveOrUpdate(BusModel busModel) throws NameAlreadyExistException {
        Objects.requireNonNull(busModel, "车辆配置信息不能为空");
        Date date = new Date();
        if (busModel.isNew()) {
            List<BusModel> u = this.busModelDao.findByProperty("name", busModel.getName());
            if (!u.isEmpty()) {
                logger.error("车辆型号名称{}已存在", busModel.getName());
                throw new NameAlreadyExistException("车辆型号名称" + busModel.getName() + "已存在");
            } else {
                busModel.setCreateTime(date);
                busModel.setUpdateTime(date);
                logger.debug("添加车辆型号信息：{}", busModel);
            }
        } else {
            busModel.setUpdateTime(date);
            logger.debug("更新车辆型号信息：{}", busModel);
        }
        busModel = this.busModelDao.save(busModel);
        return busModel;
    }

    @Transactional
    @Override
    public BusModel save(BusModel t) throws Exception {
        t.setDeleted(false);
        return saveOrUpdate(t);
    }

}
