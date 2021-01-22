package org.apache.ibatis.liliang.java.com.lkker.mapper;

import org.apache.ibatis.liliang.java.com.lkker.entity.User;

import java.util.List;

/**
 * @Author liliang
 * @Date 2019/11/22 10:51
 * @Description
 **/
public interface UserMapper {

    User selectUserById(int id);

    List<User> selectUserByName(String userName);

}
