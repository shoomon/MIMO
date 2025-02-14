package com.bisang.backend.auth.infrastructure;

import static com.bisang.backend.common.utils.StringUtils.encodeString;
import static com.bisang.backend.common.utils.StringUtils.randomAlphaNumeric;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bisang.backend.user.controller.request.UserCreateOrLoginRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GoogleOAuthProvider {
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    private static final String scope = encodeString("openid email profile");

    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.google.token-uri}")
    private String tokenUri;

    @Value("${oauth2.google.resource-uri}")
    private String resourceUri;

    public UserCreateOrLoginRequest getUserInfoByGoogle(String code) {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        String accessToken = (String) responseBody.get("access_token");

        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        String userInfoUri = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<Map> userInfoResponse
                = restTemplate.exchange(userInfoUri, HttpMethod.GET, userInfoRequest, Map.class);

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String nickname = randomAlphaNumeric(20);
        String picture = (String) userInfo.get("picture");

        return new UserCreateOrLoginRequest(email, name, nickname, picture);
    }

}
