package com.autodesk.adp.validation_framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.yaml.snakeyaml.Yaml;

import com.autodesk.adp.validation_framework.utils.ASSERTTYPE;
import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.YML_CONSTANTS;

public class TestSuiteGenerator {
	private static final Logger LOG = LoggerFactory
			.getLogger(TestSuiteGenerator.class);

	@SuppressWarnings("unchecked")
	public XmlSuite generateXMLSuite(String yamlLocation)
			throws FileNotFoundException, IOException {
		File f = new File(yamlLocation);
		try (FileInputStream fin = new FileInputStream(f)) {
			Yaml yaml = new Yaml();
			List<Map<String, Object>> ymlData = (List<Map<String, Object>>) yaml
					.load(fin);
			XmlSuite suite = new XmlSuite();
			suite.setName(f.getName());
			for (Map<String, Object> data : ymlData) {
				XmlTest test = new XmlTest(suite);
				
				List<XmlClass> classes = new ArrayList<XmlClass>();
				classes.add(new XmlClass("com.autodesk.adp.validation_framework.tests.TestAssertion"));
				test.setXmlClasses(classes);
				
				test.addParameter("testData", ymlData.toString());
				
				
				test.setName((String) data.get(YML_CONSTANTS.name.name()));
				
				RETURNTYPE returnType = RETURNTYPE.getReturnType((String) data
						.get(YML_CONSTANTS.return_type.name()));
				ASSERTTYPE assertType = ASSERTTYPE.getAssertType((String) data
						.get(YML_CONSTANTS.assert_type.name()));
				ArrayList<String> setup = (ArrayList<String>) data
						.get(YML_CONSTANTS.setup.name());
				ArrayList<String> tearDown = (ArrayList<String>) data
						.get(YML_CONSTANTS.teardown.name());
				test.addParameter(YML_CONSTANTS.setup.name(), setup.toString());
				test.addParameter(YML_CONSTANTS.teardown.name(),
						tearDown.toString());
				test.addParameter(YML_CONSTANTS.ignore_failures.name(), Boolean
						.toString((boolean) data
								.get(YML_CONSTANTS.ignore_failures.name())));
				test.addParameter(YML_CONSTANTS.test.name(),
						(String) data.get(YML_CONSTANTS.test.name()));
				test.addParameter(YML_CONSTANTS.return_type.name(), returnType.name());
				test.addParameter(YML_CONSTANTS.assert_type.name(), assertType.name());
				switch (returnType) {
				case CSV:
					test.addParameter(YML_CONSTANTS.assert_expectation.name(), (String) data.get(YML_CONSTANTS.assert_expectation.name()));
					break;
				case LIST:
					ArrayList<String> dataList = (ArrayList<String>) data.get(YML_CONSTANTS.assert_expectation.name());
					test.addParameter(YML_CONSTANTS.assert_expectation.name(), dataList.toString());
					break;
				case MAP:
					ArrayList<Map<String, Object>> dataMap = (ArrayList<Map<String, Object>>) data.get(YML_CONSTANTS.assert_expectation.name());
					test.addParameter(YML_CONSTANTS.assert_expectation.name(), dataMap.toString());
					break;
				default:
					break;
				}
			}
			LOG.info("Generated test suite: " + suite);
			return suite;
		}
	}

}
