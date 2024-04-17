package com.shj.apiserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shj.apiserver.entity.ArxivResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JsonUtils {

    public static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    public static String obj2Json(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArxivResponse Json2Arxiv(String json){
        try {
            return mapper.readValue(json, ArxivResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
