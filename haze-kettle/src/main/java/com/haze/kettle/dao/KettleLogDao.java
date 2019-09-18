package com.haze.kettle.dao;

import com.haze.core.jpa.repository.BaseRepository;
import com.haze.kettle.entity.KettleLog;
import org.springframework.stereotype.Repository;

@Repository
public interface KettleLogDao extends BaseRepository<KettleLog, Long> {

}
