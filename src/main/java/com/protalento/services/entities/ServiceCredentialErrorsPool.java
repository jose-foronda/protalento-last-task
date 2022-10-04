package com.protalento.services.entities;

import java.util.HashMap;
import java.util.Map;

import com.protalento.services.enums.CredentialStatusType;

public final class ServiceCredentialErrorsPool {

	private static Map<CredentialStatusType, ErrorCredentialServiceMessage> serviceError = new HashMap<>();

	static {

		serviceError.put(CredentialStatusType.INCORRECT_CREDENTIALS,
				new ErrorCredentialServiceMessage("01", "Las credenciales no son correctas."));
		serviceError.put(CredentialStatusType.EMPTY_CREDENTIALS,
				new ErrorCredentialServiceMessage("02", "Faltan credenciales."));
	}

	public static ErrorCredentialServiceMessage getErrorCredentialServiceMessage(
			CredentialStatusType credentialStatusType, String... message) {

		ErrorCredentialServiceMessage errorCredentialServiceMessage = serviceError.get(credentialStatusType);
		String msg = message.length > 0 ? message[0] : "";

		ErrorCredentialServiceMessage errorCredentialServiceMessageReturned = ErrorCredentialServiceMessage.builder()
				.error(errorCredentialServiceMessage.getError()).mensaje(errorCredentialServiceMessage.getMensaje())
				.build();

		errorCredentialServiceMessageReturned.setMensaje(errorCredentialServiceMessageReturned.getMensaje() + msg);
		return errorCredentialServiceMessageReturned;
	}

	private ServiceCredentialErrorsPool() {

	}

	public static void main(String[] args) {
		System.out.println(
				ServiceCredentialErrorsPool.getErrorCredentialServiceMessage(CredentialStatusType.INCORRECT_CREDENTIALS));
	}
}
