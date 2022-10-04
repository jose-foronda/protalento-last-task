package com.protalento;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private ServiceInterceptor serviceInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) { 
		registry.addInterceptor(serviceInterceptor).excludePathPatterns("/usuarios/**");
		logger.info("User Interceptor Registered... ");

	}
}
