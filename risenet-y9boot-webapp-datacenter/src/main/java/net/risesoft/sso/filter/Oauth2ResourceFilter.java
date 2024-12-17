package net.risesoft.sso.filter;

import net.risesoft.sso.Oauth2Properties;
import net.risesoft.sso.exception.AuthException;
import net.risesoft.sso.model.oauth2.IntrospectionResponse;
import net.risesoft.sso.model.oauth2.UserInfo;
import net.risesoft.sso.util.JacksonUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class Oauth2ResourceFilter implements Filter {

    private Oauth2Properties y9Properties;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        y9Properties = webApplicationContext.getBean(Oauth2Properties.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) servletRequest;
        
        String accessToken = getAccessTokenFromRequest(request);
        UserInfo userInfo = checkAccessToken(accessToken);
        if (userInfo == null) {
            userInfo = getProfile(accessToken);
        }
        LoginUserHolder.setUserInfo(userInfo);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private UserInfo getProfile(String accessToken) {
        String profileUri = y9Properties.getOauth2().getProfileUri();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + accessToken);

        URI uri = URI.create(profileUri + "?access_token=" + accessToken);
        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        String profile = responseEntity.getBody();
        return JacksonUtil.readValue(profile, UserInfo.class);
    }

    private UserInfo checkAccessToken(String accessToken) {
        Oauth2Properties.Oauth2 oauth2 = y9Properties.getOauth2();
        String clientId = oauth2.getClientId();
        String clientSecret = oauth2.getClientSecret();
        String introspectionUri = oauth2.getIntrospectionUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret, StandardCharsets.UTF_8);

        URI uri = URI.create(introspectionUri + "?token=" + accessToken);
        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.POST, uri);
        ResponseEntity<IntrospectionResponse> responseEntity = restTemplate.exchange(requestEntity, IntrospectionResponse.class);
        IntrospectionResponse introspectionResponse = responseEntity.getBody();
        if (!introspectionResponse.isActive()) {
            throw new AuthException("令牌已过期");
        }
        String attr = introspectionResponse.getAttr();
        return JacksonUtil.readValue(attr, UserInfo.class);
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        // 从请求参数或请求头中获取令牌 如果两者中都没有则抛出异常
        String accessToken = request.getParameter("access_token");
        if (StringUtils.isBlank(accessToken)) {
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring("Bearer ".length());
            }
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new AuthException("未传入令牌");
        }
        return accessToken;
    }
    
}
