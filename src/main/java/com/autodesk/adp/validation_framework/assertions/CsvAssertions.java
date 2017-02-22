package com.autodesk.adp.validation_framework.assertions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autodesk.adp.validation_framework.utils.ASSERTTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

/**
 * The Class CsvAssertions. Checks for various supported assertions (specified in
 * {@link ASSERTTYPE}) The expected object must always be a locally existing csv
 * file. The execution of tests create the actual result csv file which is then
 * matched against the expected file. Post assertion, the actual file is
 * deleted.
 */
public class CsvAssertions implements Assertions {

	/** The Constant LOG. Used for logging */
	private static final Logger LOG = LoggerFactory
			.getLogger(CsvAssertions.class);

	/**
	 * Assert equals checks for equality between expected and actual csv file.
	 * Both csv files are parsed and the contents are compared. The order of the
	 * rows in the result does not affect the comparison and if both the files
	 * have same number of rows and the contents of all the rows match, it is
	 * considered to be a match. The test case executed will create a temporary
	 * csv file containing rows of actual results.
	 * 
	 * @param expectedFile
	 *            The name of local csv file containing the expected rows of
	 *            results.
	 * @param result
	 *            The result obtained after executing the test case.
	 * @throws IOException
	 *             In case, the file is not found or there is an error while
	 *             parsing the contents of the file.
	 */
	public boolean assertEquals(Object expectedFile, Result result)
			throws IOException {
		String actualFileName = result.getFileName();
		File actualFile = new File(actualFileName);
		try (Reader expectedFileReader = new FileReader((String) expectedFile);
				Reader actualFileReader = new FileReader(actualFile);) {
			CSVParser expectedFileParser = CSVFormat.DEFAULT
					.withFirstRecordAsHeader().parse(expectedFileReader);

			CSVParser actualFileParser = CSVFormat.DEFAULT
					.withFirstRecordAsHeader().parse(actualFileReader);
			if (!expectedFileParser.getHeaderMap().equals(
					actualFileParser.getHeaderMap())) {
				LOG.error("Headers in CSV file do not match");
				return false;
			}
			List<Map<String, String>> expectedList = expectedFileParser
					.getRecords().stream().map(record -> record.toMap())
					.collect(Collectors.toList());
			List<Map<String, String>> actualList = actualFileParser
					.getRecords().stream().map(record -> record.toMap())
					.collect(Collectors.toList());
			if (expectedList.size() != actualList.size()) {
				LOG.error("Number of records in csv files do not match");
				return false;
			}
			if (!actualList.containsAll(expectedList)) {
				LOG.error("Contents of csv file do not match");
				return false;
			} else {
				LOG.info("CSV files matched");
				return true;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			actualFile.delete();
		}

	}

	/**
	 * Assert include checks for inclusion of expected results in the actual csv
	 * file. Both csv files are parsed and the contents are compared. The order
	 * of the rows in the result does not affect the comparison and if all the
	 * rows of expected file are present in the actual file, it is considered to
	 * be a match. The test case executed will create a temporary csv file
	 * containing rows of actual results.
	 * 
	 * @param expectedFile
	 *            The name of local csv file containing the expected rows of
	 *            results.
	 * @param result
	 *            The result obtained after executing the test case.
	 * @throws IOException
	 *             In case, the file is not found or there is an error while
	 *             parsing the contents of the file.
	 */
	public boolean assertIncludes(Object expectedFile, Result result)
			throws IOException {
		String actualFileName = result.getFileName();
		File actualFile = new File(actualFileName);
		try (Reader expectedFileReader = new FileReader((String) expectedFile);
				Reader actualFileReader = new FileReader(actualFile);) {
			CSVParser expectedFileParser = CSVFormat.DEFAULT
					.withFirstRecordAsHeader().parse(expectedFileReader);
			CSVParser actualFileParser = CSVFormat.DEFAULT
					.withFirstRecordAsHeader().parse(actualFileReader);
			if (!expectedFileParser.getHeaderMap().equals(
					actualFileParser.getHeaderMap())) {
				LOG.error("Headers in CSV file do not match");
				return false;
			}
			List<Map<String, String>> expectedList = expectedFileParser
					.getRecords().stream().map(record -> record.toMap())
					.collect(Collectors.toList());
			List<Map<String, String>> actualList = actualFileParser
					.getRecords().stream().map(record -> record.toMap())
					.collect(Collectors.toList());
			if (!actualList.containsAll(expectedList)) {
				LOG.error("Actual csv file does not include the expected csv file");
				return false;
			} else {
				LOG.info("Actual csv file includes expected csv file");
				return true;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			actualFile.delete();
		}
	}

	/**
	 * Assert excludes checks for exclusion of expected results from the actual
	 * csv file. Both csv files are parsed and the contents are compared. The
	 * order of the rows in the result does not affect the comparison and if all
	 * the rows of expected file are absent from the actual file, it is
	 * considered to be a match. The test case executed will create a temporary
	 * csv file containing rows of actual results.
	 * 
	 * @param expectedFile
	 *            The name of local csv file containing the expected rows of
	 *            results.
	 * @param result
	 *            The result obtained after executing the test case.
	 * @throws IOException
	 *             In case, the file is not found or there is an error while
	 *             parsing the contents of the file.
	 */
	public boolean assertExcludes(Object expectedFile, Result result)
			throws IOException {
		String actualFileName = result.getFileName();
		File actualFile = new File(actualFileName);
		try (Reader expectedFileReader = new FileReader((String) expectedFile);
				Reader actualFileReader = new FileReader(actualFile);) {
			CSVParser expectedFileParser = CSVFormat.DEFAULT
					.withFirstRecordAsHeader().parse(expectedFileReader);
			CSVParser actualFileParser = CSVFormat.DEFAULT
					.withFirstRecordAsHeader().parse(actualFileReader);
			if (!expectedFileParser.getHeaderMap().equals(
					actualFileParser.getHeaderMap())) {
				LOG.error("Headers in CSV file do not match");
				return false;
			}
			List<Map<String, String>> expectedList = expectedFileParser
					.getRecords().stream().map(record -> record.toMap())
					.collect(Collectors.toList());
			List<Map<String, String>> actualList = actualFileParser
					.getRecords().stream().map(record -> record.toMap())
					.collect(Collectors.toList());
			for (Map<String, String> record : expectedList) {
				if (actualList.contains(record)) {
					LOG.error("Actual csv file does not excludes the expected csv file");
					return false;
				}
			}
			LOG.info("Actual csv file excludes the expected csv file");
			return true;
		} catch (IOException e) {
			throw e;
		} finally {
			actualFile.delete();
		}
	}

	/**
	 * Assert ordered equals checks for absolute equality between expected and
	 * actual csv file. Both csv files are parsed and the contents are compared.
	 * The order of the rows in the result affects the comparison and even a
	 * slight difference in the expected and actual file will result in a
	 * failure to match. The test case executed will create a temporary csv file
	 * containing rows of actual results.
	 * 
	 * @param expectedFile
	 *            The name of local csv file containing the expected rows of
	 *            results.
	 * @param result
	 *            The result obtained after executing the test case.
	 * @throws IOException
	 *             In case, the file is not found or there is an error while
	 *             parsing the contents of the file.
	 */
	public boolean assertOrderedEquals(Object expectedFile, Result result)
			throws IOException {
		String actualFileName = result.getFileName();
		File actualFile = new File(actualFileName);
		try (Reader expectedFileReader = new FileReader((String) expectedFile);
				Reader actualFileReader = new FileReader(actualFile);) {
			CSVParser expectedFileParser = CSVFormat.DEFAULT
					.withFirstRecordAsHeader().parse(expectedFileReader);

			CSVParser actualFileParser = CSVFormat.DEFAULT
					.withFirstRecordAsHeader().parse(actualFileReader);
			if (!expectedFileParser.getHeaderMap().equals(
					actualFileParser.getHeaderMap())) {
				LOG.error("Headers in CSV file do not match");
				return false;
			}
			List<Map<String, String>> expectedList = expectedFileParser
					.getRecords().stream().map(record -> record.toMap())
					.collect(Collectors.toList());
			List<Map<String, String>> actualList = actualFileParser
					.getRecords().stream().map(record -> record.toMap())
					.collect(Collectors.toList());
			if (expectedList.size() != actualList.size()) {
				LOG.error("Number of records in csv files do not match");
				return false;
			}
			for (int i = 0; i < actualList.size(); i++) {
				if (!actualList.get(i).equals(expectedList.get(i))) {
					LOG.error("actual csv file is not same as the expected csv file");
					return false;
				}
			}
			LOG.info("actual csv file is same as the expected csv file");
			return true;
		} catch (IOException e) {
			throw e;
		} finally {
			actualFile.delete();
		}

	}

	/**
	 * Unused for csv assertions. Here only for implementation consistency.
	 * Framework should not call this if test cases are properly formed in the
	 * yaml file. In case it is called, it will throw Exception.
	 * 
	 * @throws UnsupportedOperationException
	 *             Calling this means something went wrong.
	 */
	@Override
	public boolean assertFails(Object expected, Result actual)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}
