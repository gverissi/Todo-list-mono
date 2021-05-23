package com.example.todomono.security;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile({"prod"})
public class WebSecurityConfigProd extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;
    private final RoleDaoInterface roleDao;

    @Autowired
    public WebSecurityConfigProd(MyUserDetailsService myUserDetailsService, RoleDaoInterface roleDao) {
        this.myUserDetailsService = myUserDetailsService;
        this.roleDao = roleDao;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        Role admin = roleDao.findByRoleName("ADMIN");
        if (admin == null) {
            Role roleAdmin = new Role("ADMIN");
            roleDao.save(roleAdmin);
        }
        Role user = roleDao.findByRoleName("USER");
        if (user == null) {
            Role roleUser = new Role("USER");
            roleDao.save(roleUser);
        }
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/home/log-in")
                .permitAll()
                .defaultSuccessUrl("/home?login", true)
                .and()
                .logout()
                .logoutSuccessUrl("/home?logout");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
