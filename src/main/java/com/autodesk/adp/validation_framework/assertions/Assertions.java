package com.autodesk.adp.validation_framework.assertions;

import java.io.IOException;

public interface Assertions {
	
	public boolean assertEquals(String expected, String actual) throws IOException;
	
	public boolean assertIncludes(String expected, String actual) throws IOException;
	
	public boolean assertExcludes(String expected, String actual) throws IOException;
	
	public boolean assertOrderedEquals(String expected, String actual) throws IOException;

}
