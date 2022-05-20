package com.warrenverr.ppick.GoogleAPI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GoogleAPI {

    final static String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    final static String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
    final static String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v1/tokeninfo";
    final static String GOOGLE_REVOKE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/revoke";


    String clientId = "151434471836-i4vcjfpu0702hmj7bc8hi9tasvufflcl.apps.googleusercontent.com";
    String clientSecret = "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";

    public String getAccessToken(String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";

        try {
            URL url = new URL(GOOGLE_TOKEN_BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("client_id=" + clientId);
            sb.append("&client_secret=" + clientSecret);
            sb.append("&code=" + authorize_code);
            sb.append("&grant_type=authorization_code");
            sb.append("&redirect_uri=http://localhost:8080/user/auth/Google_login");
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null)
                result += line;

            System.out.println("response body : " + result);
            //Gson 라이브러르에 포함된 클래스로 json파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("id_token").getAsString();

            System.out.println("id_token : " + access_Token);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    public HashMap<String, Object> getUserInfo(String id_token) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String url = GOOGLE_TOKEN_INFO_URL + "?id_token="+id_token;
        try {
            HttpURLConnection con;
            URL googleURL = new URL(url);
            con = (HttpURLConnection) googleURL.openConnection();

            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader in = new BufferedReader(isr);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(in);

            String sns_id = element.getAsJsonObject().get("user_id").getAsString();
            String email = element.getAsJsonObject().get("email").getAsString();
            System.out.println("sns_id = " + sns_id);
            System.out.println("email = " + email);
            userInfo.put("sns_id", sns_id);
            userInfo.put("email", email);

        }catch(Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

}
