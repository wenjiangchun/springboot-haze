package com.haze.system.dao;

import java.util.List;

import com.haze.core.jpa.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.haze.system.entity.Group;

/**
 * 组织机构Dao接口定义
 * @author wenjiangchun
 *
 */
@Repository
public interface GroupDao extends BaseRepository<Group, Long> {

	/**
	 * 查询顶级机构信息
	 * @return 顶级机构信息列表
	 */
	@Query("from Group g where g.parent is null")
	List<Group> getTopGroups();

	/**
	 * 根据code查询顶级机构信息
	 * @return 顶级机构信息
	 */
	@Query("from Group g where g.parent is null and code=:rootCode")
	Group getRootGroup(@Param("rootCode")String rootCode);

}
