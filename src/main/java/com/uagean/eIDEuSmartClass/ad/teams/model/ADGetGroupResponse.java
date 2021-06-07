package com.uagean.eIDEuSmartClass.ad.teams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ADGetGroupResponse {
    @JsonProperty("@odata.context")
    public String odataContext;
    @JsonProperty("@odata.count")
    public int odataCount;
    public List<Value> value;

    @Getter
    @Setter
    public static class Value {
        public String displayName;
        public String mail;
        public String mailNickname;
        public String id;
    }
}

