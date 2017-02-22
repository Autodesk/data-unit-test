package com.autodesk.adp.validation_framework.assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autodesk.adp.validation_framework.utils.ASSERTTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

/**
 * The Class ExceptionAssertions. Works only for {@link ASSERTTYPE#assert_fails}
 * . Used for asserting scenarios when an exception is expected after running
 * the test case. Expects a string representing the exception name or message
 * that should be thrown. The exception obtained after executing the test case
 * is captured and its contents are matched against the expected string.
 */
public class ExceptionAssertions implements Assertions {

	/** The Constant LOG. Used for logging. */
	private static final Logger LOG = LoggerFactory
			.getLogger(ExceptionAssertions.class);

	/**
	 * Unused for exception assertions. Here only for implementation
	 * consistency. Framework should not call this if test cases are properly
	 * formed in the yaml file. In case it is called, it will throw Exception.
	 * 
	 * @throws UnsupportedOperationException
	 *             Calling this means something went wrong.
	 */
	@Override
	public boolean assertEquals(Object expected, Result result)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unused for exception assertions. Here only for implementation
	 * consistency. Framework should not call this if test cases are properly
	 * formed in the yaml file. In case it is called, it will throw Exception.
	 * 
	 * @throws UnsupportedOperationException
	 *             Calling this means something went wrong.
	 */
	@Override
	public boolean assertIncludes(Object expected, Result result)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unused for exception assertions. Here only for implementation
	 * consistency. Framework should not call this if test cases are properly
	 * formed in the yaml file. In case it is called, it will throw Exception.
	 * 
	 * @throws UnsupportedOperationException
	 *             Calling this means something went wrong.
	 */
	@Override
	public boolean assertExcludes(Object expected, Result result)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unused for exception assertions. Here only for implementation
	 * consistency. Framework should not call this if test cases are properly
	 * formed in the yaml file. In case it is called, it will throw Exception.
	 * 
	 * @throws UnsupportedOperationException
	 *             Calling this means something went wrong.
	 */
	@Override
	public boolean assertOrderedEquals(Object expected, Result result)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Assert fails asserts the fact that the exception that is thrown while
	 * executing the test case matches the expected exception. The expected
	 * value is a string that can either contain the name of the exception of
	 * the message that it can possibly contain. The actual exception while
	 * executing the test case is caught and its contents are matched against
	 * the provided expected value.
	 * 
	 */
	@Override
	public boolean assertFails(Object expected, Result result) {
		String actual = result.getException().getMessage();
		String actualExceptionClass = result.getException().getClass()
				.getName();
		if (result.getException().getMessage().contains((String) expected)
				|| actualExceptionClass.contains((String) expected)) {
			LOG.info("Expected result matches the actual result. Expectd : "
					+ expected + ",  actual exception class: "
					+ actualExceptionClass + ", actual exception message: "
					+ actual);
			return true;
		}
		LOG.error("Expected result does not match the actual result. Expectd : "
				+ expected
				+ ",  actual exception class: "
				+ actualExceptionClass
				+ ", actual exception message: "
				+ actual);
		return false;
	}

}
