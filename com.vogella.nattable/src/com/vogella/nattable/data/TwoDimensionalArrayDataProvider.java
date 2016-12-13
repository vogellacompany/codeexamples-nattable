package com.vogella.nattable.data;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;

public class TwoDimensionalArrayDataProvider implements IDataProvider {

	private String[][] data;

	public TwoDimensionalArrayDataProvider(String[][] data) {
		this.data = data;
	}

	@Override
	public Object getDataValue(int columnIndex, int rowIndex) {
		return this.data[columnIndex][rowIndex];
	}

	@Override
	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		this.data[columnIndex][rowIndex] = newValue != null ? newValue.toString() : null;
	}

	@Override
	public int getColumnCount() {
		return this.data.length;
	}

	@Override
	public int getRowCount() {
		return this.data[0] != null ? this.data[0].length : 0;
	}
}