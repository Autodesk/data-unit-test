package com.autodesk.adp.validation_framework.assertions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autodesk.adp.validation_framework.utils.ASSERTTYPE;
import com.autodesk.adp.validation_framework.utils.Result;

/**
 * The Class ListAssertions. Checks for various supported assertions (specified
 * in {@link ASSERTTYPE}) The expected object must be in form of a list of
 * objects. This can be achieved by specifying the expected output in form of
 * List. The execution of tests generates the output in form of list
 * of objects which is then matched against the expected list.
 */
public class ListAssertions implements Assertions {

	/** The Constant LOG. Used for logging */
	private static final Logger LOG = LoggerFactory
			.getLogger(ListAssertions.class);

	/**
	 * Assert equals checks for equality between expected and actual outputs.
	 * Both expected and output lists are first compared for size. If this check
	 * passes, the contents are compared against each other. The order of the
	 * rows in the expected and actual outputs do not matter, only the content
	 * is compared and if the contents match, the assertion succeeds.
	 * 
	 * @param expectedList
	 *            The expected output in form of List.
	 * @param result
	 *            The result obtained after executing the test case.
	 */
	public boolean assertEquals(Object expectedList, Result result) {
		@SuppressWarnings("unchecked")
		List<Object> expectedArray = (List<Object>) expectedList;
		List<Object> actualArray = result.getList();
		if (actualArray.size() != expectedArray.size()) {
			LOG.error("Number of records in actual list does not match the number of records in expected list");
			return false;
		}
		if (!actualArray.containsAll(expectedArray)) {
			LOG.error("Actual list does not match with expected list. Expected: "
					+ expectedArray.toString()
					+ ", actual: "
					+ actualArray.toString());
			return false;
		}
		LOG.info("Actual list matches with the expected list. Expected: "
				+ expectedArray.toString() + ", actual: "
				+ actualArray.toString());
		return true;
	}

	/**
	 * Assert includes checks for the inclusion of expected output in the actual
	 * output. The order of the rows in the expected and actual outputs do not
	 * matter, only the content is compared and if all the contents of expected
	 * output are present in the actual output, the assertion succeeds.
	 * 
	 * @param expectedList
	 *            The expected output in form of List.
	 * @param result
	 *            The result obtained after executing the test case.
	 */
	public boolean assertIncludes(Object expectedList, Result result) {
		@SuppressWarnings("unchecked")
		List<Object> expectedArray = (List<Object>) expectedList;
		List<Object> actualArray = result.getList();
		if (!actualArray.containsAll(expectedArray)) {
			LOG.error("Actual list does not include expected list");
			return false;
		}
		LOG.info("Actual list includes the expected list");
		return true;
	}

	/**
	 * Assert excludes checks for the exclusion of expected output in the actual
	 * output. The order of the rows in the expected and actual outputs do not
	 * matter, only the content is compared and if all the contents of expected
	 * output are absent from the actual output, the assertion succeeds.
	 * 
	 * @param expectedList
	 *            The expected output in form of List.
	 * @param result
	 *            The result obtained after executing the test case.
	 */
	public boolean assertExcludes(Object expectedList, Result result) {
		@SuppressWarnings("unchecked")
		List<Object> expectedArray = (List<Object>) expectedList;
		List<Object> actualArray = result.getList();
		for (int i = 0; i < expectedArray.size(); i++) {
			if (actualArray.contains(expectedArray.get(i))) {
				LOG.error("Actual list does not exclude expected list");
				return false;
			}
		}
		LOG.info("Actual list excludes the expected list");
		return true;
	}

	/**
	 * Assert ordered equals checks for equality between expected and actual
	 * outputs. Both expected and output lists are first compared for size. If
	 * this check passes, the contents are compared against each other. The
	 * order of the rows in the expected and actual outputs are also considered
	 * and each row of the expected output should match exactly with each row of
	 * the actual output for the assertion to succeed.
	 * 
	 * @param expectedList
	 *            The expected output in form of List.
	 * @param result
	 *            The result obtained after executing the test case.
	 */
	public boolean assertOrderedEquals(Object expectedList, Result result) {
		@SuppressWarnings("unchecked")
		List<Object> expectedArray = (List<Object>) expectedList;
		List<Object> actualArray = result.getList();
		if (actualArray.size() != expectedArray.size()) {
			LOG.error("Number of records in actual list does not match the number of records in expected list");
			return false;
		}
		for (int i = 0; i < actualArray.size(); i++) {
			if (!actualArray.get(i).equals(expectedArray.get(i))) {
				LOG.error("Actual list does not match the expected list");
				return false;
			}
		}
		LOG.info("Actual list matches with the expected list");
		return true;
	}

	/**
	 * Unused for List assertions. Here only for implementation consistency.
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
