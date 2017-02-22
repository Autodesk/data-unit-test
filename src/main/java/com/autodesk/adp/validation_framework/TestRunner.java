package com.autodesk.adp.validation_framework;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.PropertyConfigurator;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.autodesk.adp.validation_framework.db.DBHelper;
import com.autodesk.adp.validation_framework.tests.TestAssertion;
import com.autodesk.adp.validation_framework.utils.TEST_CONSTANTS;

/**
 * The Class TestRunner. Main class that is called while running the test
 * framework. Is responsible for parsing the options and running the appropriate
 * tests based on the yaml file.
 */
@SuppressWarnings("static-access")
public class TestRunner {

	/** The Constant cliOptions. Used for parsing command line options */
	private static final Options cliOptions = new Options();

	/** The yml location. The input yaml file containing all test cases */
	private static String ymlLocation;

	/**
	 * The Constant TEST_FACTORY. @see
	 * {@link com.autodesk.adp.validation_framework.tests.TestFactory}
	 */
	private static final String TEST_FACTORY = "com.autodesk.adp.validation_framework.tests.TestFactory";

	static {
		Option inputYml = OptionBuilder.hasArg(true).isRequired(true)
				.create(TEST_CONSTANTS.YML_FILE.name());
		Option loggerOption = OptionBuilder.hasArg(true).isRequired(false)
				.create(TEST_CONSTANTS.LOGGER.name());
		Option dbUrlOption = OptionBuilder.hasArg(true).isRequired(true)
				.create(TEST_CONSTANTS.DB_URL.name());
		Option userNameOption = OptionBuilder.hasArg(true).isRequired(true)
				.create(TEST_CONSTANTS.USER_NAME.name());
		Option userPasswordOption = OptionBuilder.hasArg(true).isRequired(true)
				.create(TEST_CONSTANTS.USER_PASSWORD.name());
		Option idleTimeOutOption = OptionBuilder.hasArg(true).isRequired(false)
				.create(TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name());
		Option activeConnectionOption = OptionBuilder.hasArg(true)
				.isRequired(false)
				.create(TEST_CONSTANTS.ACTIVE_CONN_COUNT.name());
		Option jdbcDriverOption = OptionBuilder.hasArg(true).isRequired(false)
				.create(TEST_CONSTANTS.JDBC_DRIVER.name());

		cliOptions.addOption(inputYml);
		cliOptions.addOption(loggerOption);
		cliOptions.addOption(dbUrlOption);
		cliOptions.addOption(userNameOption);
		cliOptions.addOption(userPasswordOption);
		cliOptions.addOption(idleTimeOutOption);
		cliOptions.addOption(activeConnectionOption);
		cliOptions.addOption(jdbcDriverOption);
	}

	/**
	 * Parses the args passed by the command line
	 *
	 * @param args
	 *            the command line arguments
	 */
	private static void parseArgs(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(cliOptions, args, true);
		} catch (ParseException e) {
			e.printStackTrace();
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(TestRunner.class.getName(),
					"Tesr Runner Execution Environment", cliOptions, "", true);
			System.exit(2);
		}
		ymlLocation = cmd.getOptionValue(TEST_CONSTANTS.YML_FILE.name());
		if (cmd.hasOption(TEST_CONSTANTS.LOGGER.name())) {
			Properties props = new Properties();
			try {
				props.load(new FileInputStream(cmd
						.getOptionValue(TEST_CONSTANTS.LOGGER.name())));
			} catch (IOException e) {
				e.printStackTrace();
			}
			PropertyConfigurator.configure(props);
		}

		System.setProperty(TEST_CONSTANTS.DB_URL.name(),
				cmd.getOptionValue(TEST_CONSTANTS.DB_URL.name()));
		System.setProperty(TEST_CONSTANTS.USER_NAME.name(),
				cmd.getOptionValue(TEST_CONSTANTS.USER_NAME.name()));
		System.setProperty(TEST_CONSTANTS.USER_PASSWORD.name(),
				cmd.getOptionValue(TEST_CONSTANTS.USER_PASSWORD.name()));

		if (cmd.hasOption(TEST_CONSTANTS.JDBC_DRIVER.name()))
			System.setProperty(TEST_CONSTANTS.JDBC_DRIVER.name(),
					cmd.getOptionValue(TEST_CONSTANTS.JDBC_DRIVER.name()));

		if (cmd.hasOption(TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name()))
			System.setProperty(TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name(),
					cmd.getOptionValue(TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name()));
		if (cmd.hasOption(TEST_CONSTANTS.ACTIVE_CONN_COUNT.name()))
			System.setProperty(TEST_CONSTANTS.ACTIVE_CONN_COUNT.name(),
					cmd.getOptionValue(TEST_CONSTANTS.ACTIVE_CONN_COUNT.name()));

	}

	/**
	 * Creates and run tests. Uses the supplied arguments to create an xml
	 * suite. The xml suite uses @link TestFactory to create the test factory.
	 * This test factory takes the yaml file provided for the test cases. Each
	 * test case is executed using appropriate @see {@link TestAssertion}
	 */
	private static void createAndRunTests() {
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		XmlSuite suite = new XmlSuite();
		suite.setName("validation_tests");
		XmlTest test = new XmlTest(suite);
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass(TEST_FACTORY));
		test.setName("tests");
		test.setXmlClasses(classes);
		test.addParameter(TEST_CONSTANTS.YML_FILE.name(), ymlLocation);
		suites.add(suite);
		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.run();
	}

	/**
	 * The main method. Parses the command line arguments, creates appropriate
	 * test cases using the arguments and executes them. Once all test cases
	 * have finished executing, closes the associated database connection pool.
	 *
	 * @param args
	 *            the command line arguments
	 * @throws SQLException
	 *             the SQL exception in case DBHelper is unable to close the
	 *             connections.
	 */
	public static void main(String[] args) throws SQLException {
		try {
			parseArgs(args);
			createAndRunTests();
		} finally {
			DBHelper.getInstance().closeAll();
		}
	}

}
