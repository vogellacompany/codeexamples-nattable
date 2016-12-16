
package com.vogella.nattable.parts.edit;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.gui.ICellEditDialog;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonColumnPropertyAccessor;
import com.vogella.nattable.data.PersonHeaderDataProvider;

public class DialogEditor {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		List<Person> persons = personService.getPersons(10);
		ListDataProvider<Person> dataProvider = new ListDataProvider<>(persons, new PersonColumnPropertyAccessor());

		PersonHeaderDataProvider headerDataProvider = new PersonHeaderDataProvider();

		DefaultGridLayer gridLayer = new DefaultGridLayer(dataProvider, headerDataProvider);

		// Apply a ColumnLabelAccumulator to address the columns in the
		// EditConfiguration class
		ColumnLabelAccumulator columnLabelAccumulator = new ColumnLabelAccumulator(dataProvider);
		((DataLayer) gridLayer.getBodyDataLayer()).setConfigLabelAccumulator(columnLabelAccumulator);

		NatTable natTable = new NatTable(parent, gridLayer, false);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new AbstractRegistryConfiguration() {

			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				String firstNameLabel = ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 0;

				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
						IEditableRule.ALWAYS_EDITABLE);

				configRegistry.registerConfigAttribute(EditConfigAttributes.OPEN_IN_DIALOG, Boolean.TRUE,
						DisplayMode.EDIT, firstNameLabel);

				configRegistry.registerConfigAttribute(EditConfigAttributes.EDIT_DIALOG_SETTINGS,
						Collections.singletonMap(ICellEditDialog.DIALOG_SHELL_TITLE, "Overriden the title"),
						DisplayMode.EDIT, firstNameLabel);
			}
		});

		natTable.configure();
	}

}