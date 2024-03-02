package com.yondy.secureapi.dto;

import java.util.Collection;

public record AuthenticatedVIDUserResponse(String message, String vid, String name,
                                           Collection<String> grantedAuthorities) {
}
