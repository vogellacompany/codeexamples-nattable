package com.vogella.nattable.parts;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonDataProvider;

public class SimpleDataLayerPart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		List<Person> persons = personService.getPersons(10);
		PersonDataProvider personDataProvider = new PersonDataProvider(persons);
		DataLayer bodyDataLayer = new DataLayer(personDataProvider);

		new NatTable(parent, bodyDataLayer);
	}
}
