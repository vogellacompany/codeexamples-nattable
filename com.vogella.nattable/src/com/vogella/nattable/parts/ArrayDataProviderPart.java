
package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.vogella.nattable.data.TwoDimensionalArrayDataProvider;

public class ArrayDataProviderPart {

	@PostConstruct
	public void postConstruct(Composite parent) {

		String[][] testData = new String[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				testData[i][j] = "" + i + "/" + j;
			}
		}

		final DataLayer bodyDataLayer = new DataLayer(new TwoDimensionalArrayDataProvider(testData));
		bodyDataLayer.setDefaultColumnWidth(30);

		new NatTable(parent, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED, bodyDataLayer);

	}

}
