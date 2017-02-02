package com.autodesk.adp.validation_framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.autodesk.adp.validation_framework.utils.TEST_CONSTANTS;

@SuppressWarnings("static-access")
public class TestRunner {

	private static final Options cliOptions = new Options();
	private static CommandLine cmd;
	private static String ymlLocation;
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

		cliOptions.addOption(inputYml);
		cliOptions.addOption(loggerOption);
		cliOptions.addOption(dbUrlOption);
		cliOptions.addOption(userNameOption);
		cliOptions.addOption(userPasswordOption);
		cliOptions.addOption(idleTimeOutOption);
		cliOptions.addOption(activeConnectionOption);
	}

	private static void parseArgs(String[] args) {
		CommandLineParser parser = new BasicParser();
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
		if (cmd.hasOption(TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name()))
			System.setProperty(TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name(),
					cmd.getOptionValue(TEST_CONSTANTS.CONN_IDLE_TIMEOUT.name()));
		if (cmd.hasOption(TEST_CONSTANTS.ACTIVE_CONN_COUNT.name()))
			System.setProperty(TEST_CONSTANTS.ACTIVE_CONN_COUNT.name(),
					cmd.getOptionValue(TEST_CONSTANTS.ACTIVE_CONN_COUNT.name()));
	}

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

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {

		parseArgs(args);
		createAndRunTests();
		DBHelper.getInstance().closeAll();
	}

}
