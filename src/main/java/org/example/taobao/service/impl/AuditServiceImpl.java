package org.example.taobao.service.impl;

import org.example.taobao.mapper.AuditMapper;
import org.example.taobao.service.AuditService;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 关岁安
 */
@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditMapper auditMapper;



    @Override
    public void passShopping(Integer id) {
        auditMapper.passShopping(id);
    }

    @Override
    public void refuseShopping(Integer id, String summary) {
        auditMapper.refuseShopping(id,summary);
    }
}
