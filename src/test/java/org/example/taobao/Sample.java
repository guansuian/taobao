package org.example.taobao;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.json.JSONException;
import org.json.JSONObject;

public class Sample {
    //设置APPID/AK/SK
    public static final String APP_ID = "115687787";
    public static final String API_KEY = "BfCCR1PpwuEORQ55VJXAxcbP";
    public static final String SECRET_KEY = "zM50FNf7RCAE6UQTBgcipqYbDQsyYuWr";

    public static void main(String[] args) throws JSONException {
        // 初始化一个AipContentCensor
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
//        String path = "https://ling-an-gansuian.oss-cn-beijing.aliyuncs.com/f5463ac8-8547-43ae-96c5-9822e325083b.png";
//       JSONObject res = client.imageCensorUserDefined(path, EImgType.URL, null);
//        System.out.println(res.toString());
        JSONObject res = client.textCensorUserDefined("我很想你 骚逼");
        System.out.println(res.toString(2));

        
    }


}



//public void sample(AipContentCensor client) {
//    // 参数为本地图片路径
//    String path = "test.jpg";
//    JSONObject response = client.imageCensorUserDefined(path, EImgType.FILE, null);
//    System.out.println(response.toString());
//
//    // 参数为url
//    String url = "http://testurl";
//    response = client.imageCensorUserDefined(url, EImgType.URL, null);
//    System.out.println(response.toString());
//
//    // 参数为本地图片文件二进制数组
//    byte[] file = readImageFile(imagePath);
//    response = client.imageCensorUserDefined(file, null);
//    System.out.println(response.toString());
//}

