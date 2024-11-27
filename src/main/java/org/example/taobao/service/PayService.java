package org.example.taobao.service;

import com.alipay.api.AlipayApiException;
import jakarta.servlet.http.HttpServletResponse;


import jakarta.servlet.http.HttpSession;
import org.example.taobao.vo.Result;

import java.io.IOException;

/**
 * @author 关岁安
 */
public interface PayService {

    void refund(HttpServletResponse response, HttpSession session, String orderId) throws IOException, AlipayApiException;
}
