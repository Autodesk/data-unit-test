package com.autodesk.adp.validation_framework.assertions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.autodesk.adp.validation_framework.utils.AssertionUtils;
import com.autodesk.adp.validation_framework.utils.AssertionUtils.RandomResultSet;
import com.autodesk.adp.validation_framework.utils.RETURNTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

public class JSONAssertionTest {

	private static final JSONAssertions ASSERTION = new JSONAssertions();

	private List<Object> getRandomResults(RandomResultSet results,
			RETURNTYPE returnType) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (String[] row : results.getRows()) {
			switch (returnType) {
			case MAP:
				HashMap<String, String> hm = new HashMap<String, String>();
				String[] columns = results.getColumnNames();
				for (int i = 0; i < columns.length; i++) {
					hm.put(columns[i], row[i]);
				}
				list.add(hm);
				break;
			case LIST:
				ArrayList<String> al = new ArrayList<String>();
				columns = results.getColumnNames();
				for (int i = 0; i < columns.length; i++) {
					al.add(row[i]);
				}
				list.add(al);
				break;
			default:
				break;
			}
		}
		return list;
	}

	@Test
	public void testAssertEqualsNormal() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expected = getRandomResults(results, RETURNTYPE.MAP);
		Assert.assertTrue(ASSERTION.assertEquals(expected, new Result(
				RETURNTYPE.MAP, new JSONArray(expected), null, null)));
		expected = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertTrue(ASSERTION.assertEquals(expected, new Result(
				RETURNTYPE.LIST, new JSONArray(expected), null, null)));
	}

	@Test
	public void testAssertEqualsUnequalLength() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.repeatRows();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertFalse(ASSERTION.assertEquals(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertFalse(ASSERTION.assertEquals(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

	@Test
	public void testAssertEqualsInvertedOrder() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.invertRows();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertTrue(ASSERTION.assertEquals(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertTrue(ASSERTION.assertEquals(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

	@Test
	public void testAssertEqualsNegetiveMatch() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.replaceRandomRow();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertFalse(ASSERTION.assertEquals(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertFalse(ASSERTION.assertEquals(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

	@Test
	public void testAssertIncludesNormal() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.repeatRows();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertTrue(ASSERTION.assertIncludes(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertTrue(ASSERTION.assertIncludes(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

	@Test
	public void testAssertIncludesNegetiveMatch() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.replaceRandomRow();
		results.invertRows();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertFalse(ASSERTION.assertIncludes(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertFalse(ASSERTION.assertIncludes(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

	@Test
	public void testAssertExcludesNormal() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.replaceAllRows();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertTrue(ASSERTION.assertExcludes(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertTrue(ASSERTION.assertExcludes(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

	@Test
	public void testAssertExcludesNegetiveMatch() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.replaceRandomRow();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertFalse(ASSERTION.assertExcludes(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertFalse(ASSERTION.assertExcludes(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

	@Test
	public void testAssertOrderedEqualsNormal() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expected = getRandomResults(results, RETURNTYPE.MAP);
		Assert.assertTrue(ASSERTION.assertOrderedEquals(expected, new Result(
				RETURNTYPE.MAP, new JSONArray(expected), null, null)));
		expected = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertTrue(ASSERTION.assertOrderedEquals(expected, new Result(
				RETURNTYPE.LIST, new JSONArray(expected), null, null)));
	}

	@Test
	public void testAssertOrderedEqualsInvertedOrder() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.invertRows();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertFalse(ASSERTION.assertOrderedEquals(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertFalse(ASSERTION.assertOrderedEquals(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

	@Test
	public void testAssertOrderedEqualsNegetiveMatch() {
		RandomResultSet results = AssertionUtils.createRandomResultSet();
		List<Object> expectedListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> expectedListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		results.repeatRows();
		List<Object> actualListOfMaps = getRandomResults(results, RETURNTYPE.MAP);
		List<Object> actualListOfLists = getRandomResults(results, RETURNTYPE.LIST);
		Assert.assertFalse(ASSERTION.assertOrderedEquals(expectedListOfMaps, new Result(
				RETURNTYPE.MAP, new JSONArray(actualListOfMaps), null, null)));
		Assert.assertFalse(ASSERTION.assertOrderedEquals(expectedListOfLists, new Result(
				RETURNTYPE.LIST, new JSONArray(actualListOfLists), null, null)));
	}

}
