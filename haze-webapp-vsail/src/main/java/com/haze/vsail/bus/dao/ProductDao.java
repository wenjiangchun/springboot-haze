package com.haze.vsail.bus.dao;

import com.haze.core.jpa.repository.BaseRepository;
import com.haze.vsail.bus.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends BaseRepository<Product, Long> {

}
