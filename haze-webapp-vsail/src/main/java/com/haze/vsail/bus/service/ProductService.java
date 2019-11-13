package com.haze.vsail.bus.service;

import com.haze.core.service.AbstractLogicDeletedService;
import com.haze.vsail.bus.dao.ProductDao;
import com.haze.vsail.bus.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductService extends AbstractLogicDeletedService<Product, Long> {

    private ProductDao productDao;

    public ProductService(ProductDao dao) {
        super(dao);
        this.productDao = dao;
    }

}
