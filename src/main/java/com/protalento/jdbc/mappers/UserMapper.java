package com.protalento.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.protalento.entities.User;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
public final class UserMapper implements RowMapper<User> {
	private static final Logger logger = LogManager.getLogger();

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {

		try {

			int id = rs.getInt("id");
			String email = rs.getString("email");
			String password = rs.getString("password");
			logger.info("ResultSet result---> + " + "id = " + id + ", email= " + email + ", password= " + password);
			return User.builder().id(id).email(email).password(password).build();
		} catch (Exception e) {
			logger.error("Error trying to map a ResultSet row to a User object. " + e);
			return null;
		}  

	}

}
