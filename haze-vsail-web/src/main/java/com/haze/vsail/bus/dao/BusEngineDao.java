package com.haze.vsail.bus.dao;

import com.haze.core.jpa.repository.BaseRepository;
import com.haze.vsail.bus.entity.BusEngine;
import org.springframework.stereotype.Repository;

@Repository
public interface BusEngineDao extends BaseRepository<BusEngine, Long> {

    BusEngine findByName(String name);

}
