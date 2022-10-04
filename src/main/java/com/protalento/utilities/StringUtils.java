package com.protalento.utilities;

import java.util.Objects;

public final class StringUtils {

	public static boolean isEmptyOrNull(String string) {
		return Objects.isNull(string) || string.isEmpty();
	}

	private StringUtils() {
	}
}
