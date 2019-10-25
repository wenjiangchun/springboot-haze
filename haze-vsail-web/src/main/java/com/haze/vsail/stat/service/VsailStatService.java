package com.haze.vsail.stat.service;

import com.haze.core.jpa.repository.HazeSpecification;
import com.haze.vsail.stat.dao.BusOnOffLogDao;
import com.haze.vsail.stat.entity.BusOnOffLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VsailStatService {

    private BusOnOffLogDao busOnOffLogDao;

    public VsailStatService(BusOnOffLogDao busOnOffLogDao) {
        this.busOnOffLogDao = busOnOffLogDao;
    }

    /**
     * 获取车辆上线下线分页列表
     * @param pageable 分页信息
     * @param queryParams 查询参数
     * @return 车辆上线下线分页数据
     */
    public Page<BusOnOffLog> findOnOffPage(Pageable pageable, Map<String, Object> queryParams) {
        Specification<BusOnOffLog> spec = new HazeSpecification<>(queryParams);
        return this.busOnOffLogDao.findAll(spec, pageable);
    }
}
