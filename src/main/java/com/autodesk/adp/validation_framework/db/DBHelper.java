package com.autodesk.adp.validation_framework.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.hive.service.cli.Type;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;
import com.autodesk.adp.validation_framework.utils.TEST_CONSTANTS;

public class DBHelper {

	private static final String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	private static final Logger LOG = LoggerFactory.getLogger(DBHelper.class);

	private static final DBHelper dbHelper = new DBHelper();
	private static final String DEF_IDLE_TIMEOUT = "1";
	private static final String DEF_NUM_ACTIVE_CONN = "5";
	private BasicDataSource ds;

	private DBHelper() {
		if (DBHelper.dbHelper != null)
			throw new InstantiationError(
					"Creating of this object is not allowed.");
		ds = new BasicDataSource();
		ds.setDriverClassName(DRIVER_NAME);
		ds.setUsername(System.getProperty(TEST_CONSTANTS.USER_NAME.name()));
		ds.setPassword(System.getProperty(TEST_CONSTANTS.USER_PASSWORD.name()));
		ds.setUrl(System.getProperty(TEST_CONSTANTS.DB_URL.name()));
		ds.setRemoveAbandonedTimeout(Integer.parseInt(System.getProperty(
				TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name(), DEF_IDLE_TIMEOUT)));
		ds.setMaxActive(Integer.parseInt(System.getProperty(
				TEST_CONSTANTS.ACTIVE_CONN_COUNT.name(), DEF_NUM_ACTIVE_CONN)));
	}

	public static DBHelper getInstance() {
		return dbHelper;
	}

	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

	public void closeAll() throws SQLException {
		this.ds.close();
	}

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

	private Result getData(ResultSet rs, ResultSetMetaData metadata,
			RETURNTYPE returnType) throws SQLException, IOException {
		switch (returnType) {
		case LIST:
			JSONArray list = getListOfLists(rs, metadata);
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

	private JSONArray getListOfObjects(ResultSet rs, ResultSetMetaData metadata)
			throws SQLException {
		JSONArray output = new JSONArray();
		int numCols = metadata.getColumnCount();
		while (rs.next()) {
			JSONObject row = new JSONObject();
			for (int i = 1; i <= numCols; i++) {
				Type t = Type.getType(metadata.getColumnTypeName(i));
				switch (t) {
				case ARRAY_TYPE:
					row.put(metadata.getColumnName(i),
							new JSONArray(rs.getString(i)));
					break;
				case BIGINT_TYPE:
					row.put(metadata.getColumnName(i), rs.getLong(i));
					break;
				case BINARY_TYPE:
					row.put(metadata.getColumnName(i), rs.getBytes(i));
					break;
				case BOOLEAN_TYPE:
					row.put(metadata.getColumnName(i), rs.getBoolean(i));
					break;
				case DATE_TYPE:
					row.put(metadata.getColumnName(i), rs.getDate(i));
					break;
				case DECIMAL_TYPE:
					row.put(metadata.getColumnName(i), rs.getLong(i));
					break;
				case DOUBLE_TYPE:
					row.put(metadata.getColumnName(i), rs.getDouble(i));
					break;
				case FLOAT_TYPE:
					row.put(metadata.getColumnName(i), rs.getFloat(i));
					break;
				case INT_TYPE:
					row.put(metadata.getColumnName(i), rs.getInt(i));
					break;
				case CHAR_TYPE:
				case VARCHAR_TYPE:
				case STRUCT_TYPE:
				case UNION_TYPE:
				case STRING_TYPE:
					row.put(metadata.getColumnName(i), rs.getString(i));
					break;
				case MAP_TYPE:
					row.put(metadata.getColumnName(i),
							new JSONObject(rs.getString(i)));
					break;
				case SMALLINT_TYPE:
					row.put(metadata.getColumnName(i), rs.getShort(i));
					break;
				case TIMESTAMP_TYPE:
					row.put(metadata.getColumnName(i), rs.getTimestamp(i));
					break;
				case TINYINT_TYPE:
					row.put(metadata.getColumnName(i), rs.getByte(i));
					break;
				default:
					break;
				}
			}
			output.put(row);
		}
		return output;
	}

	private JSONArray getListOfLists(ResultSet rs, ResultSetMetaData metadata)
			throws SQLException {
		JSONArray output = new JSONArray();
		int numCols = metadata.getColumnCount();
		while (rs.next()) {
			JSONArray row = new JSONArray();
			for (int i = 1; i <= numCols; i++) {
				Type t = Type.getType(metadata.getColumnTypeName(i));
				switch (t) {
				case ARRAY_TYPE:
					row.put(new JSONArray(rs.getString(i)));
					break;
				case BIGINT_TYPE:
					row.put(rs.getLong(i));
					break;
				case BINARY_TYPE:
					row.put(rs.getBytes(i));
					break;
				case BOOLEAN_TYPE:
					row.put(rs.getBoolean(i));
					break;
				case DATE_TYPE:
					row.put(rs.getDate(i));
					break;
				case DECIMAL_TYPE:
					row.put(rs.getLong(i));
					break;
				case DOUBLE_TYPE:
					row.put(rs.getDouble(i));
					break;
				case FLOAT_TYPE:
					row.put(rs.getFloat(i));
					break;
				case INT_TYPE:
					row.put(rs.getInt(i));
					break;
				case CHAR_TYPE:
				case VARCHAR_TYPE:
				case STRUCT_TYPE:
				case UNION_TYPE:
				case STRING_TYPE:
					row.put(rs.getString(i));
					break;
				case MAP_TYPE:
					row.put(new JSONObject(rs.getString(i)));
					break;
				case SMALLINT_TYPE:
					row.put(rs.getShort(i));
					break;
				case TIMESTAMP_TYPE:
					row.put(rs.getTimestamp(i));
					break;
				case TINYINT_TYPE:
					row.put(rs.getByte(i));
					break;
				default:
					break;
				}
			}
			output.put(row);
		}
		return output;
	}

}
