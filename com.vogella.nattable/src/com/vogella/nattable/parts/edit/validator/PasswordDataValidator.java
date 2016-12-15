package com.vogella.nattable.parts.edit.validator;

import org.eclipse.nebula.widgets.nattable.data.validate.DataValidator;

public class PasswordDataValidator extends DataValidator {

	@Override
	public boolean validate(int columnIndex, int rowIndex, Object newValue) {
		return String.valueOf(newValue).length() > 5;
	}

}
