package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

//this class will filter landing page for us landing with certain role with certain page
@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());//this will bring authority
      if(roles.contains("Admin")){
          response.sendRedirect("/user/create");
    }

      if(roles.contains("Employee")){
          response.sendRedirect("/task/employee");
      }
      if (roles.contains("Manager")){
          response.sendRedirect("/project/create");
      }
}
}
