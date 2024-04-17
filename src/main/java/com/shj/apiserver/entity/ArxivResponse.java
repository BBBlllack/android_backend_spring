package com.shj.apiserver.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ArxivResponse {
    @JsonProperty("Code")
    private Integer Code;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Data")
    private Map<String, Object> Data;

    @JsonProperty("RequestId")
    private String RequestId;

    @JsonProperty("RequestTime")
    private String RequestTime;
}
