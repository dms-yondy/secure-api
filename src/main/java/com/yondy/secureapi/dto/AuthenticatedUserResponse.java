package com.yondy.secureapi.dto;


import java.util.Collection;

public record AuthenticatedUserResponse(String message, String name, Collection<String> grantedAuthorities) {}
