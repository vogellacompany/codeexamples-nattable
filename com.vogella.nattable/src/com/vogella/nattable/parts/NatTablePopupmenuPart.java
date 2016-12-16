package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonColumnPropertyAccessor;

public class NatTablePopupmenuPart {
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService, EMenuService menuService) {
		parent.setLayout(new GridLayout());

		IRowDataProvider<Person> bodyDataProvider = new ListDataProvider<Person>(personService.getPersons(10),
				new PersonColumnPropertyAccessor());

		final DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
		viewportLayer.setRegionName(GridRegion.BODY);

		final NatTable natTable = new NatTable(parent, viewportLayer, false);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());

		menuService.registerContextMenu(natTable, "com.vogella.nattable.popupmenu");

		// get the menu registered by EMenuService
		final Menu e4Menu = natTable.getMenu();

		// remove the menu reference from NatTable instance
		natTable.setMenu(null);

		natTable.addConfiguration(new AbstractUiBindingConfiguration() {

			@Override
			public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
				// add NatTable menu items and register the DisposeListener
				new PopupMenuBuilder(natTable, e4Menu).build();

				// uncomment this to automatically add an inspection command to
				// the view
				// new PopupMenuBuilder(natTable,
				// e4Menu).withInspectLabelsMenuItem().build();

				// register the UI binding
				uiBindingRegistry.registerMouseDownBinding(
						new MouseEventMatcher(SWT.NONE, GridRegion.BODY, MouseEventMatcher.RIGHT_BUTTON),
						new PopupMenuAction(e4Menu));
			}
		});

		natTable.configure();

		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
}
