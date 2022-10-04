package com.protalento.usuarios.entities.dtos;

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
public class UsuarioPutRequestDTO {
	private int id; 
	private String clave;
	private String nombre;
	private String usuario;
	private String correo;
	private Direccion direccion;
	private String telefono;
	private String pagina;
	private Company company;
	private String mensaje;

	public static Usuario getUsuarioFromDTO(UsuarioPutRequestDTO usuarioPutDTO) {
		return Usuario.builder().id(usuarioPutDTO.getId()).clave(usuarioPutDTO.getClave())
				.nombre(usuarioPutDTO.getNombre()).usuario(usuarioPutDTO.getUsuario()).correo(usuarioPutDTO.getCorreo())
				.direccion(usuarioPutDTO.getDireccion()).telefono(usuarioPutDTO.getTelefono())
				.pagina(usuarioPutDTO.getPagina()).company(usuarioPutDTO.getCompany()).build();
	}
}
