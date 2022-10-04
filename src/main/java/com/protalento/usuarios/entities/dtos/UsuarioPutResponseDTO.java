package com.protalento.usuarios.entities.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.protalento.usuarios.entities.Company;
import com.protalento.usuarios.entities.Direccion;
import com.protalento.usuarios.entities.Usuario;

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
public class UsuarioPutResponseDTO {
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
	private String mensaje;

	public static UsuarioPutResponseDTO getUsuariaPutResponseDTOFromUsuario(Usuario usuario) {
		return UsuarioPutResponseDTO.builder().id(usuario.getId()).nombre(usuario.getNombre())
				.usuario(usuario.getUsuario()).correo(usuario.getCorreo()).direccion(usuario.getDireccion())
				.telefono(usuario.getTelefono()).pagina(usuario.getPagina()).company(usuario.getCompany()).build();
	}
}
