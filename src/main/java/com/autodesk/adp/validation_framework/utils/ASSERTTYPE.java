package com.autodesk.adp.validation_framework.utils;

public enum ASSERTTYPE {
	assert_equals,
	assert_ordered_equals,
	assert_includes,
	assert_excludes,
	assert_fails;
	
	public static ASSERTTYPE getAssertType(String type) throws IllegalArgumentException {
		for(ASSERTTYPE assertType : ASSERTTYPE.values()) {
			if(assertType.name().equalsIgnoreCase(type))
				return assertType;
		}
		throw new IllegalArgumentException("Unsupported assert type: " + type);
	}

}
