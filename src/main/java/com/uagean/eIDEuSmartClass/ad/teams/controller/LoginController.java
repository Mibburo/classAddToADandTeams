package com.uagean.eIDEuSmartClass.ad.teams.controller;

import com.azure.core.credential.TokenCredential;


import com.google.gson.JsonPrimitive;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.AadUserConversationMember;
import com.microsoft.graph.models.ConversationMember;

import com.microsoft.graph.models.Invitation;
import com.microsoft.graph.requests.ConversationMemberCollectionPage;
import com.microsoft.graph.requests.GraphServiceClient;
import com.uagean.eIDEuSmartClass.ad.teams.controller.validator.EmailValidator;
import com.uagean.eIDEuSmartClass.ad.teams.model.EmailForm;
import com.uagean.eIDEuSmartClass.ad.teams.model.FormUser;
import com.uagean.eIDEuSmartClass.ad.teams.model.UserTest;
import com.uagean.eIDEuSmartClass.ad.teams.model.UserWrappers;
import com.uagean.eIDEuSmartClass.ad.teams.service.ActiveDirectoryService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Slf4j
@Controller
public class LoginController {

    @Value("team.id")
    private String teamId;

    @Autowired
    private ActiveDirectoryService adService;

    @Autowired
    private EmailValidator emailValidator;

    public final static String TOKEN_NAME = "access_token";
    public final static String TEAM_ID = "cfbefafc-ec6e-45c5-9e81-9125995e43ca"; //groupId
    // tenant-id = d4a81dbe-9310-4d28-b060-dc9cd82a3b8b
    private static final String EMAIL_FORM = "emailForm";

    @InitBinder(EMAIL_FORM)
    protected void emailFormInitBinder(WebDataBinder binder) {
        binder.setValidator(emailValidator);
    }


    @GetMapping(value = {"/landing", "/", ""})
    protected ModelAndView landingView(ModelMap model, @CookieValue(value = TOKEN_NAME, required = false) String jwtCookie, Principal principal) {
//        model.addAttribute("classRooms", classServ.findAll());
//        model.addAttribute("skypeRooms", skypeRoomServ.getAllRooms());
//        model.addAttribute("teams", teamServ.findAll());
//        model.addAttribute("loggedIn", principal != null);
//        model.addAttribute("loginPath", propServ.getPropByName("LOGIN_URL"));
        return new ModelAndView("testView");
    }
/**
     * If the user is found then the cookie type is used to check the type of
     * the login physical (QR), skype, or team and the browser is redirected to
     * the appropriate view
     *
     * @param jwtCookie
     * @param typeCookie
     * @param req
     * @param principal
     * @param model
     * @param redirectAttrs
     * @return
     */

    @RequestMapping(value = {"/eIDASSuccess", "/loginSuccess", "/ssi/registerEmail"})
    public ModelAndView login(@CookieValue(value = TOKEN_NAME, required = false) String jwtCookie,
                        @CookieValue(value = "type", required = false) String typeCookie,
                        HttpServletRequest req, Principal principal, ModelMap model, RedirectAttributes redirectAttrs) {
//        getKeycloakSecurityContext().getIdToken();


        if (principal != null) {

            /*KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
            AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();*/


           /* try {
                String eid = principal.getName();
                FormUser fuser = UserWrappers.wrapIDTokenToEidasUser(((KeycloakSecurityContext)
                                req.getAttribute(KeycloakSecurityContext.class.getName())).getIdToken(),eid);*/

                /*fuser.setAffiliation((String)accessToken.getOtherClaims().get("edugain-affiliation"));
                fuser.setDateOfBirth((String)accessToken.getOtherClaims().get("eidas-dateOfBirth"));
                fuser.setCurrentFamilyName((String)accessToken.getOtherClaims().get("eidas-familyName"));
                fuser.setCurrentFamilyName((String)accessToken.getOtherClaims().get("eidas-familyName"));
                fuser.setEngName((String)accessToken.getOtherClaims().get("edugain-given_name"));
                fuser.setEngSurname((String)accessToken.getOtherClaims().get("edugain-sn"));
                fuser.setEmail((String)accessToken.getOtherClaims().get("edugain-mail"));*/

//                String adAccessToken = adService.getAccessToken();
//                String response = adService.inviteGuestUser(fuser.getEmail(), adAccessToken);
//                JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
//                Map<String, String> invitedUser = (HashMap<String, String>) jsonObject.get("invitedUser");
//                String userId = invitedUser.get("id");
//
//                adService.addToTeamsRest(userId, teamId, adAccessToken);

                model.addAttribute("emailForm", new EmailForm());
            return new ModelAndView("landingView", model);

            /*} catch (*//*IOException*//* Exception e) {
                log.error(e.getMessage());

            }*/
        }
        model.addAttribute("error", "could not add user to AD");
        return new ModelAndView("redirect:/error", model);
    }

