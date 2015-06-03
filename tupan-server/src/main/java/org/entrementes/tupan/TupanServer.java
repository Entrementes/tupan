package org.entrementes.tupan;

import java.util.ArrayList;
import java.util.List;

import org.entrementes.tupan.entity.CustomerEntity;
import org.entrementes.tupan.entity.FareFlagEntity;
import org.entrementes.tupan.model.Flag;
import org.entrementes.tupan.repositories.CustomerRepository;
import org.entrementes.tupan.repositories.DeviceRegistrationRepository;
import org.entrementes.tupan.repositories.FareFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TupanServer extends WebMvcConfigurerAdapter {

	@Autowired
	private CustomerRepository repository;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}
	
	@Bean
	public RestTemplate restTemplate(){
		RestTemplate restDispatcher = new RestTemplate(clientHttpRequestFactory());
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new MappingJackson2HttpMessageConverter());
		restDispatcher.setMessageConverters(converters);
		return restDispatcher;
	}
	
	private ClientHttpRequestFactory clientHttpRequestFactory(){
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(2000);
		factory.setConnectTimeout(2000);
		return factory;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TupanServer.class, args);
		
		CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
		customerRepository.deleteAll();
		
		CustomerEntity streamer = new CustomerEntity();
		streamer.setCode("streamer");
		streamer.setName("UDP customer");
		streamer.setBaseFare(10.0);
		customerRepository.save(streamer);
		
		CustomerEntity hook = new CustomerEntity();
		hook.setCode("hook");
		hook.setName("Webhook customer");
		hook.setBaseFare(9.0);
		customerRepository.save(hook);
		
		CustomerEntity pooler = new CustomerEntity();
		pooler.setCode("pooler");
		pooler.setName("Pooling customer");
		pooler.setBaseFare(11.0);
		customerRepository.save(pooler);
		
		FareFlagRepository fareFlagRepository = context.getBean(FareFlagRepository.class);
		fareFlagRepository.deleteAll();
		
		FareFlagEntity white = new FareFlagEntity();
		white.setFlag(Flag.WHITE);
		white.setMultiplier(0.75);
		fareFlagRepository.save(white);
		
		FareFlagEntity green = new FareFlagEntity();
		green.setFlag(Flag.GREEN);
		green.setMultiplier(1.0);
		fareFlagRepository.save(green);
		
		FareFlagEntity yellow = new FareFlagEntity();
		yellow.setFlag(Flag.YELLOW);
		yellow.setMultiplier(1.5);
		fareFlagRepository.save(yellow);
		
		FareFlagEntity red = new FareFlagEntity();
		red.setFlag(Flag.RED);
		red.setMultiplier(2.0);
		fareFlagRepository.save(red);
		
		DeviceRegistrationRepository deviceRegistrationRepository = context.getBean(DeviceRegistrationRepository.class);
		deviceRegistrationRepository.deleteAll();
		
	}
	
	
}
