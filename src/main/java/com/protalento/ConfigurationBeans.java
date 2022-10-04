package com.protalento;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.protalento.jdbc.implementations.TaskImp;
import com.protalento.jdbc.implementations.UserImp;
import com.protalento.usuarios.jdbc.implementations.UsuarioImp;

@Configuration
public class ConfigurationBeans {

	@Bean
	public TaskImp taskImp() {
		return new TaskImp();
	}
	
	@Bean
	public UserImp userImp() {
		return new UserImp();
	}
	
	@Bean
	public UsuarioImp usuarioImp() {
		return new UsuarioImp();
	}
}
