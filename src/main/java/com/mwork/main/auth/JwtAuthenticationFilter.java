package com.mwork.main.auth;

import com.mwork.main.entity.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final Oauth2Service oauth2Service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        Cookie cookie;
        String token = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("accessToken")) {
                    cookie = c;
                    token = cookie.getValue();
                }
            }
        }

        log.info(token);
        if (token != null && tokenService.verifyToken(token)) {
            log.info("TokenFilter");
            String uid = tokenService.getUid(token);
            Member findMember = oauth2Service.findBySocialId(uid);
            Authentication authentication = getAuthentication(findMember);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Name",findMember.getName());
            response.setContentType("application/json;charset=UTF-8");
        }


        filterChain.doFilter(request,response);
    }

    public Authentication getAuthentication(Member member) {
        return new UsernamePasswordAuthenticationToken(member,"", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
