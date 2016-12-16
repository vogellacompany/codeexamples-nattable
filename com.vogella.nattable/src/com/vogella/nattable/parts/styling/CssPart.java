package com.vogella.nattable.parts.styling;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.AggregateConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.RowOverrideLabelAccumulator;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class CssPart {

	private static final String THIRD_ROW = "thirdRow";

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService, IStylingEngine stylingEngine) {

		DefaultGridLayer gridLayer = new DefaultGridLayer(personService.getPersons(10),
				personService.getDefaultPropertyNames(), personService.getDefaultPropertyToLabelMap());

		DataLayer bodyDataLayer = (DataLayer) gridLayer.getBodyDataLayer();
		@SuppressWarnings("unchecked")
		IRowDataProvider<Person> bodyDataProvider = ((IRowDataProvider<Person>) bodyDataLayer.getDataProvider());

		AggregateConfigLabelAccumulator aggregateConfigLabelAccumulator = new AggregateConfigLabelAccumulator();
		aggregateConfigLabelAccumulator.add(new ColumnLabelAccumulator(bodyDataProvider));
		RowOverrideLabelAccumulator<Person> rowOverrideLabelAccumulator = new RowOverrideLabelAccumulator<>(
				bodyDataProvider, person -> person.getId());
		rowOverrideLabelAccumulator.registerRowOverrides(2, THIRD_ROW);
		aggregateConfigLabelAccumulator.add(rowOverrideLabelAccumulator);
		bodyDataLayer.setConfigLabelAccumulator(aggregateConfigLabelAccumulator);

		NatTable natTable = new NatTable(parent, gridLayer);
		stylingEngine.setClassname(natTable, "cssVogella");

		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
}
