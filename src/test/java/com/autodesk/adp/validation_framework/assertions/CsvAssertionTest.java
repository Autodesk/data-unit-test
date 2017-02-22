package com.autodesk.adp.validation_framework.assertions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.autodesk.adp.validation_framework.utils.AssertionUtils;
import com.autodesk.adp.validation_framework.utils.AssertionUtils.RandomResultSet;
import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

public class CsvAssertionTest {
	
	private static final CsvAssertions ASSERTION = new CsvAssertions();
	
	private String createRandomCSVFile(boolean hasHeader, RandomResultSet results) {
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
		String fileName = UUID.randomUUID().toString() + ".csv";
		try (FileWriter writer = new FileWriter(fileName);
				CSVPrinter printer = new CSVPrinter(writer, csvFileFormat)) {
			if (hasHeader) {
				printer.printRecord(results.getColumnNames());
			}
			for(Object[] row : results.getRows())
				printer.printRecord(row);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	@Test
	public void testAssertEqualsNormal() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String csvFile = createRandomCSVFile(true, results);
		Assert.assertTrue(ASSERTION.assertEquals(csvFile, new Result(
				RETURNTYPE.CSV, null, csvFile, null)));
		deleteFiles(csvFile);
	}
	
	@Test
	public void testAssertEqualNoOrder() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.invertRows();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertTrue(ASSERTION.assertEquals(actualFile, new Result(
				RETURNTYPE.CSV, null, expectedFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertEqualsNoHeader() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		String expectedFile = createRandomCSVFile(false, results);
		Assert.assertFalse(ASSERTION.assertEquals(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertEqualsUnEqualLength() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.repeatRows();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertFalse(ASSERTION.assertEquals(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertEqualsNegetiveMatch() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.replaceRandomRow();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertFalse(ASSERTION.assertEquals(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertIncludesNormal() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.repeatRows();
		results.invertRows();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertTrue(ASSERTION.assertIncludes(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertIncludesNoHeader() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.repeatRows();
		String expectedFile = createRandomCSVFile(false, results);
		Assert.assertFalse(ASSERTION.assertIncludes(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertIncludesNegetiveMatch() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.replaceRandomRow();
		results.repeatRows();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertFalse(ASSERTION.assertIncludes(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertExcludesNormal() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.replaceAllRows();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertTrue(ASSERTION.assertExcludes(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertExcludesNoHeader() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.replaceAllRows();
		String expectedFile = createRandomCSVFile(false, results);
		Assert.assertFalse(ASSERTION.assertExcludes(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertExcludesNegetiveMatch() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.replaceRandomRow();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertFalse(ASSERTION.assertExcludes(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertOrderedEqualsNormal() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String csvFile = createRandomCSVFile(true, results);
		Assert.assertTrue(ASSERTION.assertOrderedEquals(csvFile, new Result(
				RETURNTYPE.CSV, null, csvFile, null)));
		deleteFiles(csvFile);
	}
	
	@Test
	public void testAssertOrderedEqualNoOrder() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.invertRows();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertFalse(ASSERTION.assertOrderedEquals(actualFile, new Result(
				RETURNTYPE.CSV, null, expectedFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertOrderedEqualsNoHeader() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		String expectedFile = createRandomCSVFile(false, results);
		Assert.assertFalse(ASSERTION.assertOrderedEquals(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertOrderedEqualsUnEqualLength() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.repeatRows();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertFalse(ASSERTION.assertOrderedEquals(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	@Test
	public void testAssertOrderedEqualsNegetiveMatch() throws IOException {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		String actualFile = createRandomCSVFile(true, results);
		results.replaceRandomRow();
		String expectedFile = createRandomCSVFile(true, results);
		Assert.assertFalse(ASSERTION.assertOrderedEquals(expectedFile, new Result(
				RETURNTYPE.CSV, null, actualFile, null)));
		deleteFiles(actualFile, expectedFile);
	}
	
	
	
	private void deleteFiles(String... files) {
		for(String file : files) {
			File f = new File(file);
			if(f.exists())
				f.delete();
		}
	}

}
