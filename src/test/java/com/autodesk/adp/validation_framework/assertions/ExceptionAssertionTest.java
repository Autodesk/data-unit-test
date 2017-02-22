package com.autodesk.adp.validation_framework.assertions;

import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

public class ExceptionAssertionTest {

	private static final ExceptionAssertions ASSERTION = new ExceptionAssertions();

	@Test
	public void testAssertFailsNameMatch() throws Exception {
		String exceptionMessage = "SQLException";
		Assert.assertTrue(ASSERTION.assertFails(exceptionMessage, new Result(
				RETURNTYPE.EXCEPTION, null, null, new SQLException(
						"Unable to fetch table contents"))));
	}
	
	@Test
	public void testAssertFailsMessageMatch() throws Exception {
		String exceptionMessage = "Unable to fetch";
		Assert.assertTrue(ASSERTION.assertFails(exceptionMessage, new Result(
				RETURNTYPE.EXCEPTION, null, null, new SQLException(
						"Unable to fetch table contents"))));
	}
	
	@Test
	public void testAssertFailsNoMatch() throws Exception {
		String exceptionMessage = "MetastoreException";
		Assert.assertFalse(ASSERTION.assertFails(exceptionMessage, new Result(
				RETURNTYPE.EXCEPTION, null, null, new SQLException(
						"Unable to fetch table contents"))));
	}

}
