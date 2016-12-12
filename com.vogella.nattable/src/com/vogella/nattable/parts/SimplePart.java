 
package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.widgets.Composite;

public class SimplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		new NatTable(parent);
	}
}