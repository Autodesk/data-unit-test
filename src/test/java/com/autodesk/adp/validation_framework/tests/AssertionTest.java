package com.autodesk.adp.validation_framework.tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.autodesk.adp.validation_framework.assertions.CsvAssertions;
import com.autodesk.adp.validation_framework.assertions.ExceptionAssertions;
import com.autodesk.adp.validation_framework.assertions.ListAssertions;
import com.autodesk.adp.validation_framework.db.DBHelper;
import com.autodesk.adp.validation_framework.utils.ASSERTTYPE;
import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

@PowerMockIgnore("org.apache.log4j.*")
@PrepareForTest(DBHelper.class)
public class AssertionTest extends PowerMockTestCase {

	@Mock
	DBHelper dbHelper;

	@BeforeClass
	public void setup() {
		PowerMockito.mockStatic(DBHelper.class);
		PowerMockito.when(DBHelper.getInstance()).thenReturn(dbHelper);
	}

	@Test(expectedExceptions = { SQLException.class })
	public void setupFailWithException() throws Exception {
		ArrayList<String> setupQueries = new ArrayList<String>();
		setupQueries.add("SHOW GRANT");
		TestAssertion assertion = new TestAssertion(true, false, false,
				setupQueries, null, null, null, null, null, "name");
		PowerMockito.field(TestAssertion.class, "dbHelper").set(assertion,
				dbHelper);
		PowerMockito.when(
				dbHelper.executeAndGetResults(Mockito.anyString(),
						Mockito.any(RETURNTYPE.class),
						Mockito.any(Connection.class))).thenThrow(
				new SQLException());
		assertion.setup();
	}

	@Test
	public void setupIgnoreException() throws Exception {
		ArrayList<String> setupQueries = new ArrayList<String>();
		setupQueries.add("SHOW GRANT");
		TestAssertion assertion = new TestAssertion(true, false, true,
				setupQueries, null, null, null, null, null, "name");
		PowerMockito.field(TestAssertion.class, "dbHelper").set(assertion,
				dbHelper);
		PowerMockito.when(
				dbHelper.executeAndGetResults(Mockito.anyString(),
						Mockito.any(RETURNTYPE.class),
						Mockito.any(Connection.class))).thenThrow(
				new SQLException());
		assertion.setup();
	}

	@Test
	public void setupSuccess() throws Exception {
		ArrayList<String> setupQueries = new ArrayList<String>();
		setupQueries.add("SHOW GRANT");
		TestAssertion assertion = new TestAssertion(true, false, true,
				setupQueries, null, null, null, null, null, "name");
		PowerMockito.field(TestAssertion.class, "dbHelper").set(assertion,
				dbHelper);
		PowerMockito.when(
				dbHelper.executeAndGetResults(Mockito.anyString(),
						Mockito.any(RETURNTYPE.class),
						Mockito.any(Connection.class))).thenReturn(
				new Result(null, null, null, null));
		assertion.setup();
	}

	@Test(expectedExceptions = { SQLException.class })
	public void tearDownFailWithException() throws Exception {
		ArrayList<String> tearDownQueries = new ArrayList<String>();
		tearDownQueries.add("SHOW GRANT");
		TestAssertion assertion = new TestAssertion(false, true, false, null,
				tearDownQueries, null, null, null, null, "name");
		PowerMockito.field(TestAssertion.class, "dbHelper").set(assertion,
				dbHelper);
		PowerMockito.when(
				dbHelper.executeAndGetResults(Mockito.anyString(),
						Mockito.any(RETURNTYPE.class),
						Mockito.any(Connection.class))).thenThrow(
				new SQLException());
		assertion.teardown();
	}

	@Test
	public void tearDownIgnoreException() throws Exception {
		ArrayList<String> tearDownQueries = new ArrayList<String>();
		tearDownQueries.add("SHOW GRANT");
		TestAssertion assertion = new TestAssertion(false, true, true, null,
				tearDownQueries, null, null, null, null, "name");
		PowerMockito.field(TestAssertion.class, "dbHelper").set(assertion,
				dbHelper);
		PowerMockito.when(
				dbHelper.executeAndGetResults(Mockito.anyString(),
						Mockito.any(RETURNTYPE.class),
						Mockito.any(Connection.class))).thenThrow(
				new SQLException());
		assertion.teardown();
	}

	@Test
	public void tearDownSuccess() throws Exception {
		ArrayList<String> teardownQueries = new ArrayList<String>();
		teardownQueries.add("SHOW GRANT");
		TestAssertion assertion = new TestAssertion(false, true, true, null,
				teardownQueries, null, null, null, null, "name");
		PowerMockito.field(TestAssertion.class, "dbHelper").set(assertion,
				dbHelper);
		PowerMockito.when(
				dbHelper.executeAndGetResults(Mockito.anyString(),
						Mockito.any(RETURNTYPE.class),
						Mockito.any(Connection.class))).thenReturn(
				new Result(null, null, null, null));
		assertion.teardown();
	}

