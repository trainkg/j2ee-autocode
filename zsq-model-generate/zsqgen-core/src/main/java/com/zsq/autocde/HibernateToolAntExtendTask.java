package com.zsq.autocde;

import org.hibernate.tool.ant.ExporterTask;
import org.hibernate.tool.ant.Hbm2HbmXmlExporterTask;
import org.hibernate.tool.ant.HibernateToolTask;

import com.zsq.autocde.ui.PickerExporterTask;

public class HibernateToolAntExtendTask extends HibernateToolTask {
	
	
	public ExporterTask createHbm2HbmXml() {
        ExporterTask generator= new Hbm2HbmXmlExporterTask(this);
        addGenerator( generator );
        return generator;
    }
	
	public PickerExporterTask createModel2Picker(){
		PickerExporterTask generator= new PickerExporterTask(this);
        addGenerator( generator );
        return generator;
	}
	
}
