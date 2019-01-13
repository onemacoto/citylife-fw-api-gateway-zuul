package com.citylife.api.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

import com.citylife.api.gateway.entrypoint.AuthExceptionEntryPoint;
import com.citylife.api.gateway.service.RibbonRemoteTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private ResourceServerProperties resourceServerProperties;

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Primary
	@Bean
	public RemoteTokenServices tokenServices() {
		final RemoteTokenServices tokenService = new RibbonRemoteTokenServices(restTemplate());
		tokenService.setCheckTokenEndpointUrl(this.resourceServerProperties.getTokenInfoUri());
		tokenService.setClientId(this.resourceServerProperties.getClientId());
		tokenService.setClientSecret(this.resourceServerProperties.getClientSecret());
		return tokenService;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().requestMatchers().antMatchers("/**").and().authorizeRequests()
				.antMatchers("/fn-auth/oauth/token").permitAll().anyRequest().authenticated();

	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
	}


}
