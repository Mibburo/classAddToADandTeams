package com.uagean.eIDEuSmartClass.ad.teams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ADTokenResponse {
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")

    private String expiresIn;
    @JsonProperty("ext_expires_in")

    private String exExpiresIn;
    @JsonProperty("expires_on")

    private String expiresOn;
    @JsonProperty("not_before")

    private String notBefore;
    private String resource;
    @JsonProperty("access_token")
    private String accessToken;


}
