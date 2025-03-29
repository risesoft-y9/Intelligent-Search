package net.risesoft;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import net.risesoft.sso.Oauth2Properties;
import net.risesoft.sso.filter.Oauth2ResourceFilter;

@SpringBootApplication
@EnableConfigurationProperties(Oauth2Properties.class)
public class DataCenterApplication {
    @Autowired
    private Oauth2Properties y9Properties;

    public static void main(String[] args) {
        SpringApplication.run(DataCenterApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        List<String> list = new ArrayList<>();
        list.add("*");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(list);
        config.setAllowedMethods(list);
        config.setAllowedHeaders(list);
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new CorsFilter(source));
        filterBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterBean;
    }

    @Bean
    public FilterRegistrationBean<Oauth2ResourceFilter> oauth2ResourceFilter() {
    	Oauth2Properties.Oauth2 oauth2 = y9Properties.getOauth2();

        final FilterRegistrationBean<Oauth2ResourceFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new Oauth2ResourceFilter());
        filterBean.setAsyncSupported(false);
        filterBean.addUrlPatterns(oauth2.getProtectedUrlPatterns());
        filterBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);

        return filterBean;
    }
}
