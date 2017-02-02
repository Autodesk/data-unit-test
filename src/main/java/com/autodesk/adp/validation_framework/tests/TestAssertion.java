package com.autodesk.adp.validation_framework.tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.autodesk.adp.validation_framework.assertions.Assertions;
import com.autodesk.adp.validation_framework.assertions.CsvAssertions;
import com.autodesk.adp.validation_framework.assertions.ExceptionAssertions;
import com.autodesk.adp.validation_framework.assertions.JSONAssertions;
import com.autodesk.adp.validation_framework.db.DBHelper;
import com.autodesk.adp.validation_framework.utils.ASSERTTYPE;
import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

public class TestAssertion implements ITest {
	private static final Logger LOG = LoggerFactory
			.getLogger(TestAssertion.class);
	private static final DBHelper dbHelper = DBHelper.getInstance();

	private boolean doSetup;
	private boolean doTearDown;
	private boolean ignoreFailures;
	private ArrayList<String> setupQueries;
	private ArrayList<String> tearDownQueries;
	private String testQuery;
	private RETURNTYPE returnType;
	private ASSERTTYPE assertType;
	private Object returnVal;
	private Connection con;
	private final String name;

	public TestAssertion(boolean doSetup, boolean doTearDown,
			boolean ignoreFailures, ArrayList<String> setupQueries,
			ArrayList<String> tearDownQueries, String testQuery,
			RETURNTYPE returnType, ASSERTTYPE assertType, Object returnVal,
			String name) throws SQLException {
		this.doSetup = doSetup;
		this.doTearDown = doTearDown;
		this.ignoreFailures = ignoreFailures;
		this.setupQueries = setupQueries;
		this.tearDownQueries = tearDownQueries;
		this.testQuery = testQuery;
		this.returnType = returnType;
		this.assertType = assertType;
		this.returnVal = returnVal;
		this.name = name;
	}

	public void setup() throws Exception {
		LOG.info("setup queries provided: " + setupQueries);
		try {
			JSONArray queryArr = new JSONArray(setupQueries);
			for (int i = 0; i < queryArr.length(); i++) {
				LOG.info("Executing query: " + queryArr.getString(i));
				dbHelper.executeAndGetResults(queryArr.getString(i),
						RETURNTYPE.NONE, con);
			}
		} catch (Exception ex) {
			LOG.error("Exception while setup", ex);
			if (!ignoreFailures)
				throw ex;
		}
	}

	@Test()
	public void test() throws Exception {
		try {
			this.con = dbHelper.getConnection();
			if (doSetup)
				setup();
			Assertions assertion = null;
			boolean assertionResult = false;
			switch (returnType) {
			case CSV:
				assertion = new CsvAssertions();
				break;
			case LIST:
			case MAP:
				assertion = new JSONAssertions();
				break;
			case EXCEPTION:
				assertion = new ExceptionAssertions();
				break;
			default:
				break;
			}
			Result result = dbHelper.executeAndGetResults(testQuery,
					returnType, con);

			switch (assertType) {
			case assert_equals:
				assertionResult = assertion.assertEquals(returnVal, result);
				break;
			case assert_excludes:
				assertionResult = assertion.assertExcludes(returnVal, result);
				break;
			case assert_fails:
				assertionResult = assertion.assertFails(returnVal, result);
				break;
			case assert_includes:
				assertionResult = assertion.assertIncludes(returnVal, result);
				break;
			case assert_ordered_equals:
				assertionResult = assertion.assertOrderedEquals(returnVal,
						result);
				break;
			default:
				break;
			}
			Assert.assertTrue(assertionResult);
			if (doTearDown)
				teardown();
		} catch (Exception e) {
			LOG.error("Exception while running test", e);
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public void teardown() throws Exception {
		LOG.info("teardown queries provided: " + tearDownQueries);
		try {
			JSONArray queryArr = new JSONArray(tearDownQueries);
			for (int i = 0; i < queryArr.length(); i++) {
				LOG.info("Executing query: " + queryArr.getString(i));
				dbHelper.executeAndGetResults(queryArr.getString(i),
						RETURNTYPE.NONE, con);
			}
		} catch (Exception ex) {
			LOG.error("Exception while teardown", ex);
			if (!ignoreFailures)
				throw ex;
		}
	}

	@Override
	public String toString() {
		return "TestAssertion [doSetup=" + doSetup + ", doTearDown="
				+ doTearDown + ", ignoreFailures=" + ignoreFailures
				+ ", setupQueries=" + setupQueries + ", tearDownQueries="
				+ tearDownQueries + ", testQuery=" + testQuery
				+ ", returnType=" + returnType + ", assertType=" + assertType
				+ ", returnVal=" + returnVal + "]";
	}

	public String getTestName() {
		return name;
	}

}