	@Test
	public void testReturnCSV() {
		TestAssertion assertion = new TestAssertion(false, false, true, null,
				null, null, RETURNTYPE.CSV, null, null, "name");
		Assert.assertTrue(assertion.getAssertionType() instanceof CsvAssertions);
	}

	@Test
	public void testReturnList() {
		TestAssertion assertion = new TestAssertion(false, false, true, null,
				null, null, RETURNTYPE.LIST, null, null, "name");
		Assert.assertTrue(assertion.getAssertionType() instanceof ListAssertions);
	}

	@Test
	public void testReturnMap() {
		TestAssertion assertion = new TestAssertion(false, false, true, null,
				null, null, RETURNTYPE.MAP, null, null, "name");
		Assert.assertTrue(assertion.getAssertionType() instanceof ListAssertions);
	}

	@Test
	public void testReturnException() {
		TestAssertion assertion = new TestAssertion(false, false, true, null,
				null, null, RETURNTYPE.EXCEPTION, null, null, "name");
		Assert.assertTrue(assertion.getAssertionType() instanceof ExceptionAssertions);
	}

	@Test
	public void testAssertEquals() throws Exception {
		TestAssertion test = Mockito.spy(new TestAssertion(false, false, true,
				null, null, null, RETURNTYPE.CSV, ASSERTTYPE.assert_equals,
				null, "name"));
		PowerMockito.field(TestAssertion.class, "dbHelper").set(test, dbHelper);
		CsvAssertions assertion = PowerMockito.mock(CsvAssertions.class);
		PowerMockito.when(assertion.assertEquals(Mockito.any(), Mockito.any()))
				.thenReturn(true);
		PowerMockito.when(test.getAssertionType()).thenReturn(assertion);
		test.test();
		Mockito.verify(assertion, Mockito.times(1)).assertEquals(Mockito.any(),
				Mockito.any());
	}

	@Test
	public void testAssertIncludes() throws Exception {
		TestAssertion test = Mockito.spy(new TestAssertion(false, false, true,
				null, null, null, RETURNTYPE.CSV, ASSERTTYPE.assert_includes,
				null, "name"));
		PowerMockito.field(TestAssertion.class, "dbHelper").set(test, dbHelper);
		CsvAssertions assertion = PowerMockito.mock(CsvAssertions.class);
		PowerMockito.when(
				assertion.assertIncludes(Mockito.any(), Mockito.any()))
				.thenReturn(true);
		PowerMockito.when(test.getAssertionType()).thenReturn(assertion);
		test.test();
		Mockito.verify(assertion, Mockito.times(1)).assertIncludes(
				Mockito.any(), Mockito.any());
	}

	@Test
	public void testAssertExcludes() throws Exception {
		TestAssertion test = Mockito.spy(new TestAssertion(false, false, true,
				null, null, null, RETURNTYPE.CSV, ASSERTTYPE.assert_excludes,
				null, "name"));
		PowerMockito.field(TestAssertion.class, "dbHelper").set(test, dbHelper);
		CsvAssertions assertion = PowerMockito.mock(CsvAssertions.class);
		PowerMockito.when(
				assertion.assertExcludes(Mockito.any(), Mockito.any()))
				.thenReturn(true);
		PowerMockito.when(test.getAssertionType()).thenReturn(assertion);
		test.test();
		Mockito.verify(assertion, Mockito.times(1)).assertExcludes(
				Mockito.any(), Mockito.any());
	}

	@Test
	public void testAssertOrderedEquals() throws Exception {
		TestAssertion test = Mockito.spy(new TestAssertion(false, false, true,
				null, null, null, RETURNTYPE.CSV,
				ASSERTTYPE.assert_ordered_equals, null, "name"));
		PowerMockito.field(TestAssertion.class, "dbHelper").set(test, dbHelper);
		CsvAssertions assertion = PowerMockito.mock(CsvAssertions.class);
		PowerMockito.when(
				assertion.assertOrderedEquals(Mockito.any(), Mockito.any()))
				.thenReturn(true);
		PowerMockito.when(test.getAssertionType()).thenReturn(assertion);
		test.test();
		Mockito.verify(assertion, Mockito.times(1)).assertOrderedEquals(
				Mockito.any(), Mockito.any());
	}

	@Test
	public void testAssertFails() throws Exception {
		TestAssertion test = Mockito.spy(new TestAssertion(false, false, true,
				null, null, null, RETURNTYPE.EXCEPTION,
				ASSERTTYPE.assert_fails, null, "name"));
		PowerMockito.field(TestAssertion.class, "dbHelper").set(test, dbHelper);
		ExceptionAssertions assertion = PowerMockito
				.mock(ExceptionAssertions.class);
		PowerMockito.when(assertion.assertFails(Mockito.any(), Mockito.any()))
				.thenReturn(true);
		PowerMockito.when(test.getAssertionType()).thenReturn(assertion);
		test.test();
		Mockito.verify(assertion, Mockito.times(1)).assertFails(Mockito.any(),
				Mockito.any());
	}

}
