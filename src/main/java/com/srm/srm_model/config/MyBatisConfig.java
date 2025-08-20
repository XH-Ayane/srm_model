package com.srm.srm_model.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class MyBatisConfig {

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();// 分页插件
        Properties properties = new Properties();// 分页插件属性
        // 分页合理化
        properties.setProperty("reasonable", "true");//什么情况才是合理的呢？当pageNum<1时，会查询第一页数据；当pageNum>总页数时，会查询最后一页数据

        // 支持通过Mapper接口参数来传递分页参数
        properties.setProperty("supportMethodsArguments", "true");
        // 为参数设置默认值
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
