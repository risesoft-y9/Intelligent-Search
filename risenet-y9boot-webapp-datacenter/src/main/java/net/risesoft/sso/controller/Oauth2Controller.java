package net.risesoft.sso.controller;

import net.risesoft.sso.Oauth2Properties;
import net.risesoft.sso.model.oauth2.AccessToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/oauth2.0")
public class Oauth2Controller {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Oauth2Properties demoProperties;
    
    @GetMapping("/accessToken")
    public AccessToken getAccessToken(String code, String redirectUri) {
        Oauth2Properties.Oauth2 oauth2Properties = demoProperties.getOauth2();

        // 设置url和请求参数
        String url = UriComponentsBuilder.fromUriString(oauth2Properties.getAccessTokenUri())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", oauth2Properties.getClientId())
                .queryParam("client_secret", oauth2Properties.getClientSecret())
                .queryParam("code", code)
                .queryParam("redirect_uri", redirectUri)
                .build().toString();
        
        ResponseEntity<AccessToken> responseEntity = restTemplate.getForEntity(url, AccessToken.class);

        return responseEntity.getBody();
    }

    @GetMapping("/refreshAccessToken")
    public AccessToken refreshAccessToken(String refreshToken) {
        Oauth2Properties.Oauth2 oauth2Properties = demoProperties.getOauth2();

        // 设置url和请求参数
        String url = UriComponentsBuilder.fromUriString(oauth2Properties.getAccessTokenUri())
                .queryParam("grant_type", "refresh_token")
                .queryParam("client_id", oauth2Properties.getClientId())
                .queryParam("client_secret", oauth2Properties.getClientSecret())
                .queryParam("refresh_token", refreshToken)
                .build().toString();

        ResponseEntity<AccessToken> responseEntity = restTemplate.getForEntity(url, AccessToken.class);

        return responseEntity.getBody();
    }
}
