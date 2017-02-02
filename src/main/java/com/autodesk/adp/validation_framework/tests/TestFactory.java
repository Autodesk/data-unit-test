package com.autodesk.adp.validation_framework.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.yaml.snakeyaml.Yaml;

import com.autodesk.adp.validation_framework.utils.ASSERTTYPE;
import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.YML_CONSTANTS;

public class TestFactory {
	private static final Logger LOG = LoggerFactory
			.getLogger(TestFactory.class);

	private boolean doSetup;
	private boolean doTearDown;
	private boolean ignoreFailures;
	private ArrayList<String> setupQueries;
	private ArrayList<String> tearDownQueries;
	private String testQuery;
	private RETURNTYPE returnType;
	private ASSERTTYPE assertType;
	private Object returnVal;
	private String name;

	@Factory
	@Parameters(value = {"YML_FILE"})
	@SuppressWarnings("unchecked")
	public Object[] createTests(String yamlLocation) throws IOException, SQLException{
		File file = new File(yamlLocation);
		try (FileInputStream fin = new FileInputStream(file)) {
			Yaml yaml = new Yaml();
			List<Map<String, Object>> ymlData = (List<Map<String, Object>>) yaml
					.load(fin);
			Object[] tests = new Object[ymlData.size()];
			int i = 0;
			for (Map<String, Object> data : ymlData) {
				if (validateTest(data)) {
					if (data.containsKey(YML_CONSTANTS.setup.name())) {
						doSetup = true;
						setupQueries = (ArrayList<String>) data
								.get(YML_CONSTANTS.setup.name());
					}
					if (data.containsKey(YML_CONSTANTS.teardown.name())) {
						doTearDown = true;
						tearDownQueries = (ArrayList<String>) data
								.get(YML_CONSTANTS.teardown.name());
					}
					if (data.containsKey(YML_CONSTANTS.ignore_failures.name())) {
						ignoreFailures = (boolean) data
								.get(YML_CONSTANTS.ignore_failures.name());
					}
					testQuery = (String) data.get(YML_CONSTANTS.test.name());
					returnType = RETURNTYPE.getReturnType((String) data
							.get(YML_CONSTANTS.return_type.name()));
					assertType = ASSERTTYPE.getAssertType((String) data
							.get(YML_CONSTANTS.assert_type.name()));
					returnVal = data.get(YML_CONSTANTS.assert_expectation
							.name());
					name = (String) data.get(YML_CONSTANTS.name.name());
					TestAssertion test = new TestAssertion(doSetup, doTearDown,
							ignoreFailures, setupQueries, tearDownQueries,
							testQuery, returnType, assertType, returnVal, name);
					tests[i] = test;
					i++;
				}
			}
			return tests;
		} catch (FileNotFoundException e) {
			LOG.error("YAML file not found : " + yamlLocation, e);
			throw e;
		} catch (IOException e) {
			LOG.error("IO Exception while opening yaml file: " + yamlLocation,
					e);
			throw e;
		} catch (SQLException e) {
			LOG.error("SQL exception while getting connection for the test", e);
			throw e;
		} 

	}

	private boolean validateTest(Map<String, Object> data) {
		if (!data.containsKey(YML_CONSTANTS.test.name())) {
			LOG.error("Parameter 'test' missing from the test description. Will skip test: "
					+ data.toString());
			return false;
		}
		if (!data.containsKey(YML_CONSTANTS.assert_expectation.name())) {
			LOG.error("Parameter 'assert_expectation' missing from the test description. Will skip test: "
					+ data.toString());
			return false;
		}
		if (!data.containsKey(YML_CONSTANTS.assert_type.name())) {
			LOG.error("Parameter 'assert_type' missing from the test description. Will skip test: "
					+ data.toString());
			return false;
		}
		if (!data.containsKey(YML_CONSTANTS.return_type.name())) {
			LOG.error("Parameter 'return_type' missing from the test description. Will skip test: "
					+ data.toString());
			return false;
		}
		if (!data.containsKey(YML_CONSTANTS.name.name())) {
			LOG.error("Parameter 'name' missing from the test description. Will skip test: "
					+ data.toString());
			return false;
		}
		LOG.info("Validation for test case succeeded. Will add test: "
				+ data.toString());
		return true;
	}

}
