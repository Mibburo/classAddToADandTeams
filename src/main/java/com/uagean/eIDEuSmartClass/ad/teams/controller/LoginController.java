package com.uagean.eIDEuSmartClass.ad.teams.controller;

import com.uagean.eIDEuSmartClass.ad.teams.controller.validator.EmailValidator;
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
import javax.validation.Valid;
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
    private static final String EMAIL_FORM = "emailFormCmd";

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
    public ModelAndView login(@ModelAttribute (EMAIL_FORM) EmailForm emailForm, @CookieValue(value = TOKEN_NAME, required = false) String jwtCookie,
                        @CookieValue(value = "type", required = false) String typeCookie,
                        HttpServletRequest req, Principal principal, ModelMap model, RedirectAttributes redirectAttrs) {

        if (principal != null) {
            model.addAttribute("emailForm", new EmailForm());
            return new ModelAndView("landingView", model);
        }
        model.addAttribute("error", "could not add user to AD");
        return new ModelAndView("redirect:/error", model);
    }

    @RequestMapping("/addGuest")
    public ModelAndView testAddMember(@ModelAttribute (EMAIL_FORM) @Valid EmailForm emailForm, @CookieValue(value = TOKEN_NAME, required = false) String jwtCookie,
                                @CookieValue(value = "type", required = false) String typeCookie, Errors errors,
                                HttpServletRequest req, Principal principal, ModelMap model, RedirectAttributes redirectAttrs){
        if (errors.hasErrors()) {
            return new ModelAndView("redirect:/ssi/registerEmail", model);
        }

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
                Boolean exists = true;
                ADUserResponse user = new ADUserResponse();
                try {
                    user = adService.checkAdExistence(adEmail);
                }catch (HttpClientErrorException.NotFound e) {
                    log.error(e.getMessage());
                    exists = false;
                }
                if(exists && user != null){
                    handleTeam(user.getId());
                    return new ModelAndView("teamsRedirect");
                }
                String invitedUserResponse = adService.inviteGuestUser(idToken.getEmail());
                JSONObject jsonObject = (JSONObject) JSONValue.parse(invitedUserResponse);
                Map<String, String> invitedUser = (HashMap<String, String>) jsonObject.get("invitedUser");
                String userId = invitedUser.get("id");
                adService.updateUserEmail(userId, emailForm.getEmail(), fuser.getEngName(), fuser.getEngSurname());
                handleTeam(userId);
                return new ModelAndView("teamsRedirect");
            } catch (Exception e) {
                log.error(e.getMessage());

            }
        }
        model.addAttribute("error", "could not add user to AD");
        return new ModelAndView("redirect:/error", model);
    }

    private void handleTeam(String userId){
        String groupId = adService.getGroupByName("Introduction to e-Privacy and Cybersecurity: Technology and Policy issues");
        if(!adService.checkMemberExistence(groupId, userId)){
            adService.addToTeamsRest(userId, groupId);
        }
    }
}

