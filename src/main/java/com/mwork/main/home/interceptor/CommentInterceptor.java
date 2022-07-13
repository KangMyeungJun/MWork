package com.mwork.main.home.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CommentInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("CommentInterceptor!");

        String accessToken = response.getHeader("Name");
        if (accessToken == null) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
