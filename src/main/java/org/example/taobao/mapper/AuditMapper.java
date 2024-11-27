package org.example.taobao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author 关岁安
 */
@Mapper
public interface AuditMapper {

    @Update("update shop set pass = 'yes' where id = #{id}")
    void passShopping(Integer id);

    @Update("update shop set pass = 'refuse' ,summary = #{summary} where id = #{id}")
    void refuseShopping(Integer id,String summary);
}
