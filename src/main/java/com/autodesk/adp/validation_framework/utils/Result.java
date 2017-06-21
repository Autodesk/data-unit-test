package com.autodesk.adp.validation_framework.utils;

import java.util.List;

/**
 * The Class Result. This is a wrapper over the query execution results.
 * Contains either a file name, list of maps, list of lists or an exception as
 * well as the expected return type.
 */
public class Result {

	/** The return type. */
	private RETURNTYPE returnType;

	/** The list of lists or map created using the results of query execution. */
	private List<Object> list;

	/**
	 * The file name of the csv file generated using the results of query
	 * execution.
	 */
	private String fileName;

	/** The exception thrown as a result of query execution. */
	private Exception exception;

	/**
	 * Instantiates a new result.
	 *
	 * @param returnType
	 *            the return type
	 * @param list
	 *            the list of lists or map created using the results of query
	 *            execution.
	 * @param fileName
	 *            the file name of the csv file generated using the results of
	 *            query execution.
	 * @param exception
	 *            the exception thrown as a result of query execution.
	 */
	public Result(RETURNTYPE returnType, List<Object> list, String fileName,
			Exception exception) {
		this.returnType = returnType;
		this.list = list;
		this.fileName = fileName;
		this.exception = exception;
	}

	/**
	 * Gets the return type.
	 *
	 * @return the return type
	 */
	public RETURNTYPE getReturnType() {
		return returnType;
	}

	/**
	 * Gets the list.
	 *
	 * @return the list
	 */
	public List<Object> getList() {
		return list;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the exception.
	 *
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

}
