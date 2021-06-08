package com.uagean.eIDEuSmartClass.ad.teams.service.impl;

import com.azure.core.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.uagean.eIDEuSmartClass.ad.teams.model.*;
import com.uagean.eIDEuSmartClass.ad.teams.service.ActiveDirectoryService;
import com.uagean.eIDEuSmartClass.ad.teams.service.ConfigPropertiesServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

/**
 *
 * @author nikos
 */
@Service
public class ActiveDirectoryServiceImpl implements ActiveDirectoryService {

    @Value("active.dir.token.endpoint")
    private String activeDirTokenEndpoint;

    @Value("client.id")
    private String clientIdConf;

    @Value("client.secret")
    private String clientSecretConf;

    @Value("invite.redirect.url")
    private String invRedirectUrl;

    public static final String ACTIVE_DIRECTORY_TOKEN_ENDPOINT = "https://login.microsoftonline.com/d4a81dbe-9310-4d28-b060-dc9cd82a3b8b/oauth2/token";
    public static final String CLIENT_ID = "29937bdf-4ead-4618-9c79-1139e2687aae";
    public static final String CLIENT_SECRET = "..01zo91D2Hl-~EdFmtOlCAgy94~49j8U~";
    public final static String BASE_URL = StringUtils.isEmpty(System.getenv("BASE_URL"))?"http://localhost:9090/":System.getenv("BASE_URL");


    private final String USER_AGENT = "Mozilla/5.0";

    private final static Logger log = LoggerFactory.getLogger(ActiveDirectoryServiceImpl.class);

    @Autowired
    private ConfigPropertiesServices propServ;

    @Override
    public String getAccessToken() {
//        String activeDirTokenEnpoint = activeDirTokenEndpoint;
//        String clientId = clientIdConf;
//        String clientSecret = clientSecretConf;
        String defaultSKU = "94763226-9b3c-4e75-a931-5c89701abe66";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        map.add("resource", "https://graph.microsoft.com");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity( ACTIVE_DIRECTORY_TOKEN_ENDPOINT, request , String.class );

        try {
            return mapper.readValue(response.getBody(), ADTokenResponse.class).getAccessToken();
        } catch (IOException e) {
            log.error(e.getMessage());
            return  null;
        }
    }

    @Override
    public String inviteGuestUser(String userEmail) {
        String accessToken = getAccessToken();
        String invitationEndpoint = "https://graph.microsoft.com/v1.0/invitations";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+accessToken);

        ADGuestInviteRequest guestJsonObject = new ADGuestInviteRequest();
        guestJsonObject.setInviteRedirectUrl(BASE_URL);
        guestJsonObject.setInvitedUserEmailAddress(userEmail);

        HttpEntity<ADGuestInviteRequest> entity = new HttpEntity<ADGuestInviteRequest>(guestJsonObject,headers);
        String result = restTemplate.postForObject(invitationEndpoint, entity, String.class);

        return result;
    }

    @Override
    public String getGroupByName(String groupName) {

        String accessToken = getAccessToken();
        String getGroupEndpoint = "https://graph.microsoft.com/v1.0/groups?$search=\"displayName:" + groupName + "\"&$select=id,displayName";
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        restTemplate.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("ConsistencyLevel", "eventual");

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ADGetGroupResponse> resp = restTemplate.exchange(
                getGroupEndpoint, HttpMethod.GET, entity, ADGetGroupResponse.class, new HashMap<>());

        return resp.getBody().getValue().get(0) != null?resp.getBody().getValue().get(0).getId():null;
    }

    @Override
    public String addToTeamsRest(String userId, String groupId){
        String accessToken = getAccessToken();
        String addEndpoint = "https://graph.microsoft.com/v1.0/groups/"+groupId+"/members/$ref";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+accessToken);

        Map<String, String> map= new LinkedHashMap<>();
        String[] roles = {"guest"};
        map.put("@odata.id", "https://graph.microsoft.com/v1.0/directoryObjects/"+userId);

        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);

        String result = restTemplate.postForObject(addEndpoint, request, String.class);

        return result;
    }

    @Override
    public ADUserResponse checkAdExistence(String userEmail){

        String accessToken = getAccessToken();
        try {
            String encodedId = URLEncoder.encode(userEmail, StandardCharsets.UTF_8.toString());
            String getMembersEP = "https://graph.microsoft.com/v1.0/users/"+encodedId;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer "+accessToken);
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.exchange(getMembersEP, HttpMethod.GET, request, String.class, 1);
            ObjectMapper mapper = new ObjectMapper();
            try {
                ADUserResponse user = mapper.readValue(response.getBody(), ADUserResponse.class);

                if (!user.getUserPrincipalName().isEmpty()) {
                    return user;
                }

            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }

        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Boolean checkMemberExistence(String groupId, String userId){

        String accessToken = getAccessToken();
        String getMembersEP = "https://graph.microsoft.com/v1.0/groups/"+groupId+"/members";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+accessToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange( getMembersEP, HttpMethod.GET, request, String.class, 1);
        //List<ADUserResponse> userList = (List<ADUserResponse>) restTemplate.getForObject(getMembersEP, ADUserResponse.class);
        //JSONObject jsonObject = (JSONObject) JSONValue.parse(response.getBody());
        ObjectMapper mapper = new ObjectMapper();
        try {
            ADUserWrapper users = mapper.readValue(response.getBody(), ADUserWrapper.class);
            Optional<ADUserResponse> member = users.getResponses().stream().filter(e -> e.getId().equals(userId)).findAny();
            if(member.isPresent()){
                return true;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String updateUserEmail(String userId, String userEmail, String givenName, String surname) {
        String accessToken = getAccessToken();
        String updateEndpoint = "https://graph.microsoft.com/v1.0/users/" + userId;
        String displayName = givenName + " " + surname;
        ADUpdateUserRequest updateObject = new ADUpdateUserRequest(userEmail, givenName, surname, displayName);

        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        restTemplate.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<ADUpdateUserRequest> entity = new HttpEntity<ADUpdateUserRequest>(updateObject, headers);
        String result = restTemplate.patchForObject(updateEndpoint, entity, String.class);

        return result;
    }

    @Override
    public Boolean checkEmailAvailability(String email){

        String accessToken = getAccessToken();
        String getMemberEmail = "https://graph.microsoft.com/v1.0/users?$search=\"mail:"+email+"\"";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+accessToken);
        headers.set("ConsistencyLevel", "eventual");
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange( getMemberEmail, HttpMethod.GET, request, String.class, 1);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ADUserWrapper users = mapper.readValue(response.getBody(), ADUserWrapper.class);
            if(users.getResponses().isEmpty()){
                return true;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

}

