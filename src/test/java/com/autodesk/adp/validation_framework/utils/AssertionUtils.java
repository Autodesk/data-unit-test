package com.autodesk.adp.validation_framework.utils;

import java.util.Random;
import java.util.UUID;

public class AssertionUtils {
	
	private static final Random RANDOM = new Random();

	public static class RandomResultSet {
		private String[] columnNames;
		private String[][] rows;

		public RandomResultSet(String[] columnNames, String[][] rows) {
			this.columnNames = columnNames;
			this.rows = rows;
		}

		public String[] getColumnNames() {
			return columnNames;
		}

		public String[][] getRows() {
			return rows;
		}
		
		public void invertRows() {
			int len = rows.length - 1;
			for(int i=0; i < len/2; i++) {
				String[] tmp = rows[i];
				rows[i] = rows[len - i];
				rows[len - i] = tmp;
			}
		}
		
		public void repeatRows() {
			int length = 2*rows.length;
			String[][] temp = new String[length][];
			for(int i=0; i < length; i+=2) {
				temp[i] = temp[i+1] = rows[i/2];
			}
			rows = temp;
		}
		
		public void replaceRandomRow() {
			int index = RANDOM.nextInt(rows.length);
			for(int i=0; i < columnNames.length; i++) {
				rows[index][i] = UUID.randomUUID().toString();
			}
		}
		
		public void replaceAllRows() {
			for(int j=0; j < rows.length; j++) {
				for(int i=0; i < columnNames.length; i++) {
					rows[j][i] = UUID.randomUUID().toString();
				}
			}
		}

	}

	public static RandomResultSet createRandomResultSet() {
		int numColumns = RANDOM.nextInt(5) + 2;
		int numRows = RANDOM.nextInt(10) + 2;
		String[] columns = new String[numColumns];
		String[][] rows = new String[numRows][numColumns];
		for (int i = 0; i < numColumns; i++) {
			columns[i] = "column_" + i;
			for (int j = 0; j < numRows; j++) {
				rows[j][i] = UUID.randomUUID().toString();
			}
		}
		return new RandomResultSet(columns, rows);
	}

}
