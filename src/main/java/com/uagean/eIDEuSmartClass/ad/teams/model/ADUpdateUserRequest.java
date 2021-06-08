package com.uagean.eIDEuSmartClass.ad.teams.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ADUpdateUserRequest {
    private String mail;
    private String givenName;
    private String surname;
    private String displayName;
}
