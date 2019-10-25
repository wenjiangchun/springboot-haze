package com.haze.vsail.bus.dao;

import com.haze.core.jpa.repository.BaseRepository;
import com.haze.vsail.bus.entity.Bus;
import com.haze.vsail.bus.entity.BusModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BusDao extends BaseRepository<Bus, Long> {

}
