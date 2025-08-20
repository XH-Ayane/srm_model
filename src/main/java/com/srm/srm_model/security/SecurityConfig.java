package com.srm.srm_model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.srm.srm_model.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**", "/doc.html", "/webjars/**", "/v2/api-docs/**").permitAll()
                // 供应商管理接口权限
                .antMatchers("/supplier/**").hasAnyRole("ADMIN", "SUPPLIER")
                // 招标管理接口权限
                .antMatchers("/tender/**").hasAnyRole("ADMIN", "BUYER")
                // 采购需求管理接口权限
                .antMatchers("/requirement/**").hasAnyRole("ADMIN", "BUYER")
                //api接口文档访问权限
                .antMatchers("/api/auth/**", "/doc.html", "/webjars/**", "/v2/api-docs/**").permitAll()
                //访问文档接口：http://localhost:8080/doc.html
                //原生直接访问：http://localhost:8080/swagger-ui.html

                // 其他接口需要认证
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}