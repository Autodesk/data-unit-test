package com.autodesk.adp.validation_framework.utils;

public enum RETURNTYPE {
	
	NONE,
	LIST,
	MAP,
	CSV,
	EXCEPTION;
	
	
	public static RETURNTYPE getReturnType(String type) throws IllegalArgumentException {
		for(RETURNTYPE returnType : RETURNTYPE.values()) {
			if(returnType.name().equalsIgnoreCase(type))
				return returnType;
		}
		throw new IllegalArgumentException("Unsupported return type: " + type);
	}

}
