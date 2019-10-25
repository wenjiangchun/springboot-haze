package com.haze.vsail.stat.service;

import com.haze.core.service.AbstractBaseService;
import com.haze.vsail.stat.dao.BusTestDao;
import com.haze.vsail.stat.entity.BusTest;
import org.springframework.stereotype.Service;

@Service
public class BusTestService extends AbstractBaseService<BusTest, Long> {

    private BusTestDao busTestDao;

    public BusTestService(BusTestDao busTestDao) {
        super(busTestDao);
        this.busTestDao = busTestDao;
    }


}
