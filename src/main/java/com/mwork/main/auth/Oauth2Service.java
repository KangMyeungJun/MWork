package com.mwork.main.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwork.main.home.repository.MemberRepository;
import com.mwork.main.entity.auth2.Auth2Member;
import com.mwork.main.entity.member.Member;
import com.mwork.main.entity.member.Role;
import com.mwork.main.entity.member.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class Oauth2Service {

    private final MemberRepository memberRepository;

    public JSONObject requestParam(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "92d5910837e26848e623401f09c24031");
        params.add("redirect_uri", "http://localhost/login/oauth2/code/kakao");
        params.add("code", code);
        params.add("client_secret", "JQIT8G3DarOMVSqziD8wI9okPggpNji7");

        HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoRequest,
                String.class
        );
        JSONObject obj;
        try {
            obj = new JSONObject(response.getBody());


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return obj;
    }

    public Auth2Member requestAuthorization(JSONObject params) throws JSONException, IOException {
        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        rt.setErrorHandler(new OauthRestTemplateErrorHandler());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+params.getString("access_token"));
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<HttpHeaders> kakaoRequest = new HttpEntity<>(headers);
        log.info("authorization = {}",kakaoRequest.getHeaders());
        ResponseEntity<String> profileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoRequest,
                String.class
        );
        log.info("profile = {}",profileResponse.getBody().toString());
        String body = profileResponse.getBody();
        Map<String, Object> map = JsonParserFactory.getJsonParser().parseMap(body);
        String kakao_account = map.get("kakao_account").toString();

        JSONObject obj = new JSONObject(kakao_account);
        JSONObject profile = (JSONObject) obj.get("profile");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        JsonNode kakaoAccountNode = objectMapper.readTree(obj.toString());

        String nickname = kakaoAccountNode.get("profile").get("nickname").asText();
        String email = kakaoAccountNode.get("email").asText();
        String id = objectMapper.readTree(body.toString()).get("id").asText();
        log.info("kakao_id = {}",id);

        Auth2Member km = new Auth2Member();
        km.setId(id);
        km.setName(nickname);
        km.setEmail(email);
        km.setSocialType(SocialType.KAKAO);
        return km;
    }

    public HttpEntity requestLogout(String token) {
        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        rt.setErrorHandler(new OauthRestTemplateErrorHandler());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+token);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<HttpHeaders> kakaoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> entity = rt.exchange("https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                kakaoRequest,
                String.class);

        log.info("logout = {}",entity.getBody());
        return entity;
    }

    public String getNaverToken(String code,String state) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","CSswJoxefQi3YU_diksH");
        params.add("client_secret","hId74FyTAt");
        params.add("code",code);
        params.add("state",state);

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,headers);
        ResponseEntity<String> exchange = rt.exchange("https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                entity,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(exchange.getBody().toString());
        String token = node.get("access_token").asText();

        return token;
    }

    public Auth2Member getNaverMember(String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + token);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> memberEntity = rt.exchange("https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                entity,
                String.class);
        log.info("memberEntity = {}",memberEntity.getBody().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseNode = objectMapper.readTree(memberEntity.getBody()).get("response");
        String id = responseNode.get("id").asText();
        String name = responseNode.get("name").asText();
        String email = responseNode.get("email").asText();

        Auth2Member nm = new Auth2Member();
        nm.setId(id);
        nm.setName(name);
        nm.setEmail(email);
        nm.setSocialType(SocialType.NAVER);

        return nm;
    }

    public void saveMember(Auth2Member auth2Member) {

        Member member = new Member();
        member.setEmail(auth2Member.getEmail());
        member.setRole(Role.USER);
        member.setOAuth2Id(auth2Member.getId());
        member.setSocialType(auth2Member.getSocialType());
        member.setName(auth2Member.getName());

        memberRepository.save(member);
    }

    public Member findBySocialId(String socialId) {
        return memberRepository.findByoAuth2Id(socialId);
    }






}


