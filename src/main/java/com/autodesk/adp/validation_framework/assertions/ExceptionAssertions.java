package com.autodesk.adp.validation_framework.assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autodesk.adp.validation_framework.utils.Result;

public class ExceptionAssertions implements Assertions {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionAssertions.class);

	@Override
	public boolean assertEquals(Object expected, Result result)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean assertIncludes(Object expected, Result result)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean assertExcludes(Object expected, Result result)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean assertOrderedEquals(Object expected, Result result)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean assertFails(Object expected, Result result) throws Exception {
		String actual = result.getException().getMessage();
		if (result.getException().getMessage().contains((String) expected)) {
			LOG.info("Expected result matches the actual result. Expectd : " + expected + ", actual: " + actual);
			return true;
		}
		LOG.error("Expected result does not match the actual result. Expected: " + expected + ", actual: " + actual);
		return false;
	}

}
