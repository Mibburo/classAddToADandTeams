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
        JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
        Map<String, String> invitedUser = (HashMap<String, String>) jsonObject.get("invitedUser");
        String userId = invitedUser.get("id");
        System.out.println(adserv.addToTeamsRest(userId, "cfbefafc-ec6e-45c5-9e81-9125995e43ca"));
    }

    @Test
    public void testGetMembers(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        System.out.println(adserv.checkMemberExistence("cfbefafc-ec6e-45c5-9e81-9125995e43ca", "e57bc86f-5902-4930-82ae-a1dd80fbb676" ));
    }

    @Test
    public void testGetAdMembers(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        adserv.checkAdExistence("be071f2756a30deb4e672a99904e8a4ab4ae33432a4c959d40ddf1#EXT#@i4mlabUAegean.onmicrosoft.com");
    }


    @Test
    public void testGetMemberEmail(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        System.out.println(adserv.checkEmailAvailability("testemailKB@email.com"));
    }

    @Test
    public void updateEmail(){
        ActiveDirectoryService adserv = new ActiveDirectoryServiceImpl();
        System.out.println(adserv.updateUserEmail("1ffca454-859b-4097-9471-ea9f20c7cf6f", "testemailKB2@email.com", "konTest", "BitTest"));
    }


}
