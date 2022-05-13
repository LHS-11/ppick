package com.warrenverr.ppick.GitHubAPI;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class GitHubAPI {

    public String getAccessTocken(String authorize_code) {
        String reqURL = "https://github.com/login/oauth/access_token";
        String access_Token = "";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("client_id=Iv1.e91f1c5595cd0b04");
            sb.append("&client_secret=26e6cb7503ec8aa813bbf3d2013b4307c4d458f1");
            sb.append("&redirect_uri=http://localhost:8080/user/GitHub_login");
            sb.append("&code=" + authorize_code);
            sb.append("&state=state");
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while((line = br.readLine()) != null) {
                result += line;
            }

            System.out.println("response body : " + result);

            //Gson 라이브러르에 포함된 클래스로 json파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();

            System.out.println("access_token : " + access_Token);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public HashMap<String, Object> getUserInfo(String access_Token) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://api.github.com/user";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            String sns_id = element.getAsJsonObject().get("id").getAsString();
            String nickname = element.getAsJsonObject().get("login").getAsString();
            String email = null;
            try {
                 email = element.getAsJsonObject().get("email").getAsString();
            }catch (UnsupportedOperationException e) {
                email = "";
            }

            System.out.println("sns_id : " + sns_id);
            System.out.println("nickname : " + nickname);
            System.out.println("email : " + email);

            userInfo.put("sns_id", sns_id);
            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }

}
