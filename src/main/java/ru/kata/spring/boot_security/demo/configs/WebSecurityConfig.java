package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    private final UserDetailsService userService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailsService userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    //Настройка авторизации
                .authorizeRequests()
                    .antMatchers("/", "/index").permitAll()  //Доступ для всех пользователей
                    .antMatchers("/admin/**").hasRole("ADMIN")  //Доступ для администратора
                    .antMatchers("/user/**").hasAnyRole("ADMIN", "USER") //Доступ для администратора и юзера
                    .anyRequest().authenticated()   //Все остальные страницы требуют аутентификации
                    // Завершение авторизации
                .and()
                //Настройка для входа в систему пользователям(авторизация)
                    .formLogin().successHandler(successUserHandler)
                    .permitAll()
                .and()
                //Выход из аккаунта
                    .logout().logoutSuccessUrl("/login")
                    .permitAll();
    }


    //Создание таблиц на основе Entities
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}