package net.risesoft.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import filters.CheckUserLoginFilterDataCenter;
import filters.SetCharacterEncodingFilter;

import net.risesoft.y9.Y9Context;
import net.risesoft.y9.configuration.Y9ConfigurationProperties;

@Configuration
@EnableConfigurationProperties(Y9ConfigurationProperties.class)
public class DataCenterConfiguraton implements WebMvcConfigurer {

	private static final Logger log = LoggerFactory.getLogger(DataCenterConfiguraton.class);

	@Bean
	@ConditionalOnMissingBean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public static RequestContextFilter requestContextFilter() {
		return new OrderedRequestContextFilter();
	}

	@Bean
	public FilterRegistrationBean checkUserLoginFilter() {
		log.debug("init CheckUserLoginFilterDataCenter ...");
		FilterRegistrationBean filterBean = new FilterRegistrationBean();
		filterBean.setFilter(new CheckUserLoginFilterDataCenter());
		filterBean.setAsyncSupported(false);
		filterBean.setOrder(50);
		filterBean.addUrlPatterns("/*");
		return filterBean;
	}

	@Bean
	public Y9Context y9Context() {
		return new Y9Context();
	}

	@Bean
	public FilterRegistrationBean riseCharacterEncodingFilter() {
		FilterRegistrationBean filterBean = new FilterRegistrationBean();
		filterBean.setFilter(new SetCharacterEncodingFilter());
		Map<String, String> map = new HashMap<String, String>();
		map.put("encoding", "UTF-8");
		map.put("ignore", "false");
		filterBean.setInitParameters(map);
		filterBean.setName("Set Character Encoding");
		List<String> list = new ArrayList<String>();
		list.add("/*");
		filterBean.setUrlPatterns(list);
		return filterBean;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.parseMediaType("text/html;charset=UTF-8"));
		converter.setSupportedMediaTypes(supportedMediaTypes);
		return converter;
	}
}
