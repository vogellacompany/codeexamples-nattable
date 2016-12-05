package com.vogella.nattable.parts.tree;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.ExtendedReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDateDisplayConverter;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.tree.config.TreeLayerExpandCollapseKeyBindings;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.todo.Todo;
import com.vogella.model.todo.TodoService;
import com.vogella.model.tree.TreeItem;

public class TreePart {

	@PostConstruct
	public void createComposite(Composite parent, TodoService todoService) {
		parent.setLayout(new GridLayout(1, false));

		// create a new ConfigRegistry which will be needed for GlazedLists
		// handling
		ConfigRegistry configRegistry = new ConfigRegistry();

		// property names of the Person class
		String[] propertyNames = { "item.summary", "item.description" };

		// mapping from property to label, needed for column header labels
		Map<String, String> propertyToLabelMap = new HashMap<String, String>();
		propertyToLabelMap.put("item.summary", "Summary");
		propertyToLabelMap.put("item.description", "Description");

		IColumnPropertyAccessor<TreeItem<Todo>> columnPropertyAccessor = new ExtendedReflectiveColumnPropertyAccessor<TreeItem<Todo>>(
				propertyNames);

		final BodyLayerStack<TreeItem<Todo>> bodyLayerStack = new BodyLayerStack<TreeItem<Todo>>(
				todoService.getSampleTodoTreeItems(), columnPropertyAccessor, new TreeItemFormat());

		// build the column header layer
		IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
		DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayerStack,
				bodyLayerStack.getSelectionLayer());

		// build the row header layer
		IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyLayerStack.getBodyDataProvider());
		DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
		ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, bodyLayerStack,
				bodyLayerStack.getSelectionLayer());

		// build the corner layer
		IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider,
				rowHeaderDataProvider);
		DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
		ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, columnHeaderLayer);

		// build the grid layer
		GridLayer gridLayer = new GridLayer(bodyLayerStack, columnHeaderLayer, rowHeaderLayer, cornerLayer);

		// turn the auto configuration off as we want to add our header menu
		// configuration
		final NatTable natTable = new NatTable(parent, gridLayer, false);
		// bodyLayerStack.setRegionName(GridRegion.BODY);
		// final NatTable natTable = new NatTable(parent, bodyLayerStack,
		// false);

		// as the autoconfiguration of the NatTable is turned off, we have to
		// add the DefaultNatTableStyleConfiguration and the ConfigRegistry
		// manually
		natTable.setConfigRegistry(configRegistry);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());

		natTable.addConfiguration(new AbstractRegistryConfiguration() {

			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				// register a CheckBoxPainter as CellPainter for the married
				// information
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(),
						DisplayMode.NORMAL, ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 3);

				configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
						new DefaultDateDisplayConverter("MM/dd/yyyy"), DisplayMode.NORMAL,
						ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 4);

			}
		});

		// adds the key bindings that allows pressing space bar to
		// expand/collapse tree nodes
		natTable.addConfiguration(new TreeLayerExpandCollapseKeyBindings(bodyLayerStack.getTreeLayer(),
				bodyLayerStack.getSelectionLayer()));

		natTable.configure();

		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);

	}
}