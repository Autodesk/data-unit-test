package com.autodesk.adp.validation_framework.utils;

/**
 * The Enum ASSERTTYPE. Specifies the types of supported assertions.
 */
public enum ASSERTTYPE {

	/**
	 * The assert equals. Checks for equality of expected and actual output
	 * while disregarding the order of rows.
	 */
	assert_equals,

	/**
	 * The assert ordered equals. Checks for equality of expected and actual
	 * output. The order of rows is considered.
	 */
	assert_ordered_equals,

	/**
	 * The assert includes. Checks for inclusion of expected output in the
	 * actual output while disregarding the order of rows.
	 */
	assert_includes,

	/**
	 * The assert excludes. Checks for exclusion of expected output from the
	 * actual output while disregarding the order of rows.
	 */
	assert_excludes,

	/**
	 * The assert fails. Checks for an exception that must be that must be
	 * thrown while test case execution.
	 */
	assert_fails;

	/**
	 * Gets the assert type using the string.
	 *
	 * @param type
	 *            the type name
	 * @return the assert type
	 * @throws IllegalArgumentException
	 *             Signals that the passed string parameter does not resolve to
	 *             any of the supported assert types.
	 */
	public static ASSERTTYPE getAssertType(String type)
			throws IllegalArgumentException {
		for (ASSERTTYPE assertType : ASSERTTYPE.values()) {
			if (assertType.name().equalsIgnoreCase(type))
				return assertType;
		}
		throw new IllegalArgumentException("Unsupported assert type: " + type);
	}

}
