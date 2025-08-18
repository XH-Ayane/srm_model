package com.srm.srm_model.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class MyBatisConfig {

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        // 分页合理化
        properties.setProperty("reasonable", "true");
        // 支持通过Mapper接口参数来传递分页参数
        properties.setProperty("supportMethodsArguments", "true");
        // 为参数设置默认值
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
