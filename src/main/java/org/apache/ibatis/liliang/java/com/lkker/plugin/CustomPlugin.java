package org.apache.ibatis.liliang.java.com.lkker.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * @Author liliang
 * @Date 2021/1/16 18:09
 * @Description:自定义插件
 **/
@Intercepts(value = {@Signature(type = Executor.class,method = "query",args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class CustomPlugin implements Interceptor {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 这里是每次执行操作的时候，都会进行这个拦截器的方法内
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameterObject = null;
        if (invocation.getArgs().length>1){
            parameterObject = invocation.getArgs()[1];
        }
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        String statementId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = getSql(boundSql, parameterObject, configuration);
        long end = System.currentTimeMillis();
        long time =  end - start;
        System.out.println("执行sql耗时:" + time + "ms" + "- id:" +statementId );
        System.out.println("- SQL:" + sql);
        return result;
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

    private String getSql(BoundSql boundSql, Object parameterObject, Configuration configuration){
        String sql = boundSql.getSql().replaceAll("[\\s]+"," ");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (parameterMappings != null){
            for (int i = 0;i<parameterMappings.size();i++){
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT){
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)){
                        value = boundSql.getAdditionalParameter(propertyName);

                    }else if (parameterObject == null){
                        value = null;
                    }else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())){
                        value = parameterObject;
                    }else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    sql = replacePlaceHolder(sql,value);
                }
            }
        }
        return sql;
    }

    private String replacePlaceHolder(String sql,Object propertyValue){
        String result = null;
        if (propertyValue != null){
            if (propertyValue instanceof String){
                result = "'" + propertyValue + "'";
            }else if (propertyValue instanceof Date){
                result = "'" + DATE_FORMAT.format(propertyValue) + "'";
            }else {
                result = propertyValue.toString();
            }

        }else {
            result = "null";
        }
        return sql.replaceFirst("\\?", Matcher.quoteReplacement(result));
    }
}
