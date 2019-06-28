package com.haze.demo.dao;

import com.haze.core.jpa.BaseRepository;
import com.haze.demo.entity.Tower;
import org.springframework.stereotype.Repository;

@Repository
public interface TowerDao extends BaseRepository<Tower, Long> {

}
