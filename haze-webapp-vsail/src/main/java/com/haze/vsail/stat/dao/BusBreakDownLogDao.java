package com.haze.vsail.stat.dao;

import com.haze.vsail.stat.entity.BusBreakDownLog;
import com.haze.vsail.stat.entity.BusOnOffLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BusBreakDownLogDao extends JpaRepository<BusBreakDownLog, Long> , JpaSpecificationExecutor<BusBreakDownLog> {

}
