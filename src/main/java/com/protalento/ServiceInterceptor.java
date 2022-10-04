package com.protalento;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protalento.entities.User;
import com.protalento.jdbc.enums.Base64Schema;
import com.protalento.jdbc.implementations.UserImp;
import com.protalento.jdbc.utilities.Base64EncoderDecoder;
import com.protalento.services.entities.ErrorCredentialServiceMessage;
import com.protalento.services.entities.ServiceCredentialErrorsPool;
import com.protalento.services.enums.CredentialStatusType;
import com.protalento.utilities.StringUtils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Component
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInterceptor implements HandlerInterceptor {
	private static Logger logger = LogManager.getLogger();

	@Autowired
	private UserImp userImp;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.info("intercepted a request before being treated by the handler.");
		
		logger.info("getContextPath()" + request.getRequestURI());
		
		String authorizationHeaderValue = request.getHeader("Authorization");

		if (Objects.isNull(authorizationHeaderValue)) {
			shapeResponse(response, CredentialStatusType.EMPTY_CREDENTIALS);
		}

		authorizationHeaderValue = authorizationHeaderValue.replace("Basic ", "");

		String[] decodedHeader = Base64EncoderDecoder.operateBase64Schema(authorizationHeaderValue, Base64Schema.DECODE)
				.split(":");
		logger.info("decoded is: " + Arrays.toString(decodedHeader) + ", lenght " + decodedHeader.length);

		if (!(decodedHeader.length == 2)) {
			shapeResponse(response, CredentialStatusType.EMPTY_CREDENTIALS);
			return false;
		} else {
			
			if (StringUtils.isEmptyOrNull(decodedHeader[0])) {
				shapeResponse(response, CredentialStatusType.EMPTY_CREDENTIALS);
				return false;
			}
			
			logger.info("userImp:" + userImp);
			User user = userImp.findById(decodedHeader[0]);
			
			if (Objects.isNull(user) || !user.getPassword().equals(decodedHeader[1])) {
				logger.info("credential ERROR: " + "email not exists or invalid password");
				shapeResponse(response, CredentialStatusType.INCORRECT_CREDENTIALS);
				return false;
			}  
		}

		return true;
	}

	private void shapeResponse(HttpServletResponse response, CredentialStatusType credentialStatusType) {
		ObjectMapper mapper = new ObjectMapper();

		ErrorCredentialServiceMessage errorCredentialServiceMessage = ServiceCredentialErrorsPool
				.getErrorCredentialServiceMessage(credentialStatusType, "");

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		try {
			response.getWriter().write(mapper.writeValueAsString(errorCredentialServiceMessage));
		} catch (JsonProcessingException e) {
			logger.info("credential ERROR: problem with JSON conversion" + e);
		} catch (IOException e) {
			logger.info("credential ERROR: " + e);
		}
	}
}
