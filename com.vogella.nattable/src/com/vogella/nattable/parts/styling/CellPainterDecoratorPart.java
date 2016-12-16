package com.vogella.nattable.parts.styling;

import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;
import com.vogella.nattable.data.PersonColumnPropertyAccessor;

public class CellPainterDecoratorPart {

	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {

		ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		IRowDataProvider<Person> bodyDataProvider = new ListDataProvider<Person>(personService.getPersons(10),
				new PersonColumnPropertyAccessor());

		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

		bodyDataLayer.setConfigLabelAccumulator(new ColumnLabelAccumulator(bodyDataProvider));

		NatTable natTable = new NatTable(parent, bodyDataLayer, false);
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		natTable.addConfiguration(new AbstractRegistryConfiguration() {

			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {

				iconCellPainterDecorator(resourceManager, configRegistry, "icons/eclipse.png", 0);
				iconCellPainterDecorator(resourceManager, configRegistry, "icons/germany.png", 1);
			}

			private void iconCellPainterDecorator(ResourceManager resourceManager, IConfigRegistry configRegistry,
					String path, int index) {
				Bundle bundle = FrameworkUtil.getBundle(getClass());
				URL find = FileLocator.find(bundle, new Path(path), null);
				ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(find);

				TextPainter textPainter = new TextPainter();
				ImagePainter imagePainter = new ImagePainter(resourceManager.createImage(imageDescriptor));
				CellPainterDecorator painterDecorator = new CellPainterDecorator(textPainter, CellEdgeEnum.LEFT,
						imagePainter);
				configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, painterDecorator,
						DisplayMode.NORMAL, ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + index);
			}
		});

		natTable.configure();
	}
}
