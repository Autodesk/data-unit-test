package com.autodesk.adp.validation_framework.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UtilsTest {
	
	@Test
	public void testValidAssertType() {
		Assert.assertEquals(ASSERTTYPE.assert_equals, ASSERTTYPE.getAssertType("assert_equals"));
		Assert.assertEquals(ASSERTTYPE.assert_excludes, ASSERTTYPE.getAssertType("assert_excludes"));
		Assert.assertEquals(ASSERTTYPE.assert_fails, ASSERTTYPE.getAssertType("assert_fails"));
		Assert.assertEquals(ASSERTTYPE.assert_includes, ASSERTTYPE.getAssertType("assert_includes"));
		Assert.assertEquals(ASSERTTYPE.assert_ordered_equals, ASSERTTYPE.getAssertType("assert_ordered_equals"));
	}
	
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testInvalidAssertType() {
		ASSERTTYPE.getAssertType("any_random_assert_type");
	}
	
	@Test
	public void testSupportedReturnType() {
		Assert.assertEquals(RETURNTYPE.CSV, RETURNTYPE.getReturnType("csv"));
		Assert.assertEquals(RETURNTYPE.EXCEPTION, RETURNTYPE.getReturnType("EXCEPTION"));
		Assert.assertEquals(RETURNTYPE.LIST, RETURNTYPE.getReturnType("LIST"));
		Assert.assertEquals(RETURNTYPE.MAP, RETURNTYPE.getReturnType("MAP"));
		Assert.assertEquals(RETURNTYPE.NONE, RETURNTYPE.getReturnType("NONE"));
	}
	
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testUnsupportedReturnType() {
		RETURNTYPE.getReturnType("SOMETHING");
	}

}
