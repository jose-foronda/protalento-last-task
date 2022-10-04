package com.protalento.usuarios.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.protalento.usuarios.entities.Company;
import com.protalento.usuarios.entities.Direccion;
import com.protalento.usuarios.entities.Geolocalizacion;
import com.protalento.usuarios.entities.Usuario;

public class UsuarioMapper implements RowMapper<Usuario> {
	private static final Logger logger = LogManager.getLogger();

	@Override
	public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {

		try {

			int id = rs.getInt("id");
			String clave = rs.getString("clave");
			String nombre = rs.getString("nombre");
			String usuario = rs.getString("usuario");
			String correo = rs.getString("correo");
			String telefono = rs.getString("telefono");
			String pagina = rs.getString("pagina");

			Company company = Company.builder().razon_social(rs.getString("razon_social")).area(rs.getString("area"))
					.build();

			Geolocalizacion geolocalizacion = Geolocalizacion.builder().latitude(rs.getString("latitude"))
					.longitude(rs.getString("longitude")).build();

			Direccion direccion = Direccion.builder().calle(rs.getString("calle")).ciudad(rs.getString("ciudad"))
					.codigo_postal(rs.getString("codigo_postal")).geolocalizacion(geolocalizacion).build();

			Usuario user = Usuario.builder().id(id).clave(clave).nombre(nombre).usuario(usuario).correo(correo)
					.telefono(telefono).pagina(pagina).company(company).direccion(direccion).build();

			logger.info("ResultSet result---> + " + user);
			return user;
		} catch (Exception e) {
			logger.error("Error trying to map a ResultSet row to a Usuario object. " + e);
			return null;
		}

	}
	
}
