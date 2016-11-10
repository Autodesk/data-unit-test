package com.autodesk.adp.validation_framework.utils;

import org.json.JSONArray;

public class Result {

	private RETURNTYPE returnType;
	private JSONArray list;
	private String fileName;
	private Exception exception;

	public Result(RETURNTYPE returnType, JSONArray list, String fileName,
			Exception exception) {
		this.returnType = returnType;
		this.list = list;
		this.fileName = fileName;
		this.exception = exception;
	}

	public RETURNTYPE getReturnType() {
		return returnType;
	}

	public JSONArray getList() {
		return list;
	}

	public String getFileName() {
		return fileName;
	}

	public Exception getException() {
		return exception;
	}

}
