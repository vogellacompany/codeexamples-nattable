package com.vogella.nattable.parts.dnd;

import javax.annotation.PostConstruct;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class DnDPart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		DefaultGridLayer defaultGridLayer = new DefaultGridLayer(personService.getPersons(10),
				personService.getDefaultPropertyNames(), personService.getDefaultPropertyToLabelMap());

		SelectionLayer selectionLayer = defaultGridLayer.getBodyLayer().getSelectionLayer();
		@SuppressWarnings("unchecked")
		IRowDataProvider<Person> rowDataProvider = ((IRowDataProvider<Person>) ((DataLayer) defaultGridLayer
				.getBodyDataLayer()).getDataProvider());

		// set row selection model with single selection enabled
		selectionLayer.setSelectionModel(
				new RowSelectionModel<>(selectionLayer, rowDataProvider, rowObject -> rowObject.getId(), false));

		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getTransfer(), TextTransfer.getInstance() };
		NatTable natTable = new NatTable(parent, defaultGridLayer);
		natTable.addDragSupport(DND.DROP_COPY, transfers, new NatTableDragSourceListener(natTable, selectionLayer));
	}
}
