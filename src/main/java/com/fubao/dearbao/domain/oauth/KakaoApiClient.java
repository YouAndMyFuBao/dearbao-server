package com.fubao.dearbao.domain.oauth;

import com.fubao.dearbao.api.service.auth.dto.KakaoInfoDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import com.fubao.dearbao.global.common.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;
    @Value("${oauth.kakao.admin-key}")
    private String adminKey;
    private final RestTemplate restTemplate;

    public String requestAccessToken(KakaoLoginServiceDto kakaoLoginServiceDto) {
        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = kakaoLoginServiceDto.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        ResponseEntity<KakaoToken> response;
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, KakaoToken.class);
        } catch (HttpClientErrorException e) {
            throw new CustomException(ResponseCode.NOT_GET_KAKAO_INFO);
        }

        if (response.getBody() == null) {
            throw new CustomException(ResponseCode.NOT_GET_KAKAO_INFO);
        }

        return response.getBody().getAccessToken();
    }

    public KakaoInfoDto requestOAuthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys",
            "[\"id\", \"kakao_account.\", \"properties.\", \"has_signed_up.\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(url, request, KakaoInfoDto.class);
    }

    public void disconnect(String providerId) {
        String url = apiUrl + "/v1/user/unlink";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "KakaoAK " + adminKey);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", providerId);
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
