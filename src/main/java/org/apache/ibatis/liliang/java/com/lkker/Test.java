package org.apache.ibatis.liliang.java.com.lkker;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.liliang.java.com.lkker.entity.User;
import org.apache.ibatis.liliang.java.com.lkker.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author liliang
 * @Date 2019/11/22 10:52
 * @Description
 **/
public class Test {

    public static void main(String[] args) throws IOException {
        String resource = "org/apache/ibatis/liliang/resources/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try{
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//            User user = mapper.selectUserById(100);
            List<User> users = mapper.selectUserByName("name", "杜德兴");
            System.out.println(users);
        }finally {
            sqlSession.close();
        }



    }
}
