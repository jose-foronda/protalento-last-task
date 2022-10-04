package com.protalento.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceSubclassGenerator {
	private static final Logger logger = LogManager.getLogger();
	
	public DataSourceSubclassGenerator() {

	}

	public static DriverManagerDataSource getdriDriverManagerDataSource() {

		String driver_db = "org.mariadb.jdbc.Driver";
		String url_db = "jdbc:mariadb://localhost:3306/protalento_final_task";
		String user_db = "root";
		String password_db = "";
		
//		String driver_db = System.getenv("driver_db");
//		String url_db = System.getenv("url_db");
//		String user_db = System.getenv("user_db");
//		String password_db = System.getenv("password_db");
		
		logger.info("Environment Variable is... : " +  System.getenv("driver_db"));

		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(url_db, user_db, password_db);
		driverManagerDataSource.setDriverClassName(driver_db);

		return driverManagerDataSource;
	}
}
