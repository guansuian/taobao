package org.example.taobao.service;

import org.example.taobao.dto.AddressDto;
import org.example.taobao.dto.UserDto;
import org.example.taobao.dto.UserInformationChangeDto;
import org.example.taobao.pojo.User;
import org.example.taobao.vo.AddressVo;
import org.example.taobao.vo.Result;

import java.util.List;

/**
 * @author 关岁安
 */
public interface UserService {
    void registerUser(User user);

    User findByName(String username);

    void insertUser(UserDto request);

    Result gainCaptcha(String email);

    Result gainUserInformation(Integer id);

    Result changeUserInformation(UserInformationChangeDto userInformationChangeDto);

    void changeUserHead(String head, Integer id);

    void insertNewAddress(AddressDto addressDto);

    List<AddressVo> gainAddressList(Integer userId);

    void deleteUserAddress(Integer userId);
}