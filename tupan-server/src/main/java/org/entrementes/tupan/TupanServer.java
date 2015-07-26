package org.entrementes.tupan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.entrementes.tupan.configurations.TupanInformation;
import org.entrementes.tupan.entities.ConsumptionFlag;
import org.entrementes.tupan.entities.User;
import org.entrementes.tupan.model.ConsumerType;
import org.entrementes.tupan.repositories.UserRepository;
import org.entrementes.tupan.services.MockGridConnection;
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
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}

	@Bean(name = "org.dozer.Mapper")
	public DozerBeanMapper dozerBean() {
		List<String> mappingFiles = Arrays.asList();
		DozerBeanMapper dozerBean = new DozerBeanMapper();
		dozerBean.setMappingFiles(mappingFiles);
		return dozerBean;
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restDispatcher = new RestTemplate(
				clientHttpRequestFactory());
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new MappingJackson2HttpMessageConverter());
		restDispatcher.setMessageConverters(converters);
		return restDispatcher;
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(2000);
		factory.setConnectTimeout(2000);
		return factory;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TupanServer.class, args);
		prepareDatabase(context);
		prepareGridState(context);
	}

	private static void prepareGridState(ConfigurableApplicationContext context) {
		MockGridConnection state = context.getBean(MockGridConnection.class);
		TupanInformation config = context.getBean(TupanInformation.class);
		state.setFlag(ConsumptionFlag.valueOf(config.getConsumptionFlag()));
		state.setBaseFare(config.getBaseFare());
		state.setSystemMessage(config.getSystemMessage());
		state.setUpdateInterval(config.getUpdateInterval());
		state.setLastUpadate(LocalDateTime.now());
	}

	private static void prepareDatabase(ConfigurableApplicationContext context) {
		UserRepository userRepository = context.getBean(UserRepository.class);
		userRepository.deleteAll();
		
		User consumer = new User();
		
		consumer.setUserId("gunisalvo");
		consumer.setUtlitiesProviderId("INFNET");
		consumer.setUserType(ConsumerType.RESIDENTIAL_A);
		
		userRepository.save(consumer);
	}

}
