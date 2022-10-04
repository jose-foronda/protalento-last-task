package com.protalento.entities.dtos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.protalento.entities.Task;
import com.protalento.utilities.LocalDateUtilities;

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
public class TaskUpdatedDTO {
	private int idUser;
	private int id;
	private String title;
	private String description;
	@JsonFormat(shape = Shape.STRING, pattern = LocalDateUtilities.SQL_DATE_PATTERN)
	private LocalDate expirationDate;
	private String state;
	private String message;

	public static TaskUpdatedDTO getTaskUpdatedDTO(Task task, String message) {

		String state = ChronoUnit.DAYS.between(LocalDate.now(), task.getExpirationDate()) > 0 ? "Por realizar."
				: "Vencida.";

		TaskUpdatedDTO taskUpdatedDTO = TaskUpdatedDTO.builder().idUser(task.getIdUser()).id(task.getId())
				.title(task.getTitle()).description(task.getDescription()).expirationDate(task.getExpirationDate())
				.state(state)
				.message(message).build();

		return taskUpdatedDTO;
	}
}
