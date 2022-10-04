package com.protalento.jdbc.utilities;

import java.util.Base64;

import com.protalento.jdbc.enums.Base64Schema;

public final class Base64EncoderDecoder {

	private Base64EncoderDecoder() {
		super();
	}

	public static String operateBase64Schema(String input, Base64Schema schemaSel) {

		switch (schemaSel) {
		case ENCODE:
			return Base64.getEncoder().encodeToString(input.getBytes());

		default:
			return new String(Base64.getDecoder().decode(input));
		}
	}
}
