package com.example.sideproject1.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Class<? extends AuthenticationException> aClass = exception.getClass();
        if(aClass.isAssignableFrom(UsernameNotFoundException.class) || aClass.isAssignableFrom(BadCredentialsException.class)) {
            response.sendRedirect(request.getContextPath() + "/login?error=fail");
        }
        else if(aClass.isAssignableFrom(LockedException.class)) {
            response.sendRedirect(request.getContextPath() + "/login?error=locked");
        }

    }
}
