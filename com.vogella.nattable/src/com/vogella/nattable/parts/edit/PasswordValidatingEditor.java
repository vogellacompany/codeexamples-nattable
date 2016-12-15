
package com.vogella.nattable.parts.edit;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.PasswordCellEditor;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.PasswordTextPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonHeaderDataProvider;
import com.vogella.nattable.parts.edit.validator.PasswordDataValidator;

public class PasswordValidatingEditor {

	private PasswordDataValidator pwValidator = new PasswordDataValidator();

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		List<Person> persons = personService.getPersons(10);
		ReflectiveColumnPropertyAccessor<Person> columnPropertyAccessor = new ReflectiveColumnPropertyAccessor<>(
				"firstName", "lastName");
		ListDataProvider<Person> dataProvider = new ListDataProvider<>(persons, columnPropertyAccessor);

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

				String lastNameLabel = ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 1;

				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
						IEditableRule.ALWAYS_EDITABLE);

				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new PasswordTextPainter(),
						DisplayMode.NORMAL, lastNameLabel);

				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new PasswordCellEditor(),
						DisplayMode.EDIT, lastNameLabel);

				configRegistry.registerConfigAttribute(EditConfigAttributes.DATA_VALIDATOR, pwValidator,
						DisplayMode.EDIT, lastNameLabel);
			}
		});

		natTable.configure();
	}

}