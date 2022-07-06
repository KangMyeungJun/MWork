package com.mwork.main.home.interceptor;

import com.mwork.main.entity.member.Member;
import com.mwork.main.home.repository.MemberRepository;
import com.mwork.main.home.service.MemberService;
import com.mwork.main.home.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
public class CommentAccountInterceptor implements HandlerInterceptor {

    private final PostService postService;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Long accountId = (Long) session.getAttribute("accountId");
        Optional<Member> optionalMember = memberRepository.findById(accountId);
        if (optionalMember.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Member findMember = optionalMember.get();


        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
