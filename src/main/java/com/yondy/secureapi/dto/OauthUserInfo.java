package com.yondy.secureapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record OauthUserInfo(@JsonProperty("given_name") String givenName,
                            @JsonProperty("family_name") String familyName, @JsonProperty("custom:VID") String vid) {

    public String fullName() {
        return givenName + " " + familyName;
    }

    @Override
    public String toString() {
        return "OauthUserInfo[" +
                "givenName=" + givenName + ", " +
                "familyName=" + familyName + ", " +
                "vid=" + vid + ']';
    }
}
