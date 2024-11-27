package org.example.taobao.pojo;

import lombok.Data;

/**
 * @author 关岁安
 */
@Data
public class AliPay {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}
