package org.example.taobao.config;

/**
 * @author 关岁安
 */
public class AliRefundPay {

    // AppId
    public static String app_id = "2016102100733014";

    // 商户私钥
    public static  String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCINPCa++wN4rtYoWj/QWCSRw55yOXYx1YCzmzGiWDpKq7onueLVEXu43BtvRdQyX4zW4aUUrqYgzkcFbuhpEUP1Ev+yN530BjUBZOJEx3cGd8Yw1QQtClnxhApq5YJDpbcEUl1AVe5ykXhd9clmXzx107UL9DLoUG1Au0NW9pNkarHsnWi+f/Y00K9EjP8wWYTQNslHwlzdj59BA+gWy18MkPKyy9cm1uB001zuEIF6iR6HaJ/pZJVScL+r8CKiOKco9J8h9gKVAtW7s1xJ1bFfxGKRgUaLWdL3ErAAZ7nRnRS1Ih9QhD625AyW2ZycLO4wH3tn78scwePBICeuN9/AgMBAAECggEAA7hecf07f02YDF2sPnyMpLIoJQ9lp7waD02QDwQlj3jI+jWM4XNVzaaMpZKqllxKhCCV1mtECXJcFvZI/6s676m+waC+4sreQrw8QVwyTpauT0DWhPZGwzzXqCu1l128pRobxYRZ3ws1SWk6iR5eGUTmNElyDH3FC6qeyuSdW8z6bHx55UGZ6XGhU/3kRca6E3CRuOuTrCfncqnal/qOiLK7U53ujym+5aOkAERMepmeQ/jnftoR6VvxZDS86ZIW+SZ9McG6IR44b2i+/V+AXDT9crSVAPIvFEn6Q4AMwSGcfqr3VnJWN3B7tcRT+DwSwoYJnl2YY9V80x/rNP5f0QKBgQDXxaLsBaNu65d7dXC9z3Koom/dRb/duHDejfXl1G1wohBNOtsjKrK8VYTSACpxRageK6I6eMz0C8q3InbbmB8KbVc9vGW7WHbsavjjEuls8xCRSIvvlGiig3Z8l1FlLphqd6HJTVarhhp6VI2ZzaW4EWjo6S9c4QmyjQ04OLcMHQKBgQChmdDXdDimzUNkFIrTwebuVB7pox5xRmE/h+WnGmxgkd8HGjKrEq/6gXe49jp5GKDdLSKGqs42QKZpXzUEFJgapzleFT1NMxnS48hhDhUMeUg4yruOS9HsMBOieHJn49GPZNuBfOi6J2zTwcRl4JFZGe1xfdRjO/uv4Pc8QSsvSwKBgEsYfNh6KeRb2E/eksts3LH2PsqbEOjA3X+HltJTeXde4B+Q4V2+6ZAEj4JrDlp37Mf7Va3WBzbUt8u/EfDhY8REQ723yQxIALPNkyuslFMX0WdPAWzzkOsmp+E65Ng7PE6prkQaR5TQynxS2ylxWkDSxllxdstzguWCk9xCWOq9AoGAAsqRXOdZAyiQfW4NZ9L1ljBFyGtU30gD7gD+6n/+VzytyYWeUm4kNAOAeeVm5SiTvr5E1mak4TMErNVEKSnh5rP0vbdIM8kqLQZaM7L2khyWLsTxsGeG3LmH3UqPT1QUDpIDaJ0rt+tuacnzBn92gz97ZnZx8LTciBEutA+8GzkCgYB+dRYBexBKvB5X1k4nP2ICub2Jjx5DC/cCspsjK/tjceTDzNFl8gbi03XRHjfHDSd+TR5nhHkYqvEIgqBMNUqL3fy4Giv5frgJyYdElbNYZyXJjlPcDGA4Uz4juSjjyOUKZZn7PAHu/hujtP1JUBfaTBvdZp+OKXvE2SNtavlG8Q==";

    // 支付宝公钥
    public static  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA72i7PdKclEwKdsXgtDvK3EIh6yf1IlNJB2plo0/Wu5xh+MET6630CdLwACWO/OmwYBZ5bFH1Hb/sh4puUd/G39dfR+IJcL9/NjUaUcRmS+Iwpn7FLMHyDegPW/K3gZLDsdq8ZJKu6ce5iZCcNiOGLbSYksffCzwa457FpA9Y5xhDiarLY+SFDuUlcVd6KZd06yg1ZAUjieQiRZCjiVsN1RQZgQz0pqug22dsRrJyH53vVFsbrtdGkd7OMQJMR3NvPn20T70dybSGXmpd1S38od4ImRjTDIjl6AhxMClfxXtbYwMssAz+stV86XFCp/fTS+UmzQ/NkNx5+tmimhIYdwIDAQAB";

    // 服务器异步通知页面路径
    public static  String notify_url = "http://www.baidu.com";

    // 页面跳转同步通知页面路径，回调地址
    public static  String return_url = "http://www.baidu.com";

    // 字符编码格式
    public static  String charset = "UTF-8";

    // 数据格式
    public static  String format = "json";

    // 签名方式
    public static  String sign_type = "RSA2";

    // 支付宝网关
    public static String gateway = "https://openapi.alipaydev.com/gateway.do";
}
