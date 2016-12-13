package com.vogella.nattable.data;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;

import com.vogella.model.person.Person;

public class PersonDataProvider implements IDataProvider {

	private List<Person> persons;

	public PersonDataProvider(List<Person> persons) {
		this.persons = persons;
	}

	@Override
	public Object getDataValue(int columnIndex, int rowIndex) {
		Person person = persons.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return person.getFirstName();
		case 1:
			return person.getLastName();
		case 2:
			return person.getGender();
		case 3:
			return person.isMarried();
		case 4:
			return person.getBirthday();
		}
		return person;
	}

	@Override
	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		// similar to getDataValue()
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		return persons.size();
	}
}
