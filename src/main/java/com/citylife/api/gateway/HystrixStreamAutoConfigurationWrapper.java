package com.citylife.api.gateway;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.actuator.HasFeatures;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.hystrix.stream.HystrixStreamAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.stream.HystrixStreamClient;
import org.springframework.cloud.netflix.hystrix.stream.HystrixStreamProperties;
import org.springframework.cloud.netflix.hystrix.stream.HystrixStreamTask;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

@Configuration
public class HystrixStreamAutoConfigurationWrapper extends HystrixStreamAutoConfiguration {

	@Autowired
	private BindingServiceProperties bindings;

	@Autowired
	private HystrixStreamProperties properties;

	@Autowired
	@Output(HystrixStreamClient.OUTPUT)
	private MessageChannel outboundChannel;

	@Autowired(required = false)
	private Registration registration;

	@Bean
	public HasFeatures hystrixStreamQueueFeature() {
		return HasFeatures.namedFeature("Hystrix Stream (Queue)",
				HystrixStreamAutoConfiguration.class);
	}

	@PostConstruct
	public void init() {
		BindingProperties outputBinding = this.bindings.getBindings()
				.get(HystrixStreamClient.OUTPUT);
		if (outputBinding == null) {
			this.bindings.getBindings().put(HystrixStreamClient.OUTPUT,
					new BindingProperties());
		}
		BindingProperties output = this.bindings.getBindings()
				.get(HystrixStreamClient.OUTPUT);
		if (output.getDestination() == null || output.getDestination().equals(HystrixStreamClient.OUTPUT)) {
			output.setDestination(this.properties.getDestination());
		}
		if (output.getContentType() == null) {
			output.setContentType(this.properties.getContentType());
		}
	}

	@Bean
	public HystrixStreamProperties hystrixStreamProperties() {
		return new HystrixStreamProperties();
	}

	@Bean
	public HystrixStreamTask hystrixStreamTask(SimpleDiscoveryProperties simpleDiscoveryProperties) {
		ServiceInstance serviceInstance = this.registration;
		if (serviceInstance == null) {
			serviceInstance = simpleDiscoveryProperties.getLocal();
		}
		return new HystrixStreamTask(this.outboundChannel, serviceInstance,
				this.properties);
	}

	
	
}
