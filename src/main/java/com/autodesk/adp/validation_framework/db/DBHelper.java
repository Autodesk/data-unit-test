package com.autodesk.adp.validation_framework.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;
import com.autodesk.adp.validation_framework.utils.TEST_CONSTANTS;

/**
 * The Class DBHelper uses JDBC APIs to execute database related tasks. Is a
 * Singleton and maintains a dbcp connection pool. All database related
 * operations should be carried out using the APIs.
 */
public class DBHelper {

	/** The Constant LOG. Used for logging */
	private static final Logger LOG = LoggerFactory.getLogger(DBHelper.class);

	/** The Constant dbHelper. The singleton instance that is used at all times. */
	private static final DBHelper dbHelper = new DBHelper();

	/**
	 * The Constant DEF_IDLE_TIMEOUT. Used for determining the default idle time
	 * out for the connections in the pool.
	 */
	private static final String DEF_IDLE_TIMEOUT = "1";

	/**
	 * The Constant DEF_NUM_ACTIVE_CONN. Used for determining the default number
	 * of connections to be maintained in the connection pool.
	 */
	private static final String DEF_NUM_ACTIVE_CONN = "5";

	/**
	 * The constant DEV_JDBC_DRIVER. Points to hive jdbc driver. Used in case no
	 * driver is provided.
	 */
	private static final String DEF_JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";

	/** The BasicDataSource object used for creating the connection pool. */
	private BasicDataSource ds;

	/**
	 * Instantiates a new DB helper. The private constructor that will be called
	 * exactly once per JVM. It creates the DBCP connection-pool.
	 */
	private DBHelper() {
		if (DBHelper.dbHelper != null)
			throw new InstantiationError(
					"Creating of this object is not allowed.");
		ds = new BasicDataSource();
		if (System.getProperty(TEST_CONSTANTS.JDBC_DRIVER.name()) == null)
			System.setProperty(TEST_CONSTANTS.JDBC_DRIVER.name(), DEF_JDBC_DRIVER);
		ds.setDriverClassName(System.getProperty(TEST_CONSTANTS.JDBC_DRIVER
				.name()));
		ds.setUsername(System.getProperty(TEST_CONSTANTS.USER_NAME.name()));
		ds.setPassword(System.getProperty(TEST_CONSTANTS.USER_PASSWORD.name()));
		ds.setUrl(System.getProperty(TEST_CONSTANTS.DB_URL.name()));
		ds.setRemoveAbandonedTimeout(Integer.parseInt(System.getProperty(
				TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name(), DEF_IDLE_TIMEOUT)));
		ds.setMaxActive(Integer.parseInt(System.getProperty(
				TEST_CONSTANTS.ACTIVE_CONN_COUNT.name(), DEF_NUM_ACTIVE_CONN)));
	}

	/**
	 * Gets the single instance of DBHelper. Follows Singleton pattern so as to
	 * prevent the DBCP connection pool from being initialized multiple times.
	 *
	 * @return single static final instance of DBHelper.
	 */
	public static DBHelper getInstance() {
		return dbHelper;
	}

	/**
	 * Gets the SQL connection from the connection pool.
	 *
	 * @return one connection from the pool.
	 * @throws SQLException
	 *             Signals that an SQL exception has occurred while fetching SQL
	 *             connection from the connection pool.
	 */
	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

	/**
	 * Close all the connections as well as the connection pool. Should be
	 * called once the program finishes.
	 *
	 * @throws SQLException
	 *             Signals that and SQL exception has occurred while closing the
	 *             connection pool.
	 */
	public void closeAll() throws SQLException {
		this.ds.close();
	}

