package com.uagean.eIDEuSmartClass.ad.teams.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ADGuestInviteRequest {
    private String invitedUserEmailAddress;
    private String inviteRedirectUrl;

}
