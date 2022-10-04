package com.protalento.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.protalento.entities.Task;
import com.protalento.utilities.LocalDateUtilities;

public final class TaskMapper implements RowMapper<Task> {
	private static final Logger logger = LogManager.getLogger();

	@Override
	public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {

			int idUser = rs.getInt("users_id");
			int id = rs.getInt("id");
			String title = rs.getString("titulo");
			String description = rs.getString("descripcion");

			LocalDate expirationDate = LocalDate.parse(rs.getString("fecha_vencimiento"),
					LocalDateUtilities.getDateTimeFormatter(LocalDateUtilities.SQL_DATE_PATTERN));

			logger.info("ResultSet result---> + " + "users_id = " + idUser + ", id= " + id + ", title= " + title
					+ ", description= " + description + ", expirationDate= " + expirationDate);

			return Task.builder().idUser(idUser).id(id).title(title).description(description)
					.expirationDate(expirationDate).build();
		} catch (Exception e) {
			logger.error("Error trying to map a ResultSet row to a User object. " + e);
			return null;
		}
	}

}
