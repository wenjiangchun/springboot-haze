package com.haze.kettle.dao;

import com.haze.core.jpa.repository.BaseRepository;
import com.haze.kettle.entity.KettleRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KettleRepositoryDao extends BaseRepository<KettleRepository, Long> {

}
