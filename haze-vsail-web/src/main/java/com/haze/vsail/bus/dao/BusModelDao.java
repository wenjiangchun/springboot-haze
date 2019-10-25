package com.haze.vsail.bus.dao;

import com.haze.core.jpa.repository.BaseRepository;
import com.haze.vsail.bus.entity.BusModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BusModelDao extends BaseRepository<BusModel, Long> {

}
