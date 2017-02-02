package com.autodesk.adp.validation_framework.assertions;

import com.autodesk.adp.validation_framework.utils.Result;


public interface Assertions {
	
	public boolean assertEquals(Object expected, Result actual) throws Exception;
	
	public boolean assertIncludes(Object expected, Result actual) throws Exception;
	
	public boolean assertExcludes(Object expected, Result actual) throws Exception;
	
	public boolean assertOrderedEquals(Object expected, Result actual) throws Exception;
	
	public boolean assertFails(Object expected, Result actual) throws Exception;

}
