package com.autodesk.adp.validation_framework.assertions;

import java.util.List;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autodesk.adp.validation_framework.utils.Result;

public class JSONAssertions implements Assertions {

	private static final Logger LOG = LoggerFactory
			.getLogger(JSONAssertions.class);

	public boolean assertEquals(Object expectedList, Result result) {
		String actualList = result.getList().toString();
		@SuppressWarnings("unchecked")
		List<Object> expectedArray = (List<Object>) expectedList;
		List<Object> actualArray = new JSONArray(actualList).toList();
		if (actualArray.size() != expectedArray.size()) {
			LOG.error("Number of records in actual list does not match the number of records in expected list");
			return false;
		}
		if (!actualArray.containsAll(expectedArray)) {
			LOG.error("Actual list does not match with expected list. Expected: " + expectedArray.toString() + ", actual: " + actualArray.toString());
			return false;
		}
		LOG.info("Actual list matches with the expected list. Expected: " + expectedArray.toString() + ", actual: " + actualArray.toString());
		return true;
	}

	public boolean assertIncludes(Object expectedList, Result result) {
		String actualList = result.getList().toString();
		@SuppressWarnings("unchecked")
		List<Object> expectedArray = (List<Object>) expectedList;
		List<Object> actualArray = new JSONArray(actualList).toList();
		if (!actualArray.containsAll(expectedArray)) {
			LOG.error("Actual list does not include expected list");
			return false;
		}
		LOG.info("Actual list includes the expected list");
		return true;
	}

	public boolean assertExcludes(Object expectedList, Result result) {
		String actualList = result.getList().toString();
		@SuppressWarnings("unchecked")
		List<Object> expectedArray = (List<Object>) expectedList;
		List<Object> actualArray = new JSONArray(actualList).toList();
		for (int i = 0; i < expectedArray.size(); i++) {
			if (actualArray.contains(expectedArray.get(i))) {
				LOG.error("Actual list does not exclude expected list");
				return false;
			}
		}
		LOG.info("Actual list excludes the expected list");
		return true;
	}

	public boolean assertOrderedEquals(Object expectedList, Result result) {
		String actualList = result.getList().toString();
		@SuppressWarnings("unchecked")
		List<Object> expectedArray = (List<Object>) expectedList;
		List<Object> actualArray = new JSONArray(actualList).toList();
		if (actualArray.size() != expectedArray.size()) {
			LOG.error("Number of records in actual list does not match the number of records in expected list");
			return false;
		}
		for (int i = 0; i < actualArray.size(); i++) {
			if (!actualArray.get(i).equals(expectedArray.get(i))) {
				LOG.error("Actual list does not match the expected list");
				return false;
			}
		}
		LOG.info("Actual list matches with the expected list");
		return true;
	}

	@Override
	public boolean assertFails(Object expected, Result actual)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}
