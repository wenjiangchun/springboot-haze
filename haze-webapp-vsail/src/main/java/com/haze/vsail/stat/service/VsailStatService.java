package com.haze.vsail.stat.service;

import com.haze.core.jpa.repository.HazeSpecification;
import com.haze.vsail.stat.dao.BusBreakDownLogDao;
import com.haze.vsail.stat.dao.BusFireLogDao;
import com.haze.vsail.stat.dao.BusOnOffLogDao;
import com.haze.vsail.stat.entity.BusBreakDownLog;
import com.haze.vsail.stat.entity.BusFireLog;
import com.haze.vsail.stat.entity.BusOnOffLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VsailStatService {

    private BusOnOffLogDao busOnOffLogDao;

    private BusFireLogDao busFireLogDao;

    private BusBreakDownLogDao busBreakDownLogDao;

    public VsailStatService(BusOnOffLogDao busOnOffLogDao, BusFireLogDao busFireLogDao, BusBreakDownLogDao busBreakDownLogDao) {
        this.busOnOffLogDao = busOnOffLogDao;
        this.busFireLogDao = busFireLogDao;
        this.busBreakDownLogDao = busBreakDownLogDao;
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

    /**
     * 获取车辆火警分页列表
     * @param pageable 分页信息
     * @param queryParams 查询参数
     * @return 车辆上线下线分页数据
     */
    public Page<BusFireLog> findFirePage(Pageable pageable, Map<String, Object> queryParams) {
        Specification<BusFireLog> spec = new HazeSpecification<>(queryParams);
        return this.busFireLogDao.findAll(spec, pageable);
    }

    /**
     * 获取车辆上线下线分页列表
     * @param pageable 分页信息
     * @param queryParams 查询参数
     * @return 车辆上线下线分页数据
     */
    public Page<BusBreakDownLog> findBreakDownPage(Pageable pageable, Map<String, Object> queryParams) {
        Specification<BusBreakDownLog> spec = new HazeSpecification<>(queryParams);
        return this.busBreakDownLogDao.findAll(spec, pageable);
    }
}
