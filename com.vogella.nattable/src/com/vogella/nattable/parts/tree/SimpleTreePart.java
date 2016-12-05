package com.vogella.nattable.parts.tree;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.ExtendedReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.tree.GlazedListTreeData;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.tree.GlazedListTreeRowModel;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.tree.TreeLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.vogella.model.todo.Todo;
import com.vogella.model.todo.TodoService;
import com.vogella.model.tree.TreeItem;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TreeList;

public class SimpleTreePart {

	@PostConstruct
	public void createComposite(Composite parent, TodoService todoService) {
		// Properties of the Todo items inside the TreeItems
		String[] propertyNames = { "item.summary", "item.description" };
		
		IColumnPropertyAccessor<TreeItem<Todo>> columnPropertyAccessor = new ExtendedReflectiveColumnPropertyAccessor<TreeItem<Todo>>(
				propertyNames);

		EventList<TreeItem<Todo>> eventList = GlazedLists.eventList(todoService.getSampleTodoTreeItems());
		TreeList<TreeItem<Todo>> treeList = new TreeList<TreeItem<Todo>>(eventList, new TreeItemFormat(),
				TreeList.nodesStartExpanded());
		ListDataProvider<TreeItem<Todo>> dataProvider = new ListDataProvider<>(treeList, columnPropertyAccessor);
		DataLayer dataLayer = new DataLayer(dataProvider);
		setColumWidthPercentage(dataLayer);

		GlazedListTreeData<TreeItem<Todo>> glazedListTreeData = new GlazedListTreeData<>(treeList);
		GlazedListTreeRowModel<TreeItem<Todo>> glazedListTreeRowModel = new GlazedListTreeRowModel<>(
				glazedListTreeData);

		TreeLayer treeLayer = new TreeLayer(dataLayer, glazedListTreeRowModel);
		treeLayer.setRegionName(GridRegion.BODY);

		new NatTable(parent, treeLayer, true);

		GridLayoutFactory.fillDefaults().generateLayout(parent);
	}

	private void setColumWidthPercentage(DataLayer dataLayer) {
		dataLayer.setColumnPercentageSizing(true);
		dataLayer.setColumnWidthPercentageByPosition(0, 50);
		dataLayer.setColumnWidthPercentageByPosition(1, 50);
	}
}
