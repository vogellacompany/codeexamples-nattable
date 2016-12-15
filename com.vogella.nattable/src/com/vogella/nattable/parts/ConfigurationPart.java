
package com.vogella.nattable.parts;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.ConfigAttribute;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellLabelMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonDataProvider;

public class ConfigurationPart {

	private static final String TOP_LEFT_CORNER = "TopRightCorner";

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		List<Person> persons = personService.getPersons(10);
		PersonDataProvider personDataProvider = new PersonDataProvider(persons);
		DataLayer bodyDataLayer = new DataLayer(personDataProvider);

		bodyDataLayer.setConfigLabelAccumulator((configLabels, columnPosition, rowPosition) -> {
			if (columnPosition == 0 && rowPosition == 0)
				configLabels.addLabel(TOP_LEFT_CORNER);
		});
		bodyDataLayer.setRegionName(GridRegion.BODY);

		NatTable natTable = new NatTable(parent, bodyDataLayer, false);

		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new AbstractRegistryConfiguration() {

			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				ConfigAttribute<String> configAttribute = new ConfigAttribute<>();
				String useless = "useless";
				configRegistry.registerConfigAttribute(configAttribute, useless, DisplayMode.NORMAL, TOP_LEFT_CORNER);
			}
		});

		natTable.addConfiguration(new AbstractUiBindingConfiguration() {

			@Override
			public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
				uiBindingRegistry.registerDoubleClickBinding(
						new CellLabelMouseEventMatcher(GridRegion.BODY, MouseEventMatcher.LEFT_BUTTON, TOP_LEFT_CORNER),
						(table, event) -> {
							MessageDialog.openInformation(parent.getShell(), "CellLabelMouseEventMatcher",
									"You double clicked on a cell with the TopRightCorner label.");
						});
			}
		});

		natTable.addConfiguration(new AbstractRegistryConfiguration() {
			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
						IEditableRule.ALWAYS_EDITABLE);

				IStyle style = new Style();
				style.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_BLUE);
				style.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, GUIHelper.COLOR_WHITE);

				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, style, DisplayMode.NORMAL,
						TOP_LEFT_CORNER);
			}
		});

		natTable.configure();
	}
}