package com.protalento.services.entities;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.protalento.services.enums.ServiceStatusType;

@Service
public final class ServiceErrorsPool {

	private static Map<ServiceStatusType, ServiceMessage> serviceError = new HashMap<>();

	static {

		serviceError.put(ServiceStatusType.ELEMENT_NOT_FOUND,
				new ServiceMessage("03", ServiceStatusType.ELEMENT_NOT_FOUND.toString() + ": "));
		serviceError.put(ServiceStatusType.USER_NO_EXISTS,
				new ServiceMessage("04", ServiceStatusType.USER_NO_EXISTS.toString() + ": "));
		serviceError.put(ServiceStatusType.USER_NO_EXISTS,
				new ServiceMessage("05", ServiceStatusType.NO_UPDATED.toString() + ": "));
	}

	public static ServiceMessage getServiceError(ServiceStatusType serviceStatusType, String... message) {

		ServiceMessage serviceMessage = serviceError.get(serviceStatusType);
		String msg = message.length > 0 ? message[0] : "";

		ServiceMessage serviceMessagereturned = ServiceMessage.builder().code(serviceMessage.getCode())
				.message(serviceMessage.getMessage()).build();
		
		serviceMessagereturned.setMessage(serviceMessagereturned.getMessage() + msg);
		return serviceMessagereturned;
	}

	private ServiceErrorsPool() {

	}

	public static void main(String[] args) {
		System.out.println(ServiceErrorsPool.getServiceError(ServiceStatusType.ELEMENT_NOT_FOUND));
	}
}
