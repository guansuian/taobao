package org.example.taobao.controller;

import com.baidu.aip.contentcensor.AipContentCensor;
import org.example.taobao.service.AuditService;
import org.example.taobao.vo.Result;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author 关岁安
 */
@RestController
@RequestMapping("/audit")
public class AuditController {
    //设置APPID/AK/SK
    public static final String APP_ID = "115687787";
    public static final String API_KEY = "BfCCR1PpwuEORQ55VJXAxcbP";
    public static final String SECRET_KEY = "zM50FNf7RCAE6UQTBgcipqYbDQsyYuWr";

    @Autowired
    private AuditService auditService;

    @GetMapping(value = "auditText")
    public String auditText(@RequestParam String text) throws JSONException {
        System.out.println("检查一下"+text);
        // 初始化一个AipContentCensor
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);

        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        JSONObject res = client.textCensorUserDefined(text);
        System.out.println(res.toString(2));
        return res.toString(2);
    }

    @GetMapping(value = "passShopping")
    public Result passShopping(@RequestParam Integer id){
            auditService.passShopping(id);
        return Result.success("申请成功");
    }

    @GetMapping(value = "refuseShopping")
    public Result refuseShopping(@RequestParam Integer id,@RequestParam String summary){
        auditService.refuseShopping(id,summary);
        return Result.success("成功修改");
    }



}
