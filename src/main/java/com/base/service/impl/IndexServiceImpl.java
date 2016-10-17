package com.base.service.impl;

import com.base.persistence.IndexMapper;
import com.base.service.IndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liangjun on 16/7/2.
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper indexMapper;

    public int getIndexCount() {
        return indexMapper.getIndexCount();
    }
}