    @PostMapping("/addGuest")
    public String testAddMember(@ModelAttribute (EMAIL_FORM) EmailForm emailForm, @CookieValue(value = TOKEN_NAME, required = false) String jwtCookie,
                              @CookieValue(value = "type", required = false) String typeCookie,
                              HttpServletRequest req, Principal principal, Model model, RedirectAttributes redirectAttrs){


        log.info("2222222222222222222 test add email :{}", emailForm.getEmail());
        //TokenCredential adToken = adService.getAccessTokenAndExDt();
        //log.info("333333333333333333 adToken :{}", adToken);
        //AccessToken adAccesToken = new com.azure.core.credential.AccessToken(adToken.getLeft(), OffsetDateTime.parse(adToken.getRight()));*/
        if (principal != null) {

            KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
            AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();


            try {
                String eid = principal.getName();
                FormUser fuser = UserWrappers.wrapIDTokenToEidasUser(((KeycloakSecurityContext)
                        req.getAttribute(KeycloakSecurityContext.class.getName())).getIdToken(),eid);

                fuser.setAffiliation((String)accessToken.getOtherClaims().get("edugain-affiliation"));
                fuser.setDateOfBirth((String)accessToken.getOtherClaims().get("eidas-dateOfBirth"));
                fuser.setCurrentFamilyName((String)accessToken.getOtherClaims().get("eidas-familyName"));
                fuser.setCurrentFamilyName((String)accessToken.getOtherClaims().get("eidas-familyName"));
                fuser.setEngName((String)accessToken.getOtherClaims().get("edugain-given_name"));
                fuser.setEngSurname((String)accessToken.getOtherClaims().get("edugain-sn"));
                fuser.setEmail((String)accessToken.getOtherClaims().get("edugain-mail"));

                log.info("ooooooooooooooooooooo claims :{}", accessToken.getOtherClaims());
                //String adAccessToken = adService.getAccessToken();

                log.info("!!!!!!!!!!!!!!!!!! principal name :{}", principal);
                log.info("@@@@@@@@@@@@@@@@@@@@@@@@@ fuser email name :{}", principal.getName());
                log.info("ssssssssssssssssssssssssss fuser :{}", fuser);
                String groupId = adService.getGroupByName("Introduction to e-Privacy and Cybersecurity: Technology and Policy issues");
                log.info("kkkkkkkkkkkkkkkkkkkkkkk groupId :{}", groupId);
                String response = adService.inviteGuestUser(fuser.getEmail());
                JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
                Map<String, String> invitedUser = (HashMap<String, String>) jsonObject.get("invitedUser");
                String userId = invitedUser.get("id");
                log.info("####################### user id :{}", userId);
                log.info("hhhhhhhhhhhhhhhhhhhhhhh update");
                adService.updateUserEmail(userId, emailForm.getEmail(), fuser.getEngName(), fuser.getEngSurname());
                log.info("jjjjjjjjjjjjjjjjjjjjjjj add to teams");
                if(!adService.checkMemberExistence(groupId, userId)){
                    adService.addToTeamsRest(userId, groupId);
                }

                return "redirect:/";
            } catch (/*IOException*/ Exception e) {
                log.error(e.getMessage());

            }
        }
        model.addAttribute("error", "could not add user to AD");
        return "redirect:/error";
    }
}

