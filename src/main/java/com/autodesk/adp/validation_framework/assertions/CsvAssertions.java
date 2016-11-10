package com.autodesk.adp.validation_framework.assertions;

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

public class CsvAssertions implements Assertions {

	private static final Logger LOG = LoggerFactory.getLogger(CsvAssertions.class);

	public boolean assertEquals(String expectedFile, String actualFile)
			throws IOException {
		try (Reader expectedFileReader = new FileReader(expectedFile);
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
				System.out.println(actualList);
				System.out.println(expectedList);
				LOG.error("Contents of csv file do not match");
				return false;
			} else {
				LOG.info("CSV files matched");
				return true;
			}
		} catch (IOException e) {
			throw e;
		}

	}

	public boolean assertIncludes(String expectedFile, String actualFile)
			throws IOException {
		try (Reader expectedFileReader = new FileReader(expectedFile);
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
		}
	}

	public boolean assertExcludes(String expectedFile, String actualFile)
			throws IOException {
		try (Reader expectedFileReader = new FileReader(expectedFile);
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
		}
	}

	public boolean assertOrderedEquals(String expectedFile, String actualFile)
			throws IOException {
		try (Reader expectedFileReader = new FileReader(expectedFile);
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
		}

	}

}
