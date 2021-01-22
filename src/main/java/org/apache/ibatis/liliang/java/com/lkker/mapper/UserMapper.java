package org.apache.ibatis.liliang.java.com.lkker.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.liliang.java.com.lkker.entity.User;

import java.util.List;

/**
 * @Author liliang
 * @Date 2019/11/22 10:51
 * @Description
 **/
public interface UserMapper {

    User selectUserById(int id);

    @Select("SELECT * FROM user WHERE ${column} = #{value}")
    List<User> selectUserByName(@Param("column") String column,@Param("value") String value);

}
