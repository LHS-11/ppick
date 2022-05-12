package com.warrenverr.ppick.GoogleAPI;

import lombok.Data;

@Data
public class GoogleOAuthResponse {
    private String accessToken;
    private String expirsln;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private String idToken;
}
