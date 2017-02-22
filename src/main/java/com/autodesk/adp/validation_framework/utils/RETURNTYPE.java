package com.autodesk.adp.validation_framework.utils;

/**
 * The Enum RETURNTYPE. Specifies the different types of return types supported
 * based on which the output of query execution are converted to appropriate
 * objects..
 */
public enum RETURNTYPE {

	/**
	 * The none return type is used for setup and tear down queries. Should not
	 * be used by end users in their testing otherwise, the query results will
	 * be discarded.
	 */
	NONE,

	/**
	 * The list return type is used when the output is in form of a list of list
	 * or JSONArray of JSONArray.
	 */
	LIST,

	/**
	 * The map return type is used when output is in form of list of maps or
	 * JSONArray of JSONObject.
	 */
	MAP,

	/** The csv return type is used when output is in form of a CSV file. */
	CSV,

	/**
	 * The exception return type is used when the query execution is expected to
	 * throw an exception.
	 */
	EXCEPTION;

	/**
	 * Gets the return type based on the name of the return type.
	 *
	 * @param type
	 *            the return type name
	 * @return the return type
	 * @throws IllegalArgumentException
	 *             Signals that the return type name that was provided does not
	 *             resolve to any one of the supported return types.
	 */
	public static RETURNTYPE getReturnType(String type)
			throws IllegalArgumentException {
		for (RETURNTYPE returnType : RETURNTYPE.values()) {
			if (returnType.name().equalsIgnoreCase(type))
				return returnType;
		}
		throw new IllegalArgumentException("Unsupported return type: " + type);
	}

}
