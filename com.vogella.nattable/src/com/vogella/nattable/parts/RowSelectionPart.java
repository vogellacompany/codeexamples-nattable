
package com.vogella.nattable.parts;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.coordinate.Range;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionProvider;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultRowSelectionLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.selection.event.CellSelectionEvent;
import org.eclipse.nebula.widgets.nattable.selection.event.ColumnSelectionEvent;
import org.eclipse.nebula.widgets.nattable.selection.event.RowSelectionEvent;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonColumnPropertyAccessor;

public class RowSelectionPart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		List<Person> data = personService.getPersons(10);
		IRowDataProvider<Person> bodyDataProvider = new ListDataProvider<>(data, new PersonColumnPropertyAccessor());
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		selectionLayer
				.setSelectionModel(new RowSelectionModel<>(selectionLayer, bodyDataProvider, person -> person.getId()));
		RowSelectionProvider<Person> rowSelectionProvider = new RowSelectionProvider<>(selectionLayer,
				bodyDataProvider);
		rowSelectionProvider.addSelectionChangedListener(selectionChangeEvent -> {
			ISelection selection = selectionChangeEvent.getSelection();
			System.out.println(selection);
		});

		selectionLayer.addConfiguration(new DefaultRowSelectionLayerConfiguration());

		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
		viewportLayer.setRegionName(GridRegion.BODY);

		NatTable natTable = new NatTable(parent, viewportLayer);

		natTable.addLayerListener(layerEvent -> {
			if (layerEvent instanceof CellSelectionEvent) {
				CellSelectionEvent cellEvent = (CellSelectionEvent) layerEvent;
				System.out.println(cellEvent.getColumnPosition() + " : " + cellEvent.getRowPosition());
			} else if (layerEvent instanceof ColumnSelectionEvent) {
				ColumnSelectionEvent columnEvent = (ColumnSelectionEvent) layerEvent;
				Collection<Range> columnPositionRanges = columnEvent.getColumnPositionRanges();
				System.out.println(columnPositionRanges);
			} else if (layerEvent instanceof RowSelectionEvent) {
				RowSelectionEvent rowSelectionEvent = (RowSelectionEvent) layerEvent;
				Collection<Range> rowPositionRanges = rowSelectionEvent.getRowPositionRanges();
				System.out.println(rowPositionRanges);
			}
		});
	}

}
