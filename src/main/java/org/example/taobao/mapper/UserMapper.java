package org.example.taobao.mapper;

import org.apache.ibatis.annotations.*;
import org.example.taobao.pojo.User;
import org.example.taobao.vo.UserVo;

/**
 * @author 关岁安
 */
@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User findByName(String username);

    @Insert("INSERT INTO user (password, email, username) VALUES (#{password}, #{email}, #{username})")
    void registerUser(User user);

    @Select("select id, username, email, head, gender, sign " +
            "from user where id = #{id}")
    UserVo gainUserInformation(Integer id);


    @Update("update user set sign = #{sign} ,email = #{email}, gender = #{gender}" +
            "where id = #{id}")
    void changeUSerInformation(Integer id,String sign ,String email,String gender);

    @Update("update user set head = #{head}" +
            "where id = #{id}")
    void changeHead(Integer id,String head);




}
