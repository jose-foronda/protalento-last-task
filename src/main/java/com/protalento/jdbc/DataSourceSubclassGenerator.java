package com.protalento.jdbc;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceSubclassGenerator {
	private static final Logger logger = LogManager.getLogger();

	public DataSourceSubclassGenerator() {

	}

	public static DataSource getdriDriverManagerDataSource() {

//		String driver_db = "org.mariadb.jdbc.Driver";
//		String url_db = "jdbc:mariadb://localhost:3306/protalento_final_task";
//		String user_db = "root";
//		String password_db = "";

		String driver_db = System.getenv("driver_db");
		String url_db = System.getenv("url_db");
		String user_db = System.getenv("user_db");
		String password_db = System.getenv("password_db");
		String name_db = System.getenv("name_db");
		int port_db = Integer.valueOf(System.getenv("port_db"));
		String DATABASE_URL = System.getenv("DATABASE_URL");

		logger.info("Environment Variable driver_db is... : " + System.getenv("driver_db"));
		logger.info("Environment Variable url_db is... : " + System.getenv("url_db"));
		logger.info("Environment Variable user_db is... : " + System.getenv("user_db"));
		logger.info("Environment Variable password_db is... : " + System.getenv("password_db"));
		logger.info("Environment Variable DATABASE_URL is... : " + System.getenv("DATABASE_URL"));

		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(url_db, user_db, password_db);
		// for local tests it worked returning driverManagerDataSource but with heroku
		// if failed asking for authentication
		driverManagerDataSource.setDriverClassName(driver_db);

		// Specifically for the connection with the PostgreSQL database located in
		// Heroku
		PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
		pgSimpleDataSource.setDatabaseName(name_db);
		pgSimpleDataSource.setPortNumbers(new int[] { port_db });
		pgSimpleDataSource.setPassword(password_db);
		pgSimpleDataSource.setUrl(url_db);
		pgSimpleDataSource.setUser(user_db);

//		return driverManagerDataSource;
		return pgSimpleDataSource;
	}
}
