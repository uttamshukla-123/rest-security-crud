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
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    @Bean
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
}
