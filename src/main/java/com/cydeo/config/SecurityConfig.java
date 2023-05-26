package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // I want to use something  from Spring , I need some class, interface so build this classsc-1067
public class SecurityConfig {

    private final SecurityService securityService;
    private final  AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }


//    @Bean // what spring provides to me?
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // I am creating my user usning Spring classes
        // UserDetails user = new User()  for one user, but need more than one user, this is how we create users MANUALLY
//        List<UserDetails> userList = new ArrayList<>();
//        userList.add(
//                new User("mike", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
//        userList.add(
//                new User("ozzy", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));
//
//        return new InMemoryUserDetailsManager(userList);
//    }

// careful User class NOT ENTITY this User class created by spring
// main UserDetailsService this bussiness logic provide by public interface UserDetailsService
// and return UserDetails
//what is the mekanism bring the user into the UI , it is loadUserByUsername interfaace under the UserDetailService interdace class
// Spring created UserDetailService interface class and execude loadUserByName interface which is pasing the object into the UI
// got to UserDetailService interface and click the UserDetails class
//ROLE_ADMIN is name convention by spring

    //filltering
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       // use this credentials --> admin@admin.com   password ->Abc1
        return http    // gives authorizing
                .authorizeRequests()  // related pages
                //.antMatchers("/user/**").hasRole("Admin")// /user/** every method under only access by AADMIN,--> underscore koymuyor
                .antMatchers("/user/**").hasAuthority("Admin")//  automaticly olarak underscore koyuyor for db ROLE_ADMIN
                                                // ADMIN is not ADMIN in db it is Admin so needs to be match.
                .antMatchers("/project/**").hasAuthority("Manager")// /user/** every method under only access by AADMIN :
                .antMatchers("/task/employee/**").hasAuthority("Employee")// /user/** every method under only access by AADMIN
                .antMatchers("/task/**").hasAuthority("Manager")// /user/** every method under only access by AADMIN
                                    //hasAnyRole
                //.antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN")
                //.antMatchers("task/**").hasAuthority("ROLE_EMPLOYEE")  //hasAuthority underscore koymali uymasi icin
                .antMatchers(  //define the pages for authhorized user
                        "/", //8080
                        "/login",  //endpoint
                        "/fragments/**",// frag package nin altindaki hersey
                        "/assets/**",  //asset package nin altinda hersey
                        "/images/**"
                ).permitAll() // everybody can access
                .anyRequest().authenticated()
                .and()
               // .httpBasic()  //not my page, it is pop up page small
                .formLogin()  // I want to use this endPoint page
                .loginPage("/login")
              //  .defaultSuccessUrl("/welcome") // landin paage, after AuthSuccessHandler class created we land on different page based on the role
                .successHandler(authSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()  //ever one can access this log in page
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                .tokenValiditySeconds(120)
                .key("cydeo")
                .userDetailsService(securityService)
                .and().build();
    }


}