package com.mwork.main.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwork.main.entity.auth2.Auth2Member;
import com.mwork.main.entity.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/login/oauth2/code")
@RequiredArgsConstructor
@Slf4j
@SessionAttributes({"token","email","nick","oauth2id","accountId"})
public class MemberOauth2Controller {
    private final Oauth2Service os;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @GetMapping("/kakao")
    public String kakaoOauthRedirect(@RequestParam String code, Model model, HttpServletResponse response) throws JSONException, IOException {

        JSONObject params = os.requestParam(code);
        log.info("param = {}",params.get("access_token"));
        model.addAttribute("token",params.get("access_token"));

        Auth2Member kakaoMember = os.requestAuthorization(params);
        setMemberToModel(model, kakaoMember);
        Token token = tokenService.generateToken(kakaoMember.getId(),"USER");
        log.info("token = {}",token);

        writeTokenResponse(response,token);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String kakaoOauthLogout(@SessionAttribute String token, SessionStatus status) {
        os.requestLogout(token);
        status.setComplete();
        log.info("setComplete = {}",status.isComplete());
        return "redirect:/";
    }

    @GetMapping("/naver")
    public String naverCallback(String code,@SessionAttribute String state,Model model) throws JsonProcessingException {
        String token = os.getNaverToken(code, state);
        log.info("token = {}",token);
        model.addAttribute("token",token);
        Auth2Member naverMember = os.getNaverMember(token);

        setMemberToModel(model, naverMember);

        return "redirect:/";
    }

    private void setMemberToModel(Model model, Auth2Member auth2Member) {
        Member findBySocialId = os.findBySocialId(auth2Member.getId());

        if (findBySocialId == null) {
            os.saveMember(auth2Member);
        }

        model.addAttribute("oauth2id",auth2Member.getId());
        model.addAttribute("email", auth2Member.getEmail());
        model.addAttribute("nick", auth2Member.getName());
        model.addAttribute("accountId",findBySocialId.getId());
    }

    private void writeTokenResponse(HttpServletResponse response, Token token) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Auth", token.getToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

//        var writer = response.getWriter();
//        writer.println(objectMapper.writeValueAsString(token));
//        writer.flush();
    }

}
