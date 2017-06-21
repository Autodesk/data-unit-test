package com.autodesk.adp.validation_framework.tests;

import java.sql.Connection;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.autodesk.adp.validation_framework.assertions.Assertions;
import com.autodesk.adp.validation_framework.assertions.CsvAssertions;
import com.autodesk.adp.validation_framework.assertions.ExceptionAssertions;
import com.autodesk.adp.validation_framework.assertions.ListAssertions;
import com.autodesk.adp.validation_framework.db.DBHelper;
import com.autodesk.adp.validation_framework.utils.ASSERTTYPE;
import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;
import com.google.common.annotations.VisibleForTesting;

/**
 * The Class TestAssertion. Each object of this class corresponds to one test
 * case. This object executes the setup queries if present, executes the actual
 * test query whose result it uses to compare with the expected value and then
 * runs the teardown queries if provided. The test case succeeds if the
 * comparison that this class performs passes, else it fails.
 */
public class TestAssertion implements ITest {

	/** The Constant LOG. Used for logging. */
	private static final Logger LOG = LoggerFactory
			.getLogger(TestAssertion.class);

	/** The db helper instance used for exeuting queries. */
	private static DBHelper dbHelper = DBHelper.getInstance();

	/** The do setup flag specifies if setup queries should be executed or not. */
	private boolean doSetup;

	/**
	 * The do tear down flag specifies if tear down queries should be executed
	 * or not.
	 */
	private boolean doTearDown;

	/**
	 * The ignore failures flag specifies if exceptions during setup and tear
	 * down can be ignored or not.
	 */
	private boolean ignoreFailures;

	/** The setup queries. */
	private ArrayList<String> setupQueries;

	/** The tear down queries. */
	private ArrayList<String> tearDownQueries;

	/** The actual test query. */
	private String testQuery;

	/** The return type. */
	private RETURNTYPE returnType;

	/** The assert type. */
	private ASSERTTYPE assertType;

	/** The expected return value. */
	private Object returnVal;

	/** The SQL connection used to execute this test case. */
	private Connection con;

	/** The name of the test case. */
	private final String name;

	/**
	 * Instantiates a new test assertion. This object contains all information
	 * provided in one test case as present in the YAML file.
	 *
	 * @param doSetup
	 *            Flag to specify whether setup is required for this test case
	 *            or not.
	 * @param doTearDown
	 *            Flag to specify whether tear down is required for this test
	 *            case or not.
	 * @param ignoreFailures
	 *            Flag to specify whether errors during setup and tear down
	 *            phase are to be ignored of not.
	 * @param setupQueries
	 *            the setup queries.
	 * @param tearDownQueries
	 *            the tear down queries.
	 * @param testQuery
	 *            the test query that is supposed to generate actual output.
	 * @param returnType
	 *            the return type.
	 * @param assertType
	 *            the assert type.
	 * @param returnVal
	 *            the expected return value as specified in test case.
	 * @param name
	 *            the name of the test case.
	 */
	public TestAssertion(boolean doSetup, boolean doTearDown,
			boolean ignoreFailures, ArrayList<String> setupQueries,
			ArrayList<String> tearDownQueries, String testQuery,
			RETURNTYPE returnType, ASSERTTYPE assertType, Object returnVal,
			String name) {
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

	/**
	 * Setup. This is used if setup queries are provided. This method executes
	 * the queries provided in the setup before the test case is executed.
	 *
	 * @throws Exception
	 *             Signals that an Exception has occurred while executing setup
	 *             queries.
	 */
	public void setup() throws Exception {
		LOG.info("setup queries provided: " + setupQueries);
		try {
			for (String setupQry : setupQueries) {
				LOG.info("Executing query: " + setupQry);
				dbHelper.executeAndGetResults(setupQry,
						RETURNTYPE.NONE, con);
			}
		} catch (Exception ex) {
			LOG.error("Exception while setup", ex);
			if (!ignoreFailures)
				throw ex;
		}
	}

	/**
	 * Gets the assertion type.
	 *
	 * @return the assertion type
	 */
	@VisibleForTesting
	protected Assertions getAssertionType() {
		switch (returnType) {
		case CSV:
			return new CsvAssertions();
		case LIST:
		case MAP:
			return new ListAssertions();
		case EXCEPTION:
			return new ExceptionAssertions();
		default:
			throw new IllegalArgumentException("Unrecognized return type: "
					+ returnType);
		}
	}

	/**
	 * Execute assertion. Depending upon the return type and the assertion type
	 * provided in the test case, executes appropriate assertion.
	 *
	 * @param assertion
	 *            the assertion, can be one of any specified in
	 *            {@link ASSERTTYPE}
	 * @param result
	 *            the result obtained by executing the query.
	 * @return true, if successful, false otherwise.
	 * @throws Exception
	 *             Signals that an Exception occurred while executing the
	 *             appropriate assertion.
	 */
	@VisibleForTesting
	protected boolean executeAssertion(Assertions assertion, Result result)
			throws Exception {
		switch (assertType) {
		case assert_equals:
			return assertion.assertEquals(returnVal, result);
		case assert_excludes:
			return assertion.assertExcludes(returnVal, result);
		case assert_fails:
			return assertion.assertFails(returnVal, result);
		case assert_includes:
			return assertion.assertIncludes(returnVal, result);
		case assert_ordered_equals:
			return assertion.assertOrderedEquals(returnVal, result);
		default:
			throw new IllegalArgumentException("Un recognized assert type: "
					+ assertType);
		}
	}

	/**
	 * The main method that executes the test case. Gets the SQL connection from
	 * the connection pool, executes the setup queries if provided, then runs
	 * the actual test query, gets the output and runs assertions on the output.
	 * Post that it runs the tear down queries if provided. In the end, it
	 * closes the connection.
	 *
	 * @throws Exception
	 *             Signals that an Exception occurred while running the test
	 *             case.
	 */
	@Test()
	public void test() throws Exception {
		try {
			this.con = dbHelper.getConnection();
			if (doSetup)
				setup();
			Assertions assertion = getAssertionType();
			Result result = dbHelper.executeAndGetResults(testQuery,
					returnType, con);
			Assert.assertTrue(executeAssertion(assertion, result));
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

	/**
	 * Teardown. This is used if tear down queries are provided. This method
	 * executes the queries provided in the tear down after the test case is
	 * executed.
	 *
	 * @throws Exception
	 *             Signals that an Exception has occurred while executing tear
	 *             down queries.
	 */
	public void teardown() throws Exception {
		LOG.info("teardown queries provided: " + tearDownQueries);
		try {
			for (String teardownQry : tearDownQueries) {
				LOG.info("Executing query: " + teardownQry);
				dbHelper.executeAndGetResults(teardownQry,
						RETURNTYPE.NONE, con);
			}
		} catch (Exception ex) {
			LOG.error("Exception while teardown", ex);
			if (!ignoreFailures)
				throw ex;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestAssertion [doSetup=" + doSetup + ", doTearDown="
				+ doTearDown + ", ignoreFailures=" + ignoreFailures
				+ ", setupQueries=" + setupQueries + ", tearDownQueries="
				+ tearDownQueries + ", testQuery=" + testQuery
				+ ", returnType=" + returnType + ", assertType=" + assertType
				+ ", returnVal=" + returnVal + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITest#getTestName()
	 */
	public String getTestName() {
		return name;
	}

}
