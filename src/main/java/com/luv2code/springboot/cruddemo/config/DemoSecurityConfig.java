package com.luv2code.springboot.cruddemo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

    // add support for JDBC  no more hardcoded users

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager userDetailsManager=  new JdbcUserDetailsManager(dataSource);
    // define query to retrieve a user by username
        userDetailsManager.setUsersByUsernameQuery("select  user_id, pw, active from members where user_id=?");

   // define query to retrieve the roles by username
        userDetailsManager.setAuthoritiesByUsernameQuery("select  user_id, role from roles where user_id=?");

        return  userDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(configure->
                configure.requestMatchers(HttpMethod.GET,"/api/employees" ).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/employees/**" ).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST,"/api/employees" ).hasRole("MANAGER")
                        . requestMatchers(HttpMethod.PUT,"/api/employees" ).hasRole("MANAGER")
                        . requestMatchers(HttpMethod.DELETE,"/api/employees/**" ).hasRole("ADMIN"));

        //user http basic authentication
        httpSecurity.httpBasic(Customizer.withDefaults());

        //disable CSRF
        // in general not required for Stateless Rest APIs that uses  GET,POST,PUT and DELETE
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

 /*   @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails john= User.builder().username("john").password("{noop}testjohn").roles("EMPLOYEE").build();
        UserDetails mary= User.builder().username("mary").password("{noop}testmary").roles("EMPLOYEE","MANAGER").build();

        UserDetails susan= User.builder().username("susan").password("{noop}testsusan").roles("EMPLOYEE","MANAGER", "ADMIN").build();
        return  new InMemoryUserDetailsManager(john,mary,susan);

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(configure->
                configure.requestMatchers(HttpMethod.GET,"/api/employees" ).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/employees/**" ).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST,"/api/employees" ).hasRole("MANAGER")
                        . requestMatchers(HttpMethod.PUT,"/api/employees" ).hasRole("MANAGER")
                        . requestMatchers(HttpMethod.DELETE,"/api/employees/**" ).hasRole("ADMIN"));

        //user http basic authentication
        httpSecurity.httpBasic(Customizer.withDefaults());

        //disable CSRF
        // in general not required for Stateless Rest APIs that uses  GET,POST,PUT and DELETE
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
    */
}

