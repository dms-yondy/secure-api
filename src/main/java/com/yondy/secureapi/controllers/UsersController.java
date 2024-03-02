package com.yondy.secureapi.controllers;

import com.yondy.secureapi.dto.AuthenticatedUserResponse;
import com.yondy.secureapi.dto.AuthenticatedVIDUserResponse;
import com.yondy.secureapi.dto.OauthUserInfo;
import com.yondy.secureapi.dto.UnauthenticatedUserResponse;

import com.yondy.secureapi.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static final String NOT_AUTHENTICATED_MESSAGE = "Looks like you're not authenticated!";
    private static final String AUTHENTICATED_MESSAGE = "Looks like you're authenticated!";
    private static final String VENDOR_GROUP_MESSAGE = "Welcome to the vendor group!";
    private static final String SPECIFIC_VENDOR_MESSAGE = "Looks like you're a specific vendor!";

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/unauthenticated")
    public ResponseEntity<UnauthenticatedUserResponse> unauthenticated() {
        UnauthenticatedUserResponse unauthenticatedUserResponse = new UnauthenticatedUserResponse(NOT_AUTHENTICATED_MESSAGE, "Anonymous");
        return ResponseEntity.ok(unauthenticatedUserResponse);
    }

    @GetMapping("/authenticated")
    public ResponseEntity<AuthenticatedUserResponse> authenticated(JwtAuthenticationToken jwtAuthenticationToken) {
        OauthUserInfo oauthUserInfo = userInfoService.getUserInfo(jwtAuthenticationToken);
        AuthenticatedUserResponse authenticatedUserResponse = new AuthenticatedUserResponse(AUTHENTICATED_MESSAGE, oauthUserInfo.fullName(), parseAuthorities(jwtAuthenticationToken));
        return ResponseEntity.ok(authenticatedUserResponse);
    }

    @GetMapping("/vendors")
    @PreAuthorize("authentication.credentials.claims['cognito:groups'].contains('VENDOR')")
    public ResponseEntity<AuthenticatedUserResponse> vendors(JwtAuthenticationToken jwtAuthenticationToken) {
        OauthUserInfo oauthUserInfo = userInfoService.getUserInfo(jwtAuthenticationToken);
        AuthenticatedUserResponse authenticatedUserResponse = new AuthenticatedUserResponse(VENDOR_GROUP_MESSAGE, oauthUserInfo.fullName(), parseAuthorities(jwtAuthenticationToken));
        return ResponseEntity.ok(authenticatedUserResponse);
    }

    @GetMapping("/vendors/{vid}")
    @PreAuthorize("authentication.credentials.claims['cognito:groups'].contains('VENDOR')")
    public ResponseEntity<AuthenticatedVIDUserResponse> vendor(JwtAuthenticationToken jwtAuthenticationToken, @PathVariable String vid){
        OauthUserInfo oauthUserInfo = userInfoService.getUserInfo(jwtAuthenticationToken);
        if(oauthUserInfo.vid().isBlank() || !oauthUserInfo.vid().equals(vid)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(new AuthenticatedVIDUserResponse(SPECIFIC_VENDOR_MESSAGE, oauthUserInfo.vid(), oauthUserInfo.fullName(), parseAuthorities(jwtAuthenticationToken)));
    }
    private static List<String> parseAuthorities(JwtAuthenticationToken jwtAuthenticationToken) {
        return jwtAuthenticationToken.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
