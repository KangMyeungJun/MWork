package com.mwork.main.home.controller;

import com.mwork.main.entity.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;

@Controller
@SessionAttributes("state")
@Slf4j
public class MainController {

    @GetMapping("/")
    public String main(Model model,@AuthenticationPrincipal Member member) throws UnsupportedEncodingException {
        putNaverApiURL(model);
        model.addAttribute("member",member);

        return "index";
    }


    private void putNaverApiURL(Model model) throws UnsupportedEncodingException {
        String clientId = "CSswJoxefQi3YU_diksH";
        String redirectURI = URLEncoder.encode("http://3.35.254.174/login/oauth2/code/naver","UTF-8");
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130,random).toString();
        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += "&client_id=" + clientId;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&state=" + state;

        model.addAttribute("state",state);
        model.addAttribute("naverApiUrl",apiURL);
    }
}
