package com.mwork.main.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mwork.main.entity.auth2.Auth2Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/login/oauth2/code")
@RequiredArgsConstructor
@Slf4j
public class MemberOauth2Controller {
    private final Oauth2Service os;
    private final TokenService tokenService;


    @GetMapping("/kakao")
    public String kakaoOauthRedirect(@RequestParam String code, Model model, HttpServletResponse response) throws JSONException, IOException {

        JSONObject params = os.requestParam(code);
        log.info("param = {}",params.get("access_token"));
        model.addAttribute("token",params.get("access_token"));

        Auth2Member kakaoMember = os.requestAuthorization(params);
        setToken(response, kakaoMember);

        return "redirect:/";
    }



    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {



        Cookie tokenCookie = new Cookie("accessToken",null);
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        return "redirect:/";
    }

    @GetMapping("/naver")
    public String naverCallback(String code,String state,Model model,HttpServletResponse response) throws JsonProcessingException {
        String token = os.getNaverToken(code, state);
        log.info("token = {}",token);
        model.addAttribute("token",token);
        Auth2Member naverMember = os.getNaverMember(token);
        setToken(response,naverMember);

        return "redirect:/";
    }


    private void setToken(HttpServletResponse response, Auth2Member kakaoMember) {
        Token token = tokenService.generateToken(kakaoMember.getId(),"USER");
        log.info("token = {}",token);
        Cookie cookie = new Cookie("accessToken",token.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
