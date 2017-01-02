package com.vogella.nattable.parts.glazedlists;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonDataProvider;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;

public class BeanObservableEventListPart {

	private int personId = 1000;

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		List<Person> persons = personService.getPersons(10);
		
		// create an EventList, which can track changes to the list of persons
		EventList<Person> personEventList = GlazedLists.eventList(persons);
		
		// create an ObservableElementList, which can track changes to the person objects with a bean connector
		ObservableElementList<Person> observableElementList = new ObservableElementList<>(personEventList,
				GlazedLists.beanConnector(Person.class));
		PersonDataProvider personDataProvider = new PersonDataProvider(observableElementList);
		DataLayer bodyDataLayer = new DataLayer(personDataProvider);

		// add a GlazedListsEventLayer event layer that is responsible for
		// updating the grid on list changes
		GlazedListsEventLayer<Person> glazedListsEventLayer = new GlazedListsEventLayer<>(bodyDataLayer,
				observableElementList);

		SelectionLayer selectionLayer = new SelectionLayer(glazedListsEventLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		Button addPersonBtn = new Button(parent, SWT.PUSH);
		addPersonBtn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		addPersonBtn.setText("Add new Person");
		addPersonBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Person newPerson = personService.createPerson(personId++);
				observableElementList.add(newPerson);
			}
		});

		Button removePersonBtn = new Button(parent, SWT.PUSH);
		removePersonBtn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		removePersonBtn.setText("Remove first Person");
		removePersonBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!observableElementList.isEmpty()) {
					observableElementList.remove(0);
				}
			}
		});

		Button modifyPersonBtn = new Button(parent, SWT.PUSH);
		modifyPersonBtn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		modifyPersonBtn.setText("Modify first Person");
		modifyPersonBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!observableElementList.isEmpty()) {
					Person person = observableElementList.get(0);
					person.setFirstName("Simon");
				}
			}
		});

		NatTable natTable = new NatTable(parent, viewportLayer);
		GridDataFactory.fillDefaults().grab(true, true).span(3, 1).applyTo(natTable);

		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(parent);
	}
}
