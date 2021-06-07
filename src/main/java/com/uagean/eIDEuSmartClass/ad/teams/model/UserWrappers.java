/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uagean.eIDEuSmartClass.ad.teams.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.keycloak.representations.IDToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

/**
 *
 * @author nikos
 */
public class UserWrappers {

    private static Logger log = LoggerFactory.getLogger(UserWrappers.class);

    private static String IS_LATIN = "[\\p{Punct}\\p{Space}\\p{IsLatin}]+$";
    // "[\\p{L}\\p{M}&&[^\\p{Alpha}]]+";

    public static TokenUserDetails wrapEidasToTokenUser(FormUser usr, String token, PreAuthenticatedAuthenticationToken authentication) throws IOException {
        return new TokenUserDetails(usr.getEid(), usr.getProfileName(), (String) authentication.getCredentials(),
                token, usr.getEid(), usr.getProfileName(), true, authentication.getAuthorities());

    }

    public static FormUser wrapDecodedJwtEidasUser(String jwt) throws IOException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        FormUser user = mapper.readValue(jwt, FormUser.class);
        user.setEngName(user.getCurrentGivenName());
        user.setEngSurname(user.getCurrentFamilyName());
        if (!(user.getCurrentFamilyName().matches(IS_LATIN) && user.getCurrentFamilyName().matches(IS_LATIN))) {
            //non latin found in namae or surname
            String engGiveName = user.getCurrentGivenName().split(",").length > 1 ? user.getCurrentGivenName().split(",")[1].trim() : null;
            String engFamilyName = user.getCurrentFamilyName().split(",").length > 1 ? user.getCurrentFamilyName().split(",")[1].trim() : null;

            String natGiveName = user.getCurrentGivenName().split(",").length > 1 ? user.getCurrentGivenName().split(",")[0].trim() : user.getCurrentGivenName();
            String natFamilyName = user.getCurrentFamilyName().split(",").length > 1 ? user.getCurrentFamilyName().split(",")[0].trim() : user.getCurrentFamilyName();
            user.setEngSurname(engFamilyName);
            user.setEngName(engGiveName);
            user.setCurrentGivenName(natGiveName);
            user.setCurrentFamilyName(natFamilyName);
        }
        return user;
    }

    public static FormUser wrapIDTokenToEidasUser(IDToken idToken, String eId) throws IOException {
        FormUser user = new FormUser();
        user.setCurrentFamilyName(idToken.getFamilyName());
        user.setCurrentGivenName(idToken.getName());
        user.setDateOfBirth((String) idToken.getOtherClaims().get("date_of_birth"));
        user.setEngName(idToken.getName());
        user.setEngSurname(idToken.getFamilyName());
        user.setAdEmail(idToken.getEmail());

//        user.setEid(idToken.getPreferredUsername());
//        user.setEid((String) idToken.getOtherClaims().get("person_identifier"));
        user.setEid(eId);
        String eidValue = StringUtils.isEmpty((String) idToken.getOtherClaims().get("person_identifier"))?
                (String) idToken.getOtherClaims().get("eidas-personIdentifier"):(String) idToken.getOtherClaims().get("person_identifier");
        user.setPersonIdentifier(eidValue);
//        user.setPersonIdentifier((String) idToken.getOtherClaims().get("person_identifier"));
//        user.setPersonIdentifier((String) idToken.getOtherClaims().get("eidas-personIdentifier"));

        if (user.getEid() == null) {
            String hash = DigestUtils.sha256Hex(idToken.getFamilyName() + idToken.getGivenName());
            user.setEid(user.getCurrentFamilyName() + hash.substring(0, 4));
            user.setPersonIdentifier(user.getCurrentFamilyName() + hash.substring(0, 4));
        }

        if (!(user.getCurrentFamilyName().matches(IS_LATIN) && user.getCurrentFamilyName().matches(IS_LATIN))) {
            //non latin found in namae or surname
            String engGiveName = user.getCurrentGivenName().split(" ").length > 1 ? user.getCurrentGivenName().split(" ")[1].trim() : null;
            String engFamilyName = user.getCurrentFamilyName().split(" ").length > 1 ? user.getCurrentFamilyName().split(" ")[1].trim() : null;

            String natGiveName = user.getCurrentGivenName().split(" ").length > 1 ? user.getCurrentGivenName().split(" ")[0].trim() : user.getCurrentGivenName();
            String natFamilyName = user.getCurrentFamilyName().split(" ").length > 1 ? user.getCurrentFamilyName().split(" ")[0].trim() : user.getCurrentFamilyName();
            user.setEngSurname(engFamilyName);
            user.setEngName(engGiveName);
            user.setCurrentGivenName(natGiveName);
            user.setCurrentFamilyName(natFamilyName);
        }

        user.setProfileName(idToken.getGivenName());

        return user;
    }
}
