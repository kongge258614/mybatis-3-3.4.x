package org.apache.ibatis.liliang.java.com.lkker.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @Author liliang
 * @Date 2021/1/16 18:09
 * @Description:自定义插件
 **/
@Intercepts(value = {@Signature(type = Executor.class,method = "query",args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class CustomPlugin implements Interceptor {

    // 这里是每次执行操作的时候，都会进行这个拦截器的方法内
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        System.out.println("用户自定义插件....................");
        String name = invocation.getMethod().getName();
        System.out.println("name:"+name);
        return invocation.proceed();
    }

    // 主要是为了把这个拦截器生成一个代理放到拦截器链中
    @Override
    public Object plugin(Object target) {
        //官方推荐写法
        return Plugin.wrap(target,this);
    }

    // 插件初始化的时候调用，也只调用一次，插件配置的属性从这里设置进来
    @Override
    public void setProperties(Properties properties) {

    }
}
