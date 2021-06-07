package com.uagean.eIDEuSmartClass.ad.teams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
@ToString
@NoArgsConstructor
public class ADUserWrapper {

    @JsonProperty("@odata.context")
    public String context;

    @JsonProperty("value")
    public List<ADUserResponse> responses;
}
