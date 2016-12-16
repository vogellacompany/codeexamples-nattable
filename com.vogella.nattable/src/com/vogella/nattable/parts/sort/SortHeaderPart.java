package com.vogella.nattable.parts.sort;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsSortModel;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonColumnPropertyAccessor;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;

public class SortHeaderPart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		ConfigRegistry configRegistry = new ConfigRegistry();

		EventList<Person> persons = GlazedLists.eventList(personService.getPersons(10));
		SortedList<Person> sortedList = new SortedList<>(persons, null);

		IColumnPropertyAccessor<Person> accessor = new PersonColumnPropertyAccessor();
		IDataProvider bodyDataProvider = new ListDataProvider<>(sortedList, accessor);
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(
				personService.getDefaultPropertyNames(), personService.getDefaultPropertyToLabelMap());
		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);

		SortHeaderLayer<Person> sortHeaderLayer = new SortHeaderLayer<>(columnHeaderLayer,
				new GlazedListsSortModel<>(sortedList, accessor, configRegistry, columnHeaderDataLayer));

		CompositeLayer compositeLayer = new CompositeLayer(1, 2);
		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, sortHeaderLayer, 0, 0);
		compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);

		NatTable natTable = new NatTable(parent, compositeLayer, false);

		natTable.setConfigRegistry(configRegistry);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new SingleClickSortConfiguration());

		natTable.configure();
	}
}
