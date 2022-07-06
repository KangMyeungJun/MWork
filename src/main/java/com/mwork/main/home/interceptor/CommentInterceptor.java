package com.mwork.main.home.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class CommentInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("CommentInterceptor!");

        HttpSession session = request.getSession();
        Long accountId = (Long) session.getAttribute("accountId");
        if (accountId == null) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
