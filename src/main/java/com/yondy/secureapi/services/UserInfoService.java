package com.yondy.secureapi.services;

import com.yondy.secureapi.dto.OauthUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserInfoService {

    @Autowired
    RestClient restClient;

    @Value("${userinfo-endpoint-uri}")
    private String userInfoEndpointUri;

    public OauthUserInfo getUserInfo(JwtAuthenticationToken token) {
        return restClient.get().uri(userInfoEndpointUri)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .retrieve().body(OauthUserInfo.class);
    }
}
