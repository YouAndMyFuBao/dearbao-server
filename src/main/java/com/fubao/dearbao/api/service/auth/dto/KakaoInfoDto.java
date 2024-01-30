package com.fubao.dearbao.api.service.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoDto {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String id;

    @JsonCreator
    public KakaoInfoDto(@JsonProperty("id") String id) {
        this.id = id;
    }
}
