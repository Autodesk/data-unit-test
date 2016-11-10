package com.autodesk.adp.validation_framework.tests;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.autodesk.adp.validation_framework.db.DBHelper;
import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

public class TestFail {
	private static final Logger LOG = LoggerFactory.getLogger(TestFail.class);
	private DBHelper dbHelper = DBHelper.getInstance();

	@Parameters(value = { "setup-queries", "ignore-failures" })
	@BeforeMethod
	public void setup(String setupQueries, boolean ignoreFailures)
			throws Exception {
		try {
			JSONArray queryArr = new JSONArray(setupQueries);
			for (int i = 0; i < queryArr.length(); i++) {
				LOG.info("Executing query: " + queryArr.getString(i));
				dbHelper.executeAndGetResults(queryArr.getString(i),
						RETURNTYPE.NONE);
			}
		} catch (Exception ex) {
			LOG.error("Exception while setup", ex);
			if (!ignoreFailures)
				throw ex;
		}
	}

	@Parameters(value = { "test-query", "return-type", "return-val" })
	@Test
	public void test(String testQuery, String type, String returnVal)
			throws SQLException, IOException {
		try {
			RETURNTYPE returnType = RETURNTYPE.getReturnType(type);
			if (returnType != RETURNTYPE.EXCEPTION) {
				LOG.error("Unrecognized return type. Only acceptable value is exception. Return type provided is: "
						+ type);
				throw new IllegalArgumentException(
						"Unrecognized return type. Only acceptable value is exception. Return type provided is: "
								+ type);
			}
			Result result = dbHelper
					.executeAndGetResults(testQuery, returnType);
			Assert.assertTrue(result.getException().getMessage().contains(returnVal));
			
		} catch (IllegalArgumentException e) {
			LOG.error("Unrecognized return type mentioned", e);
			throw e;
		} catch (SQLException e) {
			LOG.error("SQLException while executing query: " + testQuery, e);
			throw e;
		} catch (IOException e) {
			LOG.error("IOException while executing query: " + testQuery, e);
			throw e;
		}
	}

	@Parameters(value = { "teardown-queries", "ignore-failures" })
	@AfterMethod
	public void teardown(String teardownQueries, boolean ignoreFailures)
			throws Exception {
		try {
			JSONArray queryArr = new JSONArray(teardownQueries);
			for (int i = 0; i < queryArr.length(); i++) {
				LOG.info("Executing query: " + queryArr.getString(i));
				dbHelper.executeAndGetResults(queryArr.getString(i),
						RETURNTYPE.NONE);
			}
		} catch (Exception ex) {
			LOG.error("Exception while teardown", ex);
			if (!ignoreFailures)
				throw ex;
		}
	}

}
