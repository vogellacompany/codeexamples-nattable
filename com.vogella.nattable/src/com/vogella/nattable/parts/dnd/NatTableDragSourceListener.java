package com.vogella.nattable.parts.dnd;

import java.util.List;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

import com.vogella.model.person.Person;

class NatTableDragSourceListener implements DragSourceListener {

	private static final String DATA_SEPARATOR = ",";

	private SelectionLayer selectionLayer;
	private NatTable natTable;

	public NatTableDragSourceListener(NatTable table, SelectionLayer selectionLayer) {
		this.natTable = table;
		this.selectionLayer = selectionLayer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		if (selectionLayer.getSelectedRowCount() == 0) {
			event.doit = false;
		} else if (!natTable.getRegionLabelsByXY(event.x, event.y).hasLabel(GridRegion.BODY)) {
			event.doit = false;
		}
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		// we know that we use the RowSelectionModel with single
		// selection
		List<Person> selection = ((RowSelectionModel<Person>) selectionLayer.getSelectionModel())
				.getSelectedRowObjects();

		if (!selection.isEmpty()) {
			Person person = selection.get(0);
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				StringBuilder builder = new StringBuilder();
				builder.append(person.getId()).append(DATA_SEPARATOR).append(person.getFirstName())
						.append(DATA_SEPARATOR).append(person.getLastName()).append(DATA_SEPARATOR)
						.append(person.getGender()).append(DATA_SEPARATOR).append(person.isMarried())
						.append(DATA_SEPARATOR).append(person.getBirthday());
				event.data = builder.toString();
			} else if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType)) {
				LocalSelectionTransfer.getTransfer().setSelection(new StructuredSelection(person));
			}
		}

	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		// clear selection
		selectionLayer.clear();

		natTable.refresh();
	}
}