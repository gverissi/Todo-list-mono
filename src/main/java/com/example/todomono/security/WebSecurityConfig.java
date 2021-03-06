package com.example.todomono.security;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Customer;
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
@Profile({"dev", "memory"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;
    private final CustomerDaoInterface customerDao;
    private final RoleDaoInterface roleDao;

    @Autowired
    public WebSecurityConfig(MyUserDetailsService myUserDetailsService, CustomerDaoInterface customerDao, RoleDaoInterface roleDao) {
        this.myUserDetailsService = myUserDetailsService;
        this.customerDao = customerDao;
        this.roleDao = roleDao;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String userPassword = passwordEncoder().encode("gg");
        String adminPassword = passwordEncoder().encode("gg");

        Role roleUser = new Role("USER");
        roleDao.save(roleUser);

        Role roleAdmin = new Role("ADMIN");
        roleDao.save(roleAdmin);

        Customer user = new Customer("greg", userPassword);
        user.setEnabled(true);
        user.addRole(roleUser);
        customerDao.save(user);

        Customer admin = new Customer("admin", adminPassword);
        admin.addRole(roleUser);
        admin.addRole(roleAdmin);
        customerDao.save(admin);

        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
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

//        http.csrf().disable().authorizeRequests()
//                .anyRequest().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
