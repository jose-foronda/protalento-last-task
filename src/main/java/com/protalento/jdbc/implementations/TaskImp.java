package com.protalento.jdbc.implementations;

import java.math.BigInteger;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.protalento.entities.Task;
import com.protalento.jdbc.DAO;
import com.protalento.jdbc.DataSourceSubclassGenerator;
import com.protalento.jdbc.mappers.TaskMapper;
import com.protalento.utilities.LocalDateUtilities;

public class TaskImp implements DAO<Task, Integer> {
	private static final Logger logger = LogManager.getLogger();

	private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(
			DataSourceSubclassGenerator.getdriDriverManagerDataSource());

	@Override
	public Task findById(Integer id) {
		String query = "SELECT users_id, id, titulo, descripcion, fecha_vencimiento FROM tasks where id = ?";
		Task taskRecovered;

		try {
			taskRecovered = jdbcTemplate.queryForObject(query, new TaskMapper(), id);
		} catch (Exception e) {
			logger.error("no existing element. Error is:" + e);

			taskRecovered = null;
		}
		logger.info("findById= " + id + ", Task instance= " + taskRecovered);
		return taskRecovered;
	}

	@Override
	public Boolean insert(Task task) {
		String sql = "INSERT INTO tasks "
				+ "(users_id, titulo, descripcion, fecha_vencimiento) VALUES(?, ?, ?, ?)";

		PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(sql,
				Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);

		Object[] parameters = new Object[] { task.getIdUser(), task.getTitle(), task.getDescription(),
				task.getExpirationDate()
						.format(LocalDateUtilities.getDateTimeFormatter(LocalDateUtilities.SQL_DATE_PATTERN)) };
		
		preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
		preparedStatementCreatorFactory.setGeneratedKeysColumnNames("id");
		PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory
				.newPreparedStatementCreator(parameters);
		
		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

		byte insertedRows = (byte) jdbcTemplate.update(preparedStatementCreator, generatedKeyHolder);

		boolean inserted = insertedRows == 1;
		logger.info("was inserted? :" + inserted + ". " + task);
		task.setId( ((BigInteger)generatedKeyHolder.getKeys().get("insert_id")).intValue()); 
		return inserted;
	}

	@Override
	public Boolean update(Task task) {
		String sql = "UPDATE tasks SET titulo= ?, descripcion= ?,"
				+ " fecha_vencimiento= ? WHERE id= ?";

		int updatedRows = jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getExpirationDate()
				.format(LocalDateUtilities.getDateTimeFormatter(LocalDateUtilities.SQL_DATE_PATTERN)), task.getId());

		boolean updated = updatedRows == 1;
		logger.info("was updated? :" + updated + ". " + task);
		return updated;
	}

	@Override
	public Boolean save(Task task) {
		boolean modified;

		if (findById(task.getId()) == null) {
			modified = insert(task);
		} else {
			modified = update(task);
		}
		logger.info("was saved? :" + modified + ". " + task);
		return modified;
	}

	@Override
	public Boolean delete(Task task) {

		String sql = "DELETE FROM tasks WHERE users_id = ? and id= ?";

		int deletedRows = jdbcTemplate.update(sql, task.getIdUser(), task.getId());

		boolean deleted = deletedRows == 1;
		logger.info("Deleted element? : " + deleted + ". Task= " + task);
		return deleted;
	}

	@Override
	public List<Task> listAll() {
		String query = "SELECT users_id, id, titulo, descripcion, fecha_vencimiento FROM tasks";

		List<Task> taskList = jdbcTemplate.query(query, new TaskMapper());
		logger.info("Listing all tasks: " + taskList);
		return taskList;
	}

	public static void main(String[] args) {
		Task task = Task.builder().idUser(11).id(2).title("crear api de curso de Java")
				.description("debo crear una API para el 8 de septiembre…").expirationDate(LocalDate.parse("2022-09-08",
						LocalDateUtilities.getDateTimeFormatter(LocalDateUtilities.SQL_DATE_PATTERN)))
				.build();

		TaskImp taskImp = new TaskImp();

		// findById
//		taskImp.findById(1);

		// Inserting or Updating
		// taskImp.save(task);

		// Deleting element
//		taskImp.delete(task);

		// Inserting
		// taskImp.listAll();

	}

}
