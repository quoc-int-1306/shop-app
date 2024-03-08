package com.project.shopapp.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.Responses.ProductResponse;
import com.project.shopapp.Service.impl.IProductRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRedisService implements IProductRedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper redisObjectMapper;


    private String getKeyFrom(String keyword,
                              Long categoryId,
                              PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = sort.getOrderFor("id").
                getDirection() == Sort.Direction.ASC ? "asc": "desc";
        String key = String.format("all_products:%d:%d:%d", pageNumber, pageSize, pageRequest);
        return key;
    }

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public List<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<ProductResponse> productResponses = json != null ?
                redisObjectMapper.readValue(json, new TypeReference<List<ProductResponse>>() {}) : null;

        return productResponses;
    }

    @Override
    public void saveAllProducts(List<ProductResponse> productResponses, String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key, json);
    }
}
