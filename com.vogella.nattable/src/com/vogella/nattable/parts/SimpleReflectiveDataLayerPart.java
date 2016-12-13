package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class SimpleReflectiveDataLayerPart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		String[] propertyNames = { "firstName", "lastName", "gender", "married", "birthday" };

		IColumnPropertyAccessor<Person> columnPropertyAccessor = new ReflectiveColumnPropertyAccessor<Person>(
				propertyNames);
		IRowDataProvider<Person> bodyDataProvider = new ListDataProvider<Person>(personService.getPersons(10),
				columnPropertyAccessor);

		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

		new NatTable(parent, bodyDataLayer);
	}
}
