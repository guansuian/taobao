package org.example.taobao.service;

import org.example.taobao.vo.Result;

/**
 * @author 关岁安
 */
public interface AuditService {

    void passShopping(Integer id);

    void refuseShopping(Integer id,String summary);
}
