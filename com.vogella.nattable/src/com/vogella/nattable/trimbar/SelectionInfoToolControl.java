package com.vogella.nattable.trimbar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SelectionInfoToolControl {

	private Label label;


	@PostConstruct
	public void createGui(Composite parent) {
		label = new Label(parent, SWT.NONE);
	}
	
	
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object selection) {
		if(label != null && !label.isDisposed()) {
			label.setText(String.valueOf(selection));
		}
	}
}