	/**
	 * Execute query and get results. The query is executed using the connection
	 * and the query provided and the results are converted in appropriate form
	 * using the {@link RETURNTYPE}
	 *
	 * @param query
	 *            the query string that must be executed.
	 * @param returnType
	 *            the return type as specified in the test case.
	 * @param con
	 *            the SQL connection object that must be taken from the
	 *            connection pool earlier using {@link DBHelper#getConnection()}
	 * @return the result in the appropriate form using the results obtained by
	 *         the query and converting them using the expected returnType
	 * @throws SQLException
	 *             Signals that an SQL exception has occurred while executing
	 *             the query.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Result executeAndGetResults(String query, RETURNTYPE returnType,
			Connection con) throws SQLException, IOException {
		LOG.info("Will execute query: " + query);
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			switch (returnType) {
			case EXCEPTION:
				LOG.info("return type is EXCEPTION. Query execution should throw exception."
						+ " Will catch, wrap and return");
				try {
					stmt.execute(query);
				} catch (Exception exception) {
					return new Result(returnType, null, null, exception);
				}
				break;
			case NONE:
				LOG.info("return type is NONE. Will not try to fetch resultset."
						+ " Just execute query and be happy.");
				stmt.execute(query);
				break;
			case LIST:
			case MAP:
			case CSV:
				LOG.info(
						"return type is {}. Will fetch, wrap and return data.",
						returnType.name());
				stmt.execute(query);
				rs = stmt.getResultSet();
				ResultSetMetaData metadata = rs.getMetaData();
				return getData(rs, metadata, returnType);
			default:
				break;
			}
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return null;
	}

	/**
	 * Uses the returnType to generate {@link Result} object containing the
	 * output in appropriate form. Uses the resultset and the metadata obtained
	 * by executing the query and then depending upon the return type, creates
	 * the output.
	 *
	 * @param rs
	 *            the resultset obtained by executing the query.
	 * @param metadata
	 *            the metadata of the resultset.
	 * @param returnType
	 *            the return type as specified in the test case.
	 * @return the result object containing the output in appropriate form.
	 * @throws SQLException
	 *             Signals that SQL exception has occurred while parsing the
	 *             resultset and the corresponding metadata.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred while creating the
	 *             csv file.
	 */
	private Result getData(ResultSet rs, ResultSetMetaData metadata,
			RETURNTYPE returnType) throws SQLException, IOException {
		switch (returnType) {
		case LIST:
			List<Object> list = getListOfLists(rs, metadata);
			return new Result(returnType, list, null, null);
		case MAP:
			list = getListOfObjects(rs, metadata);
			return new Result(returnType, list, null, null);
		case CSV:
			String fileName = UUID.randomUUID().toString() + ".csv";
			File file = new File(fileName);
			CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(metadata);
			try (FileWriter fileWriter = new FileWriter(file);
					CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(
							fileName), csvFormat);) {
				csvPrinter.printRecords(rs);
				return new Result(returnType, null, fileName, null);
			}
		default:
			return null;
		}
	}

	/**
	 * Gets the list of objects from the resultset. Called when the return type
	 * is {@link RETURNTYPE#MAP}. Parses the resultset as well as the metadata
	 * and creates a {@link List} of {@link Map} with keys as the column names and
	 * values as the column values.
	 *
	 * @param rs
	 *            the resultset obtained after executing the query
	 * @param metadata
	 *            the metadata associated with the resultset
	 * @return the List of Map obtained by parsing the resultset.
	 * @throws SQLException
	 *             Signals that SQL exception has occurred while parsing the
	 *             resultset and the corresponding metadata.
	 */
	private List<Object> getListOfObjects(ResultSet rs, ResultSetMetaData metadata)
			throws SQLException {
		List<Object> output = new ArrayList<Object>();
		int numCols = metadata.getColumnCount();
		while (rs.next()) {
			Map<String, Object> row = new HashMap<String, Object>();
			for (int i = 1; i <= numCols; i++) {
				int type = metadata.getColumnType(i);
				switch (type) {
				case Types.ARRAY:
					row.put(metadata.getColumnName(i),
							rs.getArray(i));
					break;
				case Types.BIGINT:
					row.put(metadata.getColumnName(i), rs.getLong(i));
					break;
				case Types.BINARY:
					row.put(metadata.getColumnName(i), rs.getBytes(i));
					break;
				case Types.BOOLEAN:
					row.put(metadata.getColumnName(i), rs.getBoolean(i));
					break;
				case Types.DATE:
					row.put(metadata.getColumnName(i), rs.getDate(i));
					break;
				case Types.DECIMAL:
					row.put(metadata.getColumnName(i), rs.getDouble(i));
					break;
				case Types.DOUBLE:
					row.put(metadata.getColumnName(i), rs.getDouble(i));
					break;
				case Types.FLOAT:
					row.put(metadata.getColumnName(i), rs.getFloat(i));
					break;
				case Types.INTEGER:
					row.put(metadata.getColumnName(i), rs.getInt(i));
					break;
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.STRUCT:
				case Types.OTHER:
					row.put(metadata.getColumnName(i), rs.getString(i));
					break;
				case Types.JAVA_OBJECT:
					row.put(metadata.getColumnName(i),
							rs.getObject(i));
					break;
				case Types.SMALLINT:
					row.put(metadata.getColumnName(i), rs.getShort(i));
					break;
				case Types.TIMESTAMP:
					row.put(metadata.getColumnName(i), rs.getTimestamp(i));
					break;
				case Types.TINYINT:
					row.put(metadata.getColumnName(i), rs.getByte(i));
					break;
				default:
					break;
				}
			}
			output.add(row);
		}
		return output;
	}

	/**
	 * Gets the list of lists from the resultset. Called when the return type is
	 * {@link RETURNTYPE#LIST}. Parses the resultset as well as the metadata and
	 * creates a {@link List} of {@link List} containing the column values.
	 *
	 * @param rs
	 *            the resultset obtained after executing the query
	 * @param metadata
	 *            the metadata associated with the resultset
	 * @return the List of List obtained by parsing the resultset.
	 * @throws SQLException
	 *             Signals that SQL exception has occurred while parsing the
	 *             resultset and the corresponding metadata.
	 */
	private List<Object> getListOfLists(ResultSet rs, ResultSetMetaData metadata)
			throws SQLException {
		List<Object> output = new ArrayList<Object>();
		int numCols = metadata.getColumnCount();
		while (rs.next()) {
			List<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= numCols; i++) {
				int type = metadata.getColumnType(i);
				switch (type) {
				case Types.ARRAY:
					row.add(rs.getArray(i));
					break;
				case Types.BIGINT:
					row.add(rs.getLong(i));
					break;
				case Types.BINARY:
					row.add(rs.getBytes(i));
					break;
				case Types.BOOLEAN:
					row.add(rs.getBoolean(i));
					break;
				case Types.DATE:
					row.add(rs.getDate(i));
					break;
				case Types.DECIMAL:
					row.add(rs.getDouble(i));
					break;
				case Types.DOUBLE:
					row.add(rs.getDouble(i));
					break;
				case Types.FLOAT:
					row.add(rs.getFloat(i));
					break;
				case Types.INTEGER:
					row.add(rs.getInt(i));
					break;
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.STRUCT:
				case Types.OTHER:
					row.add(rs.getString(i));
					break;
				case Types.JAVA_OBJECT:
					row.add(rs.getObject(i));
					break;
				case Types.SMALLINT:
					row.add(rs.getShort(i));
					break;
				case Types.TIMESTAMP:
					row.add(rs.getTimestamp(i));
					break;
				case Types.TINYINT:
					row.add(rs.getByte(i));
					break;
				default:
					break;
				}
			}
			output.add(row);
		}
		return output;
	}

}
