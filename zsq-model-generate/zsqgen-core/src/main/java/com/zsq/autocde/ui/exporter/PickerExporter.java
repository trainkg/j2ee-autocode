package com.zsq.autocde.ui.exporter;

import java.util.Iterator;

import org.hibernate.tool.hbm2x.GenericExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

/**
 * 模型选择器生成方案
 * <P>
 * 	   系统生成方案一切从模型开始出发,每个模型都可以不同exporter对应的导出配置.
 * </P>
 * @author Administrator
 */
public class PickerExporter extends GenericExporter {

	public PickerExporter() {
		init();
	}

	void init() {
		// initexporter
		setTemplateName("com/zsq/autocde/ui/exporter/picker.ftl");
		setFilePattern("{package-name}/{class-name}.html");
	}

	/**
	 * 覆盖 GenericExporter 的生成业务
	 */
	@Override
	protected void doStart() {
		loadPickerSettings();
		/*Map<String, Object> additionalContext = new HashMap<String, Object>();
		TemplateProducer producer = new TemplateProducer(getTemplateHelper(),getArtifactCollector());*/
		// additionalContext.put("pojo", element);
		// additionalContext.put("clazz", element.getDecoratedObject());
		// String filename = resolveFilename( element );

		// producer.produce(additionalContext, getTemplateName(), new
		// File(getOutputDirectory(),filename), templateName,
		// element.toString());

		Iterator<?> iterator = getCfg2JavaTool().getPOJOIterator(getConfiguration().getClassMappings());
		while ( iterator.hasNext() ) {					
			POJOClass element = (POJOClass) iterator.next();
		}
	}

	/**
	 * 加载picker配置
	 */
	private void loadPickerSettings() {
		
	}

}
