package com.protalento.usuarios.jdbc.implementations;

import java.math.BigInteger;
import java.sql.Types;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.protalento.jdbc.DAO;
import com.protalento.jdbc.DataSourceSubclassGenerator;
import com.protalento.jdbc.enums.Base64Schema;
import com.protalento.jdbc.utilities.Base64EncoderDecoder;
import com.protalento.usuarios.entities.Company;
import com.protalento.usuarios.entities.Direccion;
import com.protalento.usuarios.entities.Geolocalizacion;
import com.protalento.usuarios.entities.Usuario;
import com.protalento.usuarios.jdbc.mapper.UsuarioMapper;

public class UsuarioImp implements DAO<Usuario, Integer> {
	private static final Logger logger = LogManager.getLogger();

	private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(
			DataSourceSubclassGenerator.getdriDriverManagerDataSource());

	@Override
	public Usuario findById(Integer key) {
		String sql = "SELECT id, clave, nombre, usuario, correo, telefono, pagina, razon_social, area, calle, ciudad, codigo_postal, latitude, longitude\r\n"
				+ "FROM usuarios where id = ?";

		try {
			return jdbcTemplate.queryForObject(sql, new UsuarioMapper(), key);
		} catch (Exception e) {
			logger.error("no existing id. Error is:" + e);
		}

		return null;
	}

	@Override
	public Boolean insert(Usuario usuario) {
		String sql = "INSERT INTO usuarios\r\n"
				+ "(clave, nombre, usuario, correo, telefono, pagina, razon_social, area, calle, ciudad, codigo_postal, latitude, longitude)\r\n"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

		PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(sql,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);

		usuario.setClave(getCodedRandom10DigitsValue());
		Company company = usuario.getCompany();
		Direccion direccion = usuario.getDireccion();
		Geolocalizacion geolocalizacion = direccion.getGeolocalizacion();
		Object[] parameters = new Object[] { usuario.getClave(), usuario.getNombre(), usuario.getUsuario(),
				usuario.getCorreo(), usuario.getTelefono(), usuario.getPagina(), company.getRazon_social(),
				company.getArea(), direccion.getCalle(), direccion.getCiudad(), direccion.getCodigo_postal(),
				geolocalizacion.getLatitude(), geolocalizacion.getLongitude() };

		preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
		preparedStatementCreatorFactory.setGeneratedKeysColumnNames("id");
		PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory
				.newPreparedStatementCreator(parameters);

		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

		byte insertedRows = (byte) jdbcTemplate.update(preparedStatementCreator, generatedKeyHolder);

		boolean inserted = insertedRows == 1;
		logger.info("was inserted? :" + inserted + ". " + usuario);
		
		logger.info("generated keys are:" +  generatedKeyHolder.getKeys());
 
		
		
//		usuario.setId(((BigInteger) generatedKeyHolder.getKeys().get("insert_id")).intValue());
		logger.info("insertion with generate key? :" + inserted + ". " + usuario);
		return inserted;
	}

	@Override
	public Boolean update(Usuario usuario) {
		String sql = "UPDATE usuarios\r\n"
				+ "SET clave= ?, nombre= ?, usuario= ?, correo= ?, telefono= ?, pagina= ?, razon_social= ?, area= ?, calle= ?, ciudad= ?, codigo_postal= ?, latitude= ?, longitude= ?\r\n"
				+ "WHERE id = ?";
		
		Company company = usuario.getCompany();
		Direccion direccion = usuario.getDireccion();
		Geolocalizacion geolocalizacion = direccion.getGeolocalizacion();
		
		int updatedRows = jdbcTemplate.update(sql, usuario.getClave(), usuario.getNombre(), usuario.getUsuario(),
				usuario.getCorreo(), usuario.getTelefono(), usuario.getPagina(), company.getRazon_social(),
				company.getArea(), direccion.getCalle(), direccion.getCiudad(), direccion.getCodigo_postal(),
				geolocalizacion.getLatitude(), geolocalizacion.getLongitude(), usuario.getId());

		boolean updated = updatedRows == 1;
		logger.info("was updated? :" + updated + ". " + usuario);
		return updated;
	}

	@Override
	public Boolean save(Usuario usuario) {
		boolean modified;

		if (findById(usuario.getId()) == null) {
			modified = insert(usuario);
		} else {
			modified = update(usuario);
		}
		logger.info("was saved? :" + modified + ". " + usuario);
		return modified;
	}

	@Override
	public Boolean delete(Usuario usuario) {
		String sql = "DELETE FROM usuarios WHERE id = ?";

		int deletedRows = jdbcTemplate.update(sql, usuario.getId());

		boolean deleted = deletedRows == 1;
		logger.info("Deleted element? : " + deleted + ". Usuario = " + usuario);
		return deleted;
	}

