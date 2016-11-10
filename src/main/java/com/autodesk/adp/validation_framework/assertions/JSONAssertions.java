package com.autodesk.adp.validation_framework.assertions;

import java.util.List;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONAssertions implements Assertions {

	private static final Logger LOG = LoggerFactory.getLogger(JSONAssertions.class);

	public boolean assertEquals(String expectedList, String actualList) {
		List<Object> expectedArray = new JSONArray(expectedList).toList();
		List<Object> actualArray = new JSONArray(actualList).toList();
		if (actualArray.size() != expectedArray.size()) {
			LOG.error("Number of records in actual list does not match the number of records in expected list");
			return false;
		}
		if (!actualArray.containsAll(expectedArray)) {
			LOG.error("Actual list does not match with expected list");
			return false;
		}
		LOG.info("Actual list matches with the expected list");
		return true;
	}

	public boolean assertIncludes(String expectedList, String actualList) {
		List<Object> expectedArray = new JSONArray(expectedList).toList();
		List<Object> actualArray = new JSONArray(actualList).toList();
		if (!actualArray.containsAll(expectedArray)) {
			LOG.error("Actual list does not include expected list");
			return false;
		}
		LOG.info("Actual list includes the expected list");
		return true;
	}

	public boolean assertExcludes(String expectedList, String actualList) {
		List<Object> expectedArray = new JSONArray(expectedList).toList();
		List<Object> actualArray = new JSONArray(actualList).toList();
		for(int i=0; i< expectedArray.size(); i++) {
			if (actualArray.contains(expectedArray.get(i))) {
				LOG.error("Actual list does not exclude expected list");
				return false;
			}
		}
		LOG.info("Actual list excludes the expected list");
		return true;
	}

	public boolean assertOrderedEquals(String expectedList, String actualList) {
		List<Object> expectedArray = new JSONArray(expectedList).toList();
		List<Object> actualArray = new JSONArray(actualList).toList();
		if (actualArray.size() != expectedArray.size()) {
			LOG.error("Number of records in actual list does not match the number of records in expected list");
			return false;
		}
		for(int i=0; i<actualArray.size(); i++) {
			if (!actualArray.get(i).equals(expectedArray.get(i))) {
				LOG.error("Actual list does not match the expected list");
				return false;
			}
		}
		LOG.info("Actual list matches with the expected list");
		return true;
	}
	
}
