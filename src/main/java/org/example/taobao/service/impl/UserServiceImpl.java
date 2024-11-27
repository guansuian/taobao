package org.example.taobao.service.impl;
import org.example.taobao.dto.AddressDto;
import org.example.taobao.dto.UserDto;
import org.example.taobao.dto.UserInformationChangeDto;
import org.example.taobao.mapper.UserAddressMapper;
import org.example.taobao.mapper.UserMapper;
import org.example.taobao.pojo.User;
import org.example.taobao.service.UserService;
import org.example.taobao.vo.AddressVo;
import org.example.taobao.vo.Result;
import org.example.taobao.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;

import java.util.List;

import static org.example.taobao.utils.MD5.encrypt;
import static org.example.taobao.utils.MailUtils.sendEmail;

/**
 * @author 关岁安
 */

//这个注解是将这个类放在ioc容器里面进行管理
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public void registerUser(User user) {

    }

    @Override
    public User findByName(String username) {
        return userMapper.findByName(username);
    }

    @Override
    public void insertUser(UserDto request) {
        String password = request.getPassword();
        password = encrypt(password);
        User user = new User(request.getUsername(),request.getEmail(),password);
        userMapper.registerUser(user);
    }

    @Override
    public Result gainCaptcha(@RequestBody String email) {
        try {
            sendEmail(email);
            return Result.success("发送验证码成功");
        } catch (MessagingException e) {
            return Result.error("发送验证码失败请稍后失败");
        }
    }

    @Override
    public Result gainUserInformation(Integer id) {
       UserVo userInformation = userMapper.gainUserInformation(id);
       if(userInformation == null){
           return Result.error("获取用户信息失败");
       }else{
           return Result.success(userInformation);
       }
    }

    @Override
    public Result changeUserInformation(UserInformationChangeDto userInformationChangeDto) {
        Integer id  = userInformationChangeDto.getId();

        String sign = userInformationChangeDto.getSign();
        String email = userInformationChangeDto.getEmail();
        String gender = userInformationChangeDto.getGender();


        try {
            userMapper.changeUSerInformation(id,sign,email,gender);
            return Result.success("修改成功了");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("修改失败");
        }
    }

    @Override
    public void changeUserHead(String head,Integer id) {
        userMapper.changeHead(id,head);
    }

    @Override
    public void insertNewAddress(AddressDto addressDto) {
        Integer userId = addressDto.getUserId();
        String address = addressDto.getAddress();
        userAddressMapper.insertNewAddress(address,userId);
    }

    @Override
    public List<AddressVo> gainAddressList(Integer userId) {
        return userAddressMapper.selectUserAddressList(userId);
    }

    @Override
    public void deleteUserAddress(Integer userId) {
        userAddressMapper.deleteUserAddress(userId);
    }


}