	@Override
	public List<Usuario> listAll() {
		String query = "SELECT id, clave, nombre, usuario, correo, telefono, pagina, razon_social, area, calle, ciudad, codigo_postal, latitude, longitude\r\n"
				+ "FROM usuarios;";

		List<Usuario> UsuarioList = jdbcTemplate.query(query, new UsuarioMapper());
		logger.info("Listing all Usuarios: " + UsuarioList);
		return UsuarioList;
	}

	private String getCodedRandom10DigitsValue() {
		double randomValue = Math.random();
		Long random10DigitsValue = Math.round(10000000000L * randomValue);

		String coded = Base64EncoderDecoder.operateBase64Schema(String.valueOf(random10DigitsValue),
				Base64Schema.ENCODE);

		return coded.replace("==", "");
	}

	public static void main(String[] args) {
//		double randomValue = Math.random();
//		Long random10DigitsValue = Math.round(10000000000L * randomValue);
//
//		String coded = Base64EncoderDecoder.operateBase64Schema(String.valueOf(random10DigitsValue),
//				Base64Schema.ENCODE);
//		System.out.println("value= " + random10DigitsValue + ". coded = " + coded);
		
		UsuarioImp usuarioImp = new UsuarioImp();
		
		
		/** Inserting **/ 
//		Company company = Company.builder().razon_social("octavio robleto SA").area("Sistemas")
//				.build();
//
//		Geolocalizacion geolocalizacion = Geolocalizacion.builder().latitude("-37.3159")
//				.longitude("81.1496").build();
//
//		Direccion direccion = Direccion.builder().calle("Simon Bolivar").ciudad("Bogota")
//				.codigo_postal("92998").geolocalizacion(geolocalizacion).build();
//
//		Usuario user = Usuario.builder().nombre("octavio robleto T").usuario("octaviorobleto").correo("octaviorobleto@gmail.com")
//				.telefono("91150124479").pagina("https://octaviorobleto.com/").company(company).direccion(direccion).build();
	
		//inserting element
//		usuarioImp.save(user);
		
		
		/** Updating **/ 
//		Company company = Company.builder().razon_social("octavio robleto SA").area("Sistemas")
//				.build();
//
//		Geolocalizacion geolocalizacion = Geolocalizacion.builder().latitude("-37.3159")
//				.longitude("81.1496").build();
//
//		Direccion direccion = Direccion.builder().calle("Simon Bolivar").ciudad("Bogota")
//				.codigo_postal("92998").geolocalizacion(geolocalizacion).build();
//
//		Usuario user = Usuario.builder().id(1).clave("MTczMjE5MzUyOQ==").nombre("octavio robleto T").usuario("octaviorobleto").correo("octaviorobleto@gmail.com")
//				.telefono("91150124479").pagina("https://octaviorobleto.com/").company(company).direccion(direccion).build();
//		
//		usuarioImp.save(user);
		
		
		/** Deleting **/ 
//		Company company = Company.builder().razon_social("octavio robleto SA").area("Sistemas")
//				.build();
//
//		Geolocalizacion geolocalizacion = Geolocalizacion.builder().latitude("-37.3159")
//				.longitude("81.1496").build();
//
//		Direccion direccion = Direccion.builder().calle("Simon Bolivar").ciudad("Bogota")
//				.codigo_postal("92998").geolocalizacion(geolocalizacion).build();
//
//		Usuario user = Usuario.builder().id(2).clave("MTczMjE5MzUyOQ==").nombre("octavio robleto T").usuario("octaviorobleto").correo("octaviorobleto@gmail.com")
//				.telefono("91150124479").pagina("https://octaviorobleto.com/").company(company).direccion(direccion).build();
//		
//		usuarioImp.delete(user);
		
		
		/** Listing All **/ 
		Company company = Company.builder().razon_social("octavio robleto SA").area("Sistemas")
				.build();

		Geolocalizacion geolocalizacion = Geolocalizacion.builder().latitude("-37.3159")
				.longitude("81.1496").build();

		Direccion direccion = Direccion.builder().calle("Simon Bolivar").ciudad("Bogota")
				.codigo_postal("92998").geolocalizacion(geolocalizacion).build();

		Usuario user = Usuario.builder().id(2).clave("MTczMjE5MzUyOQ==").nombre("octavio robleto T").usuario("octaviorobleto").correo("octaviorobleto@gmail.com")
				.telefono("91150124479").pagina("https://octaviorobleto.com/").company(company).direccion(direccion).build();
		
		usuarioImp.listAll();		
	
	}

}
