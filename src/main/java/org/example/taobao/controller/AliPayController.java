package org.example.taobao.controller;


import cn.hutool.json.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.easysdk.factory.Factory;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.taobao.config.AliPayConfig;
import org.example.taobao.dto.OrderId;
import org.example.taobao.mapper.CommonFatherOrderMapper;
import org.example.taobao.mapper.CommonOrderMapper;
import org.example.taobao.mapper.CommonOrderSkuMapper;
import org.example.taobao.mapper.OrderMapper;
import org.example.taobao.pojo.AliPay;
import org.example.taobao.pojo.CommonFatherOrder;
import org.example.taobao.pojo.CommonOrder;
import org.example.taobao.pojo.Order;

import org.example.taobao.service.PayService;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.example.taobao.utils.IdGenerationTest.generateId;

/**
 * @author 关岁安
 */
@RestController
@RequestMapping("alipay")
@Transactional(rollbackFor = Exception.class)
public class AliPayController {
    @Resource
    AliPayConfig aliPayConfig;

    @Autowired
    private PayService payService;

    @Autowired
    private CommonOrderMapper commonOrderMapper;

    @Autowired
    private CommonOrderSkuMapper commonOrderSkuMapper;

    @Autowired
    private CommonFatherOrderMapper commonFatherOrderMapper;

    @Resource
    private OrderMapper orderMapper;
    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT ="JSON";
    private static final String CHARSET ="utf-8";
    private static final String SIGN_TYPE ="RSA2";



    @GetMapping("/pay2") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay2(HttpServletResponse httpResponse,String orderId) throws Exception {
        Long id = Long.parseLong(orderId);

        CommonOrder commonOrder = commonOrderMapper.gainCommonOrderByOrderId(id);
        if(commonOrder == null){
            return;
        }
        //1.创建Client
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        //2.创建Request设置Request参数
        //发送请求的request类
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        //获取到我们自己生成的订单编号
        bizContent.set("out_trade_no",commonOrder.getOrderId());
        bizContent.set("total_amount",commonOrder.getTotalPrice());
        bizContent.set("subject","普通单订单逻辑");
        bizContent.set("product_code","FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:5173/home");
        //执行请求，拿到响应的结果，返回给浏览器
        String from = "";
        try {
            from = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(from);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * 普通多订单的逻辑
     * 获取曾祖父的订单编号和总金额
     * @param httpResponse
     * @param orderId
     * @throws Exception
     */

    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(HttpServletResponse httpResponse,String orderId) throws Exception {
        Long id = Long.parseLong(orderId);
        CommonFatherOrder commonFatherOrder = commonFatherOrderMapper.gainTotalPrice(id);

        if(commonFatherOrder == null){
            return;
        }
        //1.创建Client
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        //2.创建Request设置Request参数
        //发送请求的request类
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        //获取到我们自己生成的订单编号
        bizContent.set("out_trade_no",commonFatherOrder.getCommonFatherOrderId());
        bizContent.set("total_amount",commonFatherOrder.getTotalPrice());
        bizContent.set("subject","普通多订单逻辑");
        bizContent.set("product_code","FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:5173/home");
        //执行请求，拿到响应的结果，返回给浏览器
        String from = "";
        try {
            from = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(from);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }


    //秒杀订单的逻辑
    @GetMapping("/pay1") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay1(HttpServletResponse httpResponse,String orderId) throws Exception {
        // 将 String 转换为 Long
        Long id = Long.parseLong(orderId);
        Order order = orderMapper.gainOrder(id);
        if(order == null){
            return;
        }
        //1.创建Client
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        //2.创建Request设置Request参数
        //发送请求的request类
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        //获取到我们自己生成的订单编号
        bizContent.set("out_trade_no",order.getId());
        bizContent.set("total_amount",order.getTotalPrice());
        bizContent.set("subject","秒杀订单的逻辑");
        bizContent.set("product_code","FAST_INSTANT_TRADE_PAY");
        bizContent.set("get_payment","guansuian");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:5173/home");
        //执行请求，拿到响应的结果，返回给浏览器
        String from = "";
        try {
            from = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(from);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }


    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {

            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                // System.out.println(name + " = " + request.getParameter(name));
            }
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content,sign,aliPayConfig.getAlipayPublicKey(),"UTF-8");
            if(checkSignature){
                System.out.println("交易名称:" + params.get("subject"));
                System.out.println("交易状态:" + params.get("trade_status"));
                System.out.println("支付宝交易凭证号:" + params.get("trade_no"));
                System.out.println("商户订单号:" + params.get("out_trade_no"));
                System.out.println("交易金额:" + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id:" + params.get("buyer_id"));
                System.out.println("买家付款时间:" + params.get("get_payment"));
                System.out.println("买家支付的金额:" + params.get("buyer_pay_amount"));
                System.out.println("看看有没有关岁安:" + params.get("out_trade_no"));

                System.out.println(sign);
                String tradeNo = params.get("out_trade_no");
                String alipayTradeNo = params.get("trade_no");
                LocalDateTime currentTime = LocalDateTime.now();
                Long orderId = Long.valueOf(tradeNo);
                if("秒杀订单的逻辑".equals(params.get("subject"))){
                    orderMapper.updateOrderHasPay(orderId,currentTime,alipayTradeNo);
                    System.out.println("进入秒杀商品订单逻辑");
                }else if("普通多订单逻辑".equals(params.get("subject"))){
                    //代表支付成功需要将全部的子订单的状态修改为已支付
                    System.out.println("进入普通多订单逻辑");
                    commonOrderMapper.updateCommonOrderStatusByFatherId(orderId,currentTime,alipayTradeNo);
                }else if("普通单订单逻辑".equals(params.get("subject"))){
                    System.out.println("进入单订单逻辑");
                    commonOrderMapper.updateCommonOrderStatusBuOrderId(orderId,currentTime,alipayTradeNo);
                }else{
                    System.out.println(params.get("subject"));
                }

            }
        }
        return "success";
    }

//    ,HttpServletResponse response, HttpSession session
    @PostMapping("refund")
    public void toRefund( @RequestBody OrderId id ,HttpServletResponse response, HttpSession session) throws IOException, AlipayApiException {
        payService.refund(response,session,id.getOrderId());
    }

//        payService.refund(response,session,orderId);
}
