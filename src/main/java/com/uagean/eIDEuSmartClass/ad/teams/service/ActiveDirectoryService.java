package com.uagean.eIDEuSmartClass.ad.teams.service;

import com.uagean.eIDEuSmartClass.ad.teams.model.ADUserResponse;

public interface ActiveDirectoryService {

    public String getAccessToken();

    public String inviteGuestUser(String userEmail);
    public Boolean checkMemberExistence(String groupId, String userId);
    public String getGroupByName(String groupName);
    public String addToTeamsRest(String userId, String teamId);
    public ADUserResponse checkAdExistence(String userEmail);
    public String updateUserEmail(String userId, String userEmail, String givenName, String surname);

}
