package org.example.taobao.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author 关岁安
 */
public class AliOssUtil {

    private static final String endpoint = "https://oss-cn-beijing.aliyuncs.com";

    private static final String ACCESS_KEY_ID = "LTAI5tEqDLTZk2E4MFeeRwRt";


    private static final String ACCESS_KEY_SECRET = "k89lJH9rY8KCB0G3kK5356Dh4VyKBP";

    private static final String bucketName = "ling-an-gansuian";


    public static String upLoadFile(String objectName, InputStream in) throws Exception {



        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, ACCESS_KEY_ID,ACCESS_KEY_SECRET);


        String url = "";

        try {
            // 填写字符串。
            String content = "Hello OSS，你好世界";
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName,in);
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            url = "https://" + bucketName +"." +endpoint.substring(endpoint.lastIndexOf("/") +1) + "/" + objectName;
            System.out.println("上传到云端的图片的url为："+url);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return url;

    }

}
