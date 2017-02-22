package com.autodesk.adp.validation_framework.utils;

/**
 * The Enum YML_CONSTANTS defines all the keys that can be present in the test
 * case.
 */
public enum YML_CONSTANTS {

	/** The name of the test case. */
	name,

	/** The list of setup queries, if required. */
	setup,

	/** The test query. */
	test,

	/** The assert type. */
	assert_type,

	/** The list of tear down queries, if required. */
	teardown,

	/**
	 * The ignore failures flag, if set to true, ignores failures while
	 * executing setup and tear down queries.
	 */
	ignore_failures,

	/** The return type. */
	return_type,

	/**
	 * The assert expectation contains the expected result. Can be a file name
	 * or a JSONArray of JSONArray or JSONArray of JSONObject or an exception
	 * string. Should match with the return type.
	 */
	assert_expectation;
}
