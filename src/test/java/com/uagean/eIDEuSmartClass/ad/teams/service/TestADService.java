package com.uagean.eIDEuSmartClass.ad.teams.service;

import com.uagean.eIDEuSmartClass.ad.teams.service.impl.ActiveDirectoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestADService {


    @Test
    public void testGetAccessToken(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        System.out.println(adserv.getAccessToken());
    }

    @Test
    public void testMakeGuest(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        System.out.println(adserv.inviteGuestUser("testemailKB@email.com"));
    }

    @Test
    public void testAddToTeams(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        //String accessToken = adserv.getAccessToken();
        String response = adserv.inviteGuestUser("testemailKB@email.com");
        log.info("xxxxxxxxxxxxxxxxxxx response : {}", response);
        JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
        log.info("yyyyyyyyyyyyyyyyyy jsonObject : {}", jsonObject);
        Map<String, String> invitedUser = (HashMap<String, String>) jsonObject.get("invitedUser");
        String userId = invitedUser.get("id");
        log.info("zzzzzzzzzzzzzzzzzzzzzz userId : {}", userId);
        System.out.println(adserv.addToTeamsRest(userId, "cfbefafc-ec6e-45c5-9e81-9125995e43ca"));
    }

    @Test
    public void testGetMembers(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        System.out.println(adserv.checkMemberExistence("cfbefafc-ec6e-45c5-9e81-9125995e43ca", "f54198c6-0c71-413c-bdc4-b021463a7745" ));
    }

    @Test
    public void testGetAdMembers(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        adserv.checkAdExistence("be071f2756a30deb4e672a99904e8a4ab4ae33432a4c959d40ddf1#EXT#@i4mlabUAegean.onmicrosoft.com");
    }

}
