package com.example.todomono.security;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    private final CustomerDaoInterface customerDao;

    @Autowired
    private final RoleDaoInterface roleDao;

    public WebSecurityConfig(MyUserDetailsService myUserDetailsService, CustomerDaoInterface customerDao, RoleDaoInterface roleDao) {
        this.myUserDetailsService = myUserDetailsService;
        this.customerDao = customerDao;
        this.roleDao = roleDao;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String userPassword = passwordEncoder().encode("greg");
        String adminPassword = passwordEncoder().encode("admin");

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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home", "/log-in", "/sign-up").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/log-in")
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
