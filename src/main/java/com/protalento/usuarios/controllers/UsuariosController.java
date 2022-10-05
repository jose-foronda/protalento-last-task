package com.protalento.usuarios.controllers;

import java.util.Objects;

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

import com.protalento.entities.dtos.TaskDeletedDTO;
import com.protalento.services.entities.ServiceErrorsPool;
import com.protalento.services.enums.ServiceStatusType;
import com.protalento.usuarios.entities.Usuario;
import com.protalento.usuarios.entities.dtos.UsuarioDTO;
import com.protalento.usuarios.entities.dtos.UsuarioDeletedDTO;
import com.protalento.usuarios.entities.dtos.UsuarioPostedDTO;
import com.protalento.usuarios.entities.dtos.UsuarioPutRequestDTO;
import com.protalento.usuarios.entities.dtos.UsuarioPutResponseDTO;
import com.protalento.usuarios.jdbc.implementations.UsuarioImp;

@RestController
@RequestMapping(path = "/usuarios")
public class UsuariosController {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private UsuarioImp usuarioImp;

	@GetMapping(path = "/{id}")
	public ResponseEntity<Object> getUsuarioById(@PathVariable int id) {
		Usuario usuario = usuarioImp.findById(id);

		if (Objects.isNull(usuario)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ServiceErrorsPool
					.getServiceError(ServiceStatusType.ELEMENT_NOT_FOUND, "Id= " + id + " no existe."));
		}

		logger.info("UsuariosController Controller. method getUsuarioById ... " + id + ". Usuario: " + usuario);

		return ResponseEntity.status(HttpStatus.OK).body(usuario);
	}

	@PostMapping(path = "/agregar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> postUsuario(@RequestBody UsuarioDTO usuarioDTO) {

		Usuario usuario = UsuarioDTO.getUsuario(usuarioDTO);

		boolean isSaved = usuarioImp.save(usuario);

		UsuarioPostedDTO usuarioPostedDTO;

		if (isSaved) {
			logger.info("Usuario received as JSON through HTTP post." + "Usuario = " + usuario);

			usuarioPostedDTO = UsuarioPostedDTO.builder().id(usuario.getId()).clave(usuario.getClave())
					.mensaje("Usuario creado correctamente.").build();
			logger.info("task inserted? " + isSaved + ". Usuario = " + usuarioPostedDTO);

			return ResponseEntity.status(HttpStatus.OK).body(usuarioPostedDTO);
		}

		logger.info("Usuario received as JSON through HTTP post." + "Usuario = " + usuario);
		usuarioPostedDTO = UsuarioPostedDTO.builder().mensaje("Usuario NO creado correctamente.").build();
		logger.info("task inserted? " + isSaved + ". Usuario = " + usuarioPostedDTO);
		return ResponseEntity.status(HttpStatus.OK).body(usuarioPostedDTO);
	}

	@PutMapping(path = "/modificar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> putUsuario(@RequestBody UsuarioPutRequestDTO usuarioPutRequestDTO) {

		Usuario usuario = UsuarioPutRequestDTO.getUsuarioFromDTO(usuarioPutRequestDTO);

		boolean isSaved = usuarioImp.save(usuario);
		if (!isSaved) {
			return ResponseEntity.status(HttpStatus.OK).body(
					ServiceErrorsPool.getServiceError(ServiceStatusType.NO_UPDATED, " No pudo actualizar elemento."));
		}

		UsuarioPutResponseDTO usuarioPutResponseDTO = UsuarioPutResponseDTO.getUsuariaPutResponseDTOFromUsuario(usuario);
		usuarioPutResponseDTO.setMensaje("datos actualizados correctamente.");

		logger.info("usuarioPutRequestDTO received as JSON through HTTP post." + "usuarioPutRequestDTO = " + usuarioPutRequestDTO);
		logger.info("Usuario updated? " + isSaved + ". usuarioPutRequestDTO= " + usuarioPutRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(usuarioPutResponseDTO);
	}
	
	@DeleteMapping(path = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteUsuario(@RequestBody Usuario usuario) {
		
		
		if (usuarioImp.delete(usuario)) {
			return ResponseEntity.status(HttpStatus.OK).body(UsuarioDeletedDTO.builder().message("Usuario " + usuario.getId() + " eliminado correctamente.").build());
		}
		return ResponseEntity.status(HttpStatus.OK).body(TaskDeletedDTO.builder().message("Usuario " + usuario.getId() + " NO eliminado correctamente.").build());
	}
	
	@GetMapping(path = "/listar")
	public ResponseEntity<Object> Listar() {
		logger.info("listing Elements..."); 
		return ResponseEntity.status(HttpStatus.OK).body(usuarioImp.listAll());
	}
	
	@GetMapping(path = "/ping")
	public ResponseEntity<Object> ping() {
		logger.info("pong...");
		return ResponseEntity.ok("pong");
	}
}
