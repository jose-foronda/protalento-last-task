package com.protalento.controllers;

import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protalento.entities.Task;
import com.protalento.entities.dtos.TaskDTO;
import com.protalento.entities.dtos.TaskDeletedDTO;
import com.protalento.entities.dtos.TaskSavedDTO;
import com.protalento.entities.dtos.TaskUpdatedDTO;
import com.protalento.jdbc.implementations.TaskImp;
import com.protalento.jdbc.implementations.UserImp;
import com.protalento.services.entities.ServiceErrorsPool;
import com.protalento.services.enums.ServiceStatusType;

@RestController
@RequestMapping(path = "/tareas")
public class TaskController {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private TaskImp taskImp;
	@Autowired
	private UserImp userImp;

	@GetMapping(path = "/{id}")
	public ResponseEntity<Object> getTaskById(@PathVariable int id) {
		Task task = taskImp.findById(id);

		if (Objects.isNull(task)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ServiceErrorsPool
					.getServiceError(ServiceStatusType.ELEMENT_NOT_FOUND, "Id= " + id + " no existe."));
		}

		TaskDTO taskDTO = TaskDTO.getTaskDTO(task);
		logger.info("Task Controller. method getTaskById ..." + id + ". Task: " + taskDTO);

		return ResponseEntity.status(HttpStatus.OK).body(taskDTO);
	}

	@PostMapping(path = "/agregar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> postTask(@RequestBody TaskDTO taskDTO) {
		if (!(userImp.listAll().get(0).getId().equals(taskDTO.getIdUser()))) {
			return ResponseEntity.status(HttpStatus.OK).body(
					ServiceErrorsPool.getServiceError(ServiceStatusType.USER_NO_EXISTS, " El usuario no existe."));
		}

		Task task = TaskDTO.getTask(taskDTO);

		boolean isSaved = taskImp.save(task);

		TaskSavedDTO taskSavedDTO = TaskSavedDTO.builder().idUser(task.getIdUser()).id(task.getId()).build();

		logger.info("TaskDTO received through as JSON through HTTP post." + "TaskDTO= " + taskDTO);
		logger.info("task inserted? " + isSaved + ". TaskSavedDTO= " + taskSavedDTO);
		return ResponseEntity.status(HttpStatus.OK).body(taskSavedDTO);
	}

	@PutMapping(path = "/modificar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> putTask(@RequestBody TaskDTO taskDTO) {

		if (!(userImp.listAll().get(0).getId().equals(taskDTO.getIdUser()))) {
			return ResponseEntity.status(HttpStatus.OK).body(
					ServiceErrorsPool.getServiceError(ServiceStatusType.USER_NO_EXISTS, " El usuario no existe."));
		}

		Task task = TaskDTO.getTask(taskDTO);

		boolean isSaved = taskImp.save(task);
		if (!isSaved) {
			return ResponseEntity.status(HttpStatus.OK).body(
					ServiceErrorsPool.getServiceError(ServiceStatusType.NO_UPDATED, " No pudo actualizar elemento."));
		}

		TaskUpdatedDTO taskUpdatedDTO = TaskUpdatedDTO.getTaskUpdatedDTO(task, "datos actualizados correctamente.");

		logger.info("TaskDTO received as JSON through HTTP post." + "TaskDTO= " + taskDTO);
		logger.info("task inserted? " + isSaved + ". TaskUpdatedDTO= " + taskUpdatedDTO);
		return ResponseEntity.status(HttpStatus.OK).body(taskUpdatedDTO);
	}

	@DeleteMapping(path = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteTask(@RequestBody TaskDTO taskDTO) {

		Task task = TaskDTO.getTask(taskDTO);

		if (taskImp.delete(task)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(TaskDeletedDTO.builder().message(
							"Task " + task.getId() + " del usuario " + task.getIdUser() + " eliminada correctamente.")
							.build());
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(TaskDeletedDTO.builder().message(
						"Task " + task.getId() + " del usuario " + task.getIdUser() + " NO eliminada correctamente.")
						.build());
	}

	@GetMapping(path = "/ping")
	public ResponseEntity<Object> ping() {
		logger.info("pong...");
		return ResponseEntity.ok("pong");
	}

	@GetMapping(path = "/listar")
	public ResponseEntity<Object> Listar() {
		logger.info("listing Elements...");
		return ResponseEntity.status(HttpStatus.OK)
				.body(taskImp.listAll().stream().map(e -> TaskDTO.getTaskDTO(e)).collect(Collectors.toList()) );
	}

}
