package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.taobao.vo.AddressVo;

import java.util.List;

/**
 * @author 关岁安
 */

@Mapper
public interface UserAddressMapper {

    @Insert("insert into user_address (user_id, address) values ( #{userId}, #{address})")
    void insertNewAddress(String address,Integer userId);

    @Select("select id as addressId ,address from user_address where user_id = #{userId}")
    List<AddressVo> selectUserAddressList(Integer userId);

    @Delete("DELETE FROM user_address  WHERE user_id = #{userId}")
    void deleteUserAddress(Integer userId);
}
