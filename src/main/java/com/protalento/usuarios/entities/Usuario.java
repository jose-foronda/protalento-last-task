package com.protalento.usuarios.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Usuario {
	private int id;
	@JsonIgnore
	private String clave;
	private String nombre;
	private String usuario;
	private String correo;
	private Direccion direccion;
	private String telefono;
	private String pagina;
	private Company company; 
	
}
