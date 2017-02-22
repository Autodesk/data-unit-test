package com.autodesk.adp.validation_framework.utils;

/**
 * The Enum TEST_CONSTANTS defines different command line options used by the framework.
 */
public enum TEST_CONSTANTS {

	/** The yml file name containing all test cases. */
	YML_FILE,

	/** The log4j.properties file to be used for logging purpose. If not specified by user, framework will log on console. */
	LOGGER,

	/** The db url to connect to. */
	DB_URL,

	/** The user name to use while connecting to database. */
	USER_NAME,

	/** The user password to use while connecting to database. */
	USER_PASSWORD,

	/** The connection idle timeout after which the connection in the connection pool will be discarded. */
	CONN_IDLE_TIMEOUT,

	/** The number of active connections that are always maintained in the connection pool. */
	ACTIVE_CONN_COUNT,

	/** The jdbc driver to use while connecting to the database. */
	JDBC_DRIVER;

}
