package com.rahdevelopers.api.zuulserver.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RefreshScope
@Configuration
@EnableResourceServer
public class OauthResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${config.security.ouath.jwt.key}")
	private String jwtKey;
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(this.tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/producto/listar", "/api/item/listar").permitAll()
				.antMatchers(HttpMethod.GET, "/api/producto/**", "/api/item/**").hasRole("USER")
				.antMatchers("/api/producto/**", "/api/item/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and().cors().configurationSource(corsConfigurationSource());
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-type"));
		corsConfiguration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();

		corsSource.registerCorsConfiguration("/**", corsConfiguration);

		return corsSource;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		CorsFilter corsFilter = new CorsFilter(this.corsConfigurationSource());
		FilterRegistrationBean<CorsFilter> beanCorsFilter = new FilterRegistrationBean<>(corsFilter);
		return beanCorsFilter;
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(this.accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(this.jwtKey);
		return tokenConverter;
	}
}
