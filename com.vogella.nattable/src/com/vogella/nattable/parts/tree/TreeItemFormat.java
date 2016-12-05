package com.vogella.nattable.parts.tree;

import java.util.Comparator;
import java.util.List;

import com.vogella.model.todo.Todo;
import com.vogella.model.tree.TreeItem;

import ca.odell.glazedlists.TreeList;

class TreeItemFormat implements TreeList.Format<TreeItem<Todo>> {

	@Override
	public void getPath(List<TreeItem<Todo>> path, TreeItem<Todo> element) {
		if (element.getParent() != null) {
			path.add(element.getParent());
		}
		path.add(element);
	}

	@Override
	public boolean allowsChildren(TreeItem<Todo> element) {
		return element.hasChildren();
	}

	@Override
	public Comparator<? super TreeItem<Todo>> getComparator(int depth) {
		return (o1, o2) -> o1.getItem().getSummary().compareTo(o2.getItem().getSummary());
	}
}