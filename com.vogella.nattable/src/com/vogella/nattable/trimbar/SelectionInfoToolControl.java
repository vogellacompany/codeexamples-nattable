package com.vogella.nattable.trimbar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SelectionInfoToolControl {

	private Label label;


	@PostConstruct
	public void createGui(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(container);
		label = new Label(container, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 20).applyTo(label);
	}
	
	
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object selection) {
		if(label != null && !label.isDisposed()) {
			label.setText(String.valueOf(selection));
		}
	}
}