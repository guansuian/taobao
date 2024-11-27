package org.example.taobao.controller;
import jakarta.validation.constraints.Pattern;
import org.example.taobao.dto.AddressDto;
import org.example.taobao.dto.UserDto;
import org.example.taobao.dto.UserInformationChangeDto;
import org.example.taobao.dto.UserLoginDto;
import org.example.taobao.pojo.User;
import org.example.taobao.service.UserService;
import org.example.taobao.utils.AliOssUtil;
import org.example.taobao.vo.AddressVo;
import org.example.taobao.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.example.taobao.utils.JWTUtil.createLongToken;
import static org.example.taobao.utils.JWTUtil.createShortToken;
import static org.example.taobao.utils.MD5.encrypt;

/**
 * @author 关岁安
 * 控制层处理用户注册请求
 */
@RestController
@RequestMapping("/user")
public class UserController {


    //在controller里面在使用Autowired注解的时候一般是使用一个接口里面方法
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody UserDto request) {
       User user = userService.findByName(request.getUsername());
       if(user == null){
           //如果没有查询到 就说明可以进行插入
           userService.insertUser(request);
           return Result.success();
       }else{
            return  Result.error("客户 你已经注册过了");
       }
    }

    @PostMapping("/gainCaptcha")
    public Result gainCaptcha(@RequestBody Map<String, String> emailMap) {
        String email = emailMap.get("email");
        return userService.gainCaptcha(email);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDto userLoginDto){
        User user = userService.findByName(userLoginDto.getUsername());
        if(user == null){
            return Result.error("用户名不存在");
        }
        String password = encrypt(userLoginDto.getPassword());
        if(password.equals(user.getPassword())){
            String shortToken = createShortToken(user.getId(),userLoginDto.getUsername());
            String longToken = createLongToken(user.getId(),userLoginDto.getUsername());
            Map<String, String> doubleToken = new HashMap<>();
            doubleToken.put("shortToken",shortToken);
            doubleToken.put("longToken",longToken);
            doubleToken.put("userId", String.valueOf(user.getId()));
            return Result.success(doubleToken);
        }else{
            return Result.error("密码错误");
        }
    }

    @PostMapping(value = "gainUserInformation")
    public Result gainUserInformation(@RequestBody String id) {
        // 输出接收到的 id
        System.out.println(id);
        // 将字符串转换为 Integer
        Integer userId = Integer.parseInt(id);
        return userService.gainUserInformation(userId);
    }


    @PostMapping("test")
    public void test(@RequestBody String test){
        System.out.println(test);
    }

    @PostMapping("changeUserInformation")
    public Result changeUserInformation(@RequestBody UserInformationChangeDto userInformationChangeDto){
        return userService.changeUserInformation(userInformationChangeDto);
    }


    @PostMapping("changerHeader")
    public Result<String> changeHeader(MultipartFile file,String userId) throws Exception {
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        String url = AliOssUtil.upLoadFile(fileName,file.getInputStream());
        Integer id = Integer.parseInt(userId);
        userService.changeUserHead(url,id);
        System.out.println(id);
        return Result.success(url);
    }

    @PostMapping("insertNewAddress")
    public Result insertNewAddress(@RequestBody AddressDto addressDto){
        userService.insertNewAddress(addressDto);
        return Result.success("成功添加新的地址");
    }

    @GetMapping("gainUserAddress")
    private Result gainUserAddress(Integer userId){
        List<AddressVo> list = userService.gainAddressList(userId);
        System.out.println(list);
        return Result.success(list);
    }

    @GetMapping("deleteUserAddress")
    private Result deleteUserAddress(Integer userId){
        userService.deleteUserAddress(userId);
        return Result.success("删除成功");
    }
}
