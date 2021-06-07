package com.uagean.eIDEuSmartClass.ad.teams.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FormUser {

    //{"firstName":"ΑΝΔΡΕΑΣ, ANDREAS","eid":"GR/GR/ERMIS-11076669","familyName":"ΠΕΤΡΟΥ, PETROU","personIdentifier":"GR/GR/ERMIS-11076669","dateOfBirth":"1980-01-01"}
    private String profileName;
    @NotNull
    private String eid;
    @NotNull
    @JsonAlias({"currentGivenName", "firstName"})
    private String currentGivenName;
    @NotNull
    @JsonAlias({"currentFamilyName", "familyName"})
    private String currentFamilyName;
    private String personIdentifier;
    private String dateOfBirth;
    private String gender;

    /**
     * new columns!!
     */
    @NotNull
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email address is invalid")
    private String email;

    private String adEmail;

    @Size(min=1,message="A mobile number is required")
    private String mobile;
    @Size(min=1,message="Your affiliation is required")
    private String affiliation;
    @Size(min=1,message="Please select a country")
    private String country;
    private String engName;
    private String engSurname;
}
