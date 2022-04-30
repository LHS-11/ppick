package com.warrenverr.ppick.GoogleAPI;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleOAuthRequest {
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String code;
    private String responseType;
    private String scope;
    private String accessType;
    private String grantType;
    private String state;
    private String includeFrantedScopes;
    private String loginHint;
    private String prompt;


}
