package com.project.shopapp.Models;

import com.project.shopapp.Service.impl.IProductRedisService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class ProductListener {
    @Autowired
    private IProductRedisService productRedisService;

    private static final Logger logger = LoggerFactory.getLogger(ProductListener.class);
    private Long id;

    @PrePersist
    public void prePersit(Product product) {
        logger.info("prePersit");
    }


    @PreUpdate
    public void preUpdate(Product product) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(Product product) {
        // Update Redis cache
        logger.info("postUpdate");
        productRedisService.clear();
    }

    @PreRemove
    public void preRemove(Product product) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preRemove");
    }

    @PostRemove
    public void postRemove(Product product) {
        // Update Redis cache
        logger.info("postRemove");
        productRedisService.clear();
    }
}
