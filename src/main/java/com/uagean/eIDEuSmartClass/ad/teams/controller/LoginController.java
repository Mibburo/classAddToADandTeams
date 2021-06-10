package com.uagean.eIDEuSmartClass.ad.teams.controller;

import com.uagean.eIDEuSmartClass.ad.teams.model.*;
import com.uagean.eIDEuSmartClass.ad.teams.service.ActiveDirectoryService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Controller
public class LoginController {

    @Value("team.id")
    private String teamId;

    @Autowired
    private ActiveDirectoryService adService;

    public final static String TOKEN_NAME = "access_token";
    public final static String TEAM_ID = "cfbefafc-ec6e-45c5-9e81-9125995e43ca"; //groupId
    // tenant-id = d4a81dbe-9310-4d28-b060-dc9cd82a3b8b
    private static final String EMAIL_FORM = "emailFormCmd";

/**
     * @param jwtCookie
     * @param typeCookie
     * @param req
     * @param principal
     * @param model
     * @param redirectAttrs
     * @return
     */

    @RequestMapping(value = {"/eIDASSuccess", "/loginSuccess", "/ssi/registerEmail"})
    public ModelAndView login(@ModelAttribute (EMAIL_FORM) EmailForm emailForm, @CookieValue(value = TOKEN_NAME, required = false) String jwtCookie,
                        @CookieValue(value = "type", required = false) String typeCookie,
                        HttpServletRequest req, Principal principal, ModelMap model, RedirectAttributes redirectAttrs) {
        //if (principal != null) {
            model.addAttribute("errMessage", emailForm.getMessage());
            return new ModelAndView("landingView", model);
        //}
//        model.addAttribute("error", "could not add user to AD");
//        return new ModelAndView("redirect:/error", model);
    }

    @RequestMapping("/addGuest")
    public ModelAndView addMember(@ModelAttribute (EMAIL_FORM) EmailForm emailForm, @CookieValue(value = TOKEN_NAME, required = false) String jwtCookie,
                                @CookieValue(value = "type", required = false) String typeCookie, Errors errors,
                                HttpServletRequest req, Principal principal, ModelMap model, RedirectAttributes redirectAttrs){
        if (!isEmailValid(emailForm)) {
            redirectAttrs.addFlashAttribute(EMAIL_FORM, emailForm);
            return new ModelAndView("redirect:/ssi/registerEmail");
        }

        //return new ModelAndView("redirect:/ssi/registerEmail");
        if (principal != null) {

            KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
            AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();

            IDToken idToken = ((KeycloakSecurityContext)req.getAttribute(KeycloakSecurityContext.class.getName())).getIdToken();

            String email = idToken.getEmail();

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

                String adEmail = idToken.getEmail().substring(0,54)+"#EXT#@i4mlabUAegean.onmicrosoft.com";
                if(!adService.checkEmailAvailability(emailForm.getEmail())){
                    emailForm.setMessage("Email already exists, please use an other");
                    redirectAttrs.addFlashAttribute(EMAIL_FORM, emailForm);
                    return new ModelAndView("redirect:/ssi/registerEmail");
                }
                Boolean exists = true;
                ADUserResponse user = new ADUserResponse();
                try {
                    user = adService.checkAdExistence(emailForm.getEmail());
                }catch (HttpClientErrorException.NotFound e) {
                    log.error(e.getMessage());
                    exists = false;
                }
                if(exists && user != null){
                    handleTeam(user.getId());
                    return new ModelAndView("redirect:/viewResult");
                }
                String invitedUserResponse = adService.inviteGuestUser(emailForm.getEmail());
                JSONObject jsonObject = (JSONObject) JSONValue.parse(invitedUserResponse);
                Map<String, String> invitedUser = (HashMap<String, String>) jsonObject.get("invitedUser");
                String userId = invitedUser.get("id");
                adService.updateUserEmail(userId, emailForm.getEmail(), fuser.getEngName(), fuser.getEngSurname());
                handleTeam(userId);
                return new ModelAndView("redirect:/viewResult");
            } catch (Exception e) {
                log.error(e.getMessage());

            }
        }
        model.addAttribute("error", "could not add user to AD");
        return new ModelAndView("redirect:/error", model);
    }

    @GetMapping("/viewResult")
    protected ModelAndView resultPage(@ModelAttribute (EMAIL_FORM) EmailForm emailForm, @CookieValue(value = TOKEN_NAME, required = false) String jwtCookie,
                                      @CookieValue(value = "type", required = false) String typeCookie, Errors errors,
                                      HttpServletRequest req, Principal principal, ModelMap model, RedirectAttributes redirectAttrs){

        return new ModelAndView("result", model);

    }

    private void handleTeam(String userId){
        String groupId = adService.getGroupByName("Introduction to e-Privacy and Cybersecurity: Technology and Policy issues");
        if(!adService.checkMemberExistence(groupId, userId)){
            adService.addToTeamsRest(userId, groupId);
        }
    }

    private Boolean isEmailValid(EmailForm emailForm){
        Pattern mailPattern = Pattern
                .compile("([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{1,})");
        if (null == emailForm.getEmail() || "".equals(emailForm.getEmail()) || !mailPattern.matcher(emailForm.getEmail()).matches()) {
            emailForm.setMessage("Email invalid format, please correct it");
            return false;
        }
        return true;
    }
}

