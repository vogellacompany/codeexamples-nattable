package com.vogella.nattable.parts.tree;

import java.util.Comparator;
import java.util.List;

import com.vogella.model.Todo;
import com.vogella.model.TreeItem;

import ca.odell.glazedlists.TreeList;

/**
 * Simple TreeList.Format implementation that uses the lastname of the Todo
 * object as tree item.
 * <p>
 * Using a String directly as the tree item has the possible disadvantage of
 * haven non-unique items in the tree within subtrees.
 */
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
		return true;
	}

	@Override
	public Comparator<? super TreeItem<Todo>> getComparator(int depth) {
		return (o1, o2) -> o1.getItem().getSummary().compareTo(o2.getItem().getSummary());
	}
}