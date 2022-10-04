package com.protalento.entities;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class Task {
	private int idUser;
	private int id;
	private String title;
	private String description;
	private LocalDate expirationDate;
}
