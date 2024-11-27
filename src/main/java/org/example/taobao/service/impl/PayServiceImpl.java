//package org.example.taobao.service.impl;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.request.AlipayTradeRefundRequest;
//import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletResponse;
//import org.example.taobao.config.AliPayConfig;
//import org.example.taobao.mapper.OrderMapper;
//import org.example.taobao.pojo.Order;
//import org.example.taobao.service.PayService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import jakarta.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.UUID;
//
///**
// * @author 关岁安
// */
//@Service
//public class PayServiceImpl implements PayService {
//    @Resource
//    AliPayConfig aliPayConfig;
//
//    @Autowired
//    private OrderMapper orderMapper;
//
//
//    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
//    private static final String FORMAT ="JSON";
//    private static final String CHARSET ="utf-8";
//    private static final String SIGN_TYPE ="RSA2";
//    @Override
//    public void refund(HttpServletResponse response, HttpSession session,Integer orderId) throws IOException, AlipayApiException {
//
//        //获取Order对象
//        Order order = orderMapper.gainOrder(orderId);
//        String payNo = order.getPayNo();
//        Integer money = order.getTotalPrice();
//        // 通过空字符串连接
//        String moneyString = "" + money;
//        // 设置编码格式
//        response.setContentType("text/html;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        //1.创建Client
//        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
//                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
//        //设置请求参数
//        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
//        //商户订单号，必填
//        String out_trade_no = payNo;
//        //需要退款的金额，该金额不能大于订单金额，必填
//        String refund_amount = moneyString;
//        //标识一次退款请求，同一笔交易多次退款需要保证唯一。如需部分退款，则此参数必传；不传该参数则代表全额退款
//        String out_request_no = UUID.randomUUID().toString();
//        //trade_no的字符也需要写进去
//        String trade_no = null;
//        // 字符转义很重要
//        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
//                + "\"refund_amount\":\""+ refund_amount +"\","
//                + "\"out_request_no\":\""+ out_request_no +"\"}");
//        //请求
//        String result = alipayClient.execute(alipayRequest).getBody();
//        //输出
//        //以下写自己的订单退款代码
//        out.println(result);
////        logger.info("返回结果={}",result);
//
//    }
//}
package org.example.taobao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.taobao.config.AliPayConfig;
import org.example.taobao.mapper.CommonOrderMapper;
import org.example.taobao.mapper.OrderMapper;
import org.example.taobao.pojo.CommonOrder;
import org.example.taobao.pojo.Order;
import org.example.taobao.service.PayService;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author 关岁安
 */
@Service
public class PayServiceImpl implements PayService {
    @Resource
    AliPayConfig aliPayConfig;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CommonOrderMapper commonOrderMapper;


    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT ="JSON";
    private static final String CHARSET ="utf-8";
    private static final String SIGN_TYPE ="RSA2";
    @Override
    public void refund(HttpServletResponse response, HttpSession session, String orderId) throws IOException, AlipayApiException {
        Long orderIdNumber = Long.valueOf(orderId);

        CommonOrder commonOrder = commonOrderMapper.gainCommonOrderByOrderId(orderIdNumber);
        String payNo = commonOrder.getPayNo();
        String fatherOrderIdString = String.valueOf(commonOrder.getFatherOrderId());
        String moneyString = String.valueOf(commonOrder.getTotalPrice());
        // 设置编码格式
        response.setContentType("text/html;charset=utf-8");
        //1.创建Client
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        Map<String, Object> paraMap = new HashMap<String,Object>();
        String out_request_no = new String(UUID.randomUUID().toString());

        paraMap.put("trade_no",payNo);
        paraMap.put("refund_amount",moneyString);
//        paraMap.put("business_params",orderId);
        List<Object> list = new ArrayList<>();
        list.add(orderId);
        paraMap.put("refund_reason",orderId);
        paraMap.put("query_options",list);
        paraMap.put("out_request_no",orderId);
        System.out.println("退款ID："+orderId);
//        paraMap.put("operator_id" , orderId);

        // 设置查询选项
        String str = JSON.toJSONString(paraMap);
        alipayRequest.setBizContent(str);
        //请求
        AlipayTradeRefundResponse execute = alipayClient.execute(alipayRequest);
        String result = execute.getBody();
        System.out.println(result);
        //输出
        //以下写自己的订单退款代码

        if (execute.isSuccess()) {
            System.out.println("调用成功");


        } else {
            System.out.println("调用失败");
        }

    }

}