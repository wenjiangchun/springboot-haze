package com.haze.demo.dao;

import com.haze.core.jpa.repository.BaseRepository;
import com.haze.demo.entity.Tower;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TowerDao extends BaseRepository<Tower, Long> {

    @Query(value = "select * from s_tower",name = "selectTower", nativeQuery = true)
    List<Tower> findByExample();
}
