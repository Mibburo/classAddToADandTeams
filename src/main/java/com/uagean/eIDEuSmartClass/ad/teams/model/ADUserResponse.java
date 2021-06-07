package com.uagean.eIDEuSmartClass.ad.teams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
@ToString
@NoArgsConstructor
public class ADUserResponse {


    @JsonProperty("@odata.context")
    private String dataContext;
    @JsonProperty("@odata.type")
    private String dataType;
    @JsonProperty("businessPhones")
    private List<String> businessPhones;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("givenName")
    private String givenName;
    @JsonProperty("jobTitle")
    private String jobTitle;
    @JsonProperty("mail")
    private String mail;
    @JsonProperty("mobilePhone")
    private String mobilePhone;
    @JsonProperty("officeLocation")
    private String officeLocation;
    @JsonProperty("preferredLanguage")
    private String preferredLanguage;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("userPrincipalName")
    private String userPrincipalName;
    @JsonProperty("id")
    private String id;


}
