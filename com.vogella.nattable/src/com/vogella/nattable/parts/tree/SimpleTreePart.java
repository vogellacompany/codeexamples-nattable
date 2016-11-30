package com.vogella.nattable.parts.tree;

import java.util.HashMap;
import java.util.Map;

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
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.Todo;
import com.vogella.model.TodoService;
import com.vogella.model.TreeItem;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TreeList;

public class SimpleTreePart {

	@PostConstruct
	public void createComposite(Composite parent, TodoService todoService) {
		// property names of the Person class
		String[] propertyNames = { "item.summary", "item.description" };

		// mapping from property to label, needed for column header labels
		Map<String, String> propertyToLabelMap = new HashMap<String, String>();
		propertyToLabelMap.put("item.summary", "Summary");
		propertyToLabelMap.put("item.description", "Description");

		IColumnPropertyAccessor<TreeItem<Todo>> columnPropertyAccessor = new ExtendedReflectiveColumnPropertyAccessor<TreeItem<Todo>>(
				propertyNames);

		EventList<TreeItem<Todo>> eventList = GlazedLists.eventList(todoService.getSampleTodoTreeItems());
		TreeList<TreeItem<Todo>> treeList = new TreeList<TreeItem<Todo>>(eventList, new TreeItemFormat(),
				TreeList.nodesStartExpanded());
		ListDataProvider<TreeItem<Todo>> dataProvider = new ListDataProvider<>(treeList, columnPropertyAccessor);
		DataLayer dataLayer = new DataLayer(dataProvider);

		GlazedListTreeData<TreeItem<Todo>> glazedListTreeData = new GlazedListTreeData<>(treeList);
		GlazedListTreeRowModel<TreeItem<Todo>> glazedListTreeRowModel = new GlazedListTreeRowModel<>(
				glazedListTreeData);

		TreeLayer treeLayer = new TreeLayer(dataLayer, glazedListTreeRowModel);

		dataLayer.setRegionName(GridRegion.BODY);
		new NatTable(parent, treeLayer, true);

		GridLayoutFactory.fillDefaults().generateLayout(parent);
	}
}
