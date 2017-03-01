package com.zsq.autocde.ui;

import org.hibernate.tool.ant.ExporterTask;
import org.hibernate.tool.ant.HibernateToolTask;
import org.hibernate.tool.hbm2x.Exporter;

import com.zsq.autocde.ui.exporter.PickerExporter;

public class PickerExporterTask extends ExporterTask {

	public PickerExporterTask(HibernateToolTask parent) {
		super(parent);
	}

	@Override
	protected Exporter createExporter() {
		return new PickerExporter();
	}
	
	@Override
	public String getName() {
		return "com.zsq generate model picker info.";
	}
	
}
