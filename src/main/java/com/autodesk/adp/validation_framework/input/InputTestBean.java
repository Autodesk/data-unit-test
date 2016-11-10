package com.autodesk.adp.validation_framework.input;

import java.util.ArrayList;

public class InputTestBean {
	
	private class Test {
		String name;
		ArrayList<String> setup;
		String test;
		String test_fail;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public ArrayList<String> getSetup() {
			return setup;
		}
		public void setSetup(ArrayList<String> setup) {
			this.setup = setup;
		}
		public String getTest() {
			return test;
		}
		public void setTest(String test) {
			this.test = test;
		}
		public String getTest_fail() {
			return test_fail;
		}
		public void setTest_fail(String test_fail) {
			this.test_fail = test_fail;
		}
		@Override
		public String toString() {
			return "Test [name=" + name + ", setup=" + setup + ", test=" + test
					+ ", test_fail=" + test_fail + "]";
		}
		
		
	}
	
	private ArrayList<Test> tests;

	public ArrayList<Test> getTests() {
		return tests;
	}

	public void setTests(ArrayList<Test> tests) {
		this.tests = tests;
	}

	@Override
	public String toString() {
		return "InputTestBean [tests=" + tests + "]";
	}
	
	
	
	
	

}
