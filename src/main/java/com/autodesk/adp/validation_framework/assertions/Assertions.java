package com.autodesk.adp.validation_framework.assertions;

import com.autodesk.adp.validation_framework.utils.Result;


/**
 * The Interface Assertions.
 * Contains the definition of the supported types of assertions.
 */
public interface Assertions {
	
	/**
	 * Assert equals.
	 * Asserts the equality of expected and actual output.
	 *
	 * @param expected the expected output
	 * @param actual the actual result from the execution of the query
	 * @return true, if successful. False, otherwise.
	 */
	public boolean assertEquals(Object expected, Result actual) throws Exception;
	
	/**
	 * Assert includes.
	 * Asserts the inclusion of expected output in the actual output.
	 *
	 * @param expected the expected output
	 * @param actual the actual result from the execution of the query
	 * @return true, if successful. False, otherwise.
	 */
	public boolean assertIncludes(Object expected, Result actual) throws Exception;
	
	/**
	 * Assert excludes.
	 * Asserts the exclusion of expected output from the actual output.
	 *
	 * @param expected the expected output.
	 * @param actual the actual result from the execution of the query.
	 * @return true, if successful. False, otherwise.
	 */
	public boolean assertExcludes(Object expected, Result actual) throws Exception;
	
	/**
	 * Assert ordered equals.
	 * Asserts the equality of expected output and the actual output.
	 * Order of expected and the actual output results is also observed.
	 *
	 * @param expected the expected output.
	 * @param actual the actual result from the execution of the query.
	 * @return true, if successful. False, otherwise.
	 */
	public boolean assertOrderedEquals(Object expected, Result actual) throws Exception;
	
	/**
	 * Assert fails.
	 * Asserts that the operation results in an exception.
	 *
	 * @param expected the expected exception name or message.
	 * @param actual the actual result from the execution of the query.
	 * @return true, if successful. False, otherwise.
	 */
	public boolean assertFails(Object expected, Result actual) throws Exception;

}
