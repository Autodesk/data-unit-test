package com.autodesk.adp.validation_framework.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import com.google.common.annotations.VisibleForTesting;

/**
 * A factory for creating Test objects. Creates tests based on the YML file
 * provided.
 */
public class TestFactory {

	/** The Constant LOG. Used for logging. */
	private static final Logger LOG = LoggerFactory
			.getLogger(TestFactory.class);

	/** The do setup boolean flag. */
	private boolean doSetup;

	/** The do tear down. */
	private boolean doTearDown;

	/** The ignore failures. */
	private boolean ignoreFailures;

	/** The setup queries. */
	private ArrayList<String> setupQueries;

	/** The tear down queries. */
	private ArrayList<String> tearDownQueries;

	/** The test query. */
	private String testQuery;

	/** The return type. */
	private RETURNTYPE returnType;

	/** The assert type. */
	private ASSERTTYPE assertType;

	/** The return val. */
	private Object returnVal;

	/** The name. */
	private String name;

	/**
	 * Creates an array of tests that can be executed. Adds only valid tests.
	 *
	 * @param yamlLocation
	 *            the yaml file location that contains all information of the
	 *            tests to be executed.
	 * @return the object[] containing all the executable tests.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred while reading the
	 *             input YAML file..
	 */
	@Factory
	@Parameters(value = { "YML_FILE" })
	@SuppressWarnings("unchecked")
	public Object[] createTests(String yamlLocation) throws IOException {
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
		} catch (IOException e) {
			LOG.error("IO Exception while opening yaml file: " + yamlLocation,
					e);
			throw e;
		}

	}

	/**
	 * Validate test. Checks the YAML snippet containing the test. Checks for
	 * inconsistencies and the presence of required fields. If any required
	 * field is missing from the test specification, it returns false.
	 *
	 * @param data
	 *            the data containing the test specifications.
	 * @return true, if all required fields are present in the test
	 *         specification. False otherwise.
	 */
	@VisibleForTesting
	protected boolean validateTest(Map<String, Object> data) {
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
