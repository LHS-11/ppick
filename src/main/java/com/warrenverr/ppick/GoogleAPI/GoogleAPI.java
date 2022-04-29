package com.warrenverr.ppick.GoogleAPI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GoogleAPI {

    final static String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    final static String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
    final static String GOOGLE_REVOKE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/revoke";

    @Value("${api.client_id")
    String clientId;
    @Value("${api.client_secret")
    String clientSecret;

    @GetMapping("google/auth")
    public HashMap<String, Object> gooleAuth(Model model, @RequestParam(value="code") String authCode) throws JsonProcessingException {
        //Http Request를 위한 RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        //Google OAuth Access Token 요청을 위한 파라미터 세팅
        GoogleOAuthRequest googleOAuthRequestParam = GoogleOAuthRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(authCode)
                .redirectUri("http://localhost:8080/user/login")
                .grantType("authorization_code").build();

        //JSON 파싱을 위한 기본값 세팅
        //요청시 파라미터는 스네이크 케이스로 세팅되므로 Object mapper에 미리 설정해준다.
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //AccessToken 발급 요청
        ResponseEntity<String> resultEntity = restTemplate.postForEntity(GOOGLE_TOKEN_BASE_URL, googleOAuthRequestParam, String.class);

        //Token Request
        GoogleOAuthResponse result = mapper.readValue(resultEntity.getBody(), new TypeReference<GoogleOAuthResponse>() {
        });

        System.out.println(resultEntity.getBody());

        //ID Token만 추출 (사용자 정보는 jwt로 인코딩 되어있음.)
        String jwtToken = result.getIdToken();
        String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
                .queryParam("id_token", jwtToken).encode().toUriString();
        String resultJson = restTemplate.getForObject(requestUrl, String.class);

        HashMap<String, Object> userInfo = mapper.readValue(resultJson, new TypeReference<HashMap<String, Object>>(){});
        model.addAllAttributes(userInfo);
        model.addAttribute("token", result.getAccessToken());
        System.out.println("Google user_id : " + userInfo.get("user_id"));
        System.out.println("Google user_email : " + userInfo.get("email"));

        return userInfo;
    }

    @GetMapping("google/revoke/token")
    @ResponseBody
    public HashMap<String, Object> revokeToken(@RequestParam(value = "token") String token) throws JsonProcessingException {

        HashMap<String, Object> result = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        final String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_REVOKE_TOKEN_BASE_URL)
                .queryParam("token", token).encode().toUriString();

        System.out.println("TOKEN ? " + token);

        String resultJson = restTemplate.postForObject(requestUrl, null, String.class);
        result.put("result", "success");
        result.put("resultJson", resultJson);

        return result;

    }


}
