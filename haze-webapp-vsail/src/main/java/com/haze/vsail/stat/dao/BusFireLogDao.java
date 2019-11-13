package com.haze.vsail.stat.dao;

import com.haze.vsail.stat.entity.BusFireLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BusFireLogDao extends JpaRepository<BusFireLog, Long> , JpaSpecificationExecutor<BusFireLog> {

}
