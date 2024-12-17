package net.risesoft.sso;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "y9", ignoreInvalidFields = true, ignoreUnknownFields = true)
@Data
public class Oauth2Properties {
    
    private String systemName;
    private String tysfUrl;
    
    @NestedConfigurationProperty
    private Oauth2 oauth2 = new Oauth2();

    @Data
    public class Oauth2 {
        private String[] protectedUrlPatterns;
        private String clientId;
        private String clientSecret;
        private String accessTokenUri;
        private String introspectionUri;
        private String profileUri;
    }
}