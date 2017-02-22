package com.autodesk.adp.validation_framework.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.autodesk.adp.validation_framework.utils.YML_CONSTANTS;

public class FactoryTest {
	
	private TestFactory factory = new TestFactory();
	
	@Test(expectedExceptions={IOException.class})
	public void testMissingYML() throws IOException, SQLException {
		factory.createTests("non_existant_test.yml");
	}
	
	@Test
	public void testCreate() throws IOException{
		Object[] objects = factory.createTests("src/test/resources/valid.yml");
		Assert.assertTrue(objects.length == 1);
		Object testObject = objects[0];
		Assert.assertTrue(testObject instanceof TestAssertion);
		
	}
	
	@Test
	public void testInvalidMissingTest() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(YML_CONSTANTS.assert_expectation.name(), "expectation");
		data.put(YML_CONSTANTS.assert_type.name(), "assert_type");
		data.put(YML_CONSTANTS.return_type.name(), "return_type");
		data.put(YML_CONSTANTS.name.name(), "name");
		Assert.assertFalse(factory.validateTest(data));
				
	}
	
	@Test
	public void testInvalidMissingExpectation() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(YML_CONSTANTS.test.name(), "test");
		data.put(YML_CONSTANTS.assert_type.name(), "assert_type");
		data.put(YML_CONSTANTS.return_type.name(), "return_type");
		data.put(YML_CONSTANTS.name.name(), "name");
		Assert.assertFalse(factory.validateTest(data));
	}
	
	@Test
	public void testInvalidMissingAssertType() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(YML_CONSTANTS.test.name(), "test");
		data.put(YML_CONSTANTS.assert_expectation.name(), "expectation");
		data.put(YML_CONSTANTS.return_type.name(), "return_type");
		data.put(YML_CONSTANTS.name.name(), "name");
		Assert.assertFalse(factory.validateTest(data));
	}
	
	@Test
	public void testInvalidMissingReturnType() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(YML_CONSTANTS.test.name(), "test");
		data.put(YML_CONSTANTS.assert_expectation.name(), "expectation");
		data.put(YML_CONSTANTS.assert_type.name(), "assert_type");
		data.put(YML_CONSTANTS.name.name(), "name");
		Assert.assertFalse(factory.validateTest(data));
	}
	
	@Test
	public void testInvalidMissingName() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(YML_CONSTANTS.test.name(), "test");
		data.put(YML_CONSTANTS.assert_expectation.name(), "expectation");
		data.put(YML_CONSTANTS.assert_type.name(), "assert_type");
		data.put(YML_CONSTANTS.return_type.name(), "return_type");
		//data.put(YML_CONSTANTS.name.name(), "name");
		Assert.assertFalse(factory.validateTest(data));
	}
	
	@Test
	public void testValidCase() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(YML_CONSTANTS.test.name(), "test");
		data.put(YML_CONSTANTS.assert_expectation.name(), "expectation");
		data.put(YML_CONSTANTS.assert_type.name(), "assert_type");
		data.put(YML_CONSTANTS.return_type.name(), "return_type");
		data.put(YML_CONSTANTS.name.name(), "name");
		Assert.assertTrue(factory.validateTest(data));
	}

}
