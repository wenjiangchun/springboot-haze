package com.haze.vsail.stat.dao;

import com.haze.core.jpa.repository.BaseRepository;
import com.haze.vsail.stat.entity.BusTest;
import org.springframework.stereotype.Repository;

@Repository
public interface BusTestDao extends BaseRepository<BusTest, Long> {

}
