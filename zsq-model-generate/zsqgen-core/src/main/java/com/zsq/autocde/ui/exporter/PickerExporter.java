package com.zsq.autocde.ui.exporter;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.tool.hbm2x.GenericExporter;
import org.hibernate.tool.hbm2x.TemplateProducer;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

/**
 * 模型选择器生成方案
 * 
 * <P>
 * 	   系统生成方案一切从模型开始出发,每个模型都可以不同exporter对应的导出配置.
 * </P>
 * <p>
 * picker 选择器的使用场景大多数在的是管理型系统中， 管理型系统中伴随着一个巨大的一个影响就是数据问题
 * 尤其一些数据不是太方便放入在前台处理，不好暴露给前端用户
 * 在以往的客户中，很多是内部系统，一些数据的安全性得不到重视，但是在未来的云平台趋势下面，数据安全性会被放大出来
 * 可能一个数据安全性问题会让广大群众对你失去使用信息，所以我们在使用最新的前端技术的时候，尽量考虑这些问题，不要让权限处理交给前端来处理。
 * </p> 
 * <br>
 * <b>我的策略是不能将这部分复杂的和系统紧密联系的内容引进到模板阶段，可以结合服务后端引擎来解决权限部分问题，尤其一些权限控制框架，标签是不可能引身到前端的。</b>
 * 
 * @author Administrator
 */
public class PickerExporter extends GenericExporter {
	
	/**
	 * 读取配置文件
	 */
	protected void setupContext() {
		String fileSuffix = getFileSuffix();
		if(StringUtils.isBlank(fileSuffix)){
			setFileSuffix("html");
		}
		super.setupContext();
	}
	
	/**
	 * 覆盖 GenericExporter 的生成业务
	 */
	@Override
	protected void doStart() {
		loadPickerSettings();
		Iterator<?> iterator = getCfg2JavaTool().getPOJOIterator(getConfiguration().getClassMappings());
		while ( iterator.hasNext() ) {					
			POJOClass element = (POJOClass) iterator.next();
			Map<String, Object> additionalContext = new HashMap<String, Object>();
			TemplateProducer producer = new TemplateProducer(getTemplateHelper(), getArtifactCollector());
			additionalContext.put("pojo", element);
			additionalContext.put("clazz", element.getDecoratedObject());
			String filename = resolveFilename( element);
			log.info("[PICKER] file name {}",filename);
			producer.produce(additionalContext, getTemplateName(), new File(getOutputDirectory(),filename), element.toString());			
		}
	}

	/**
	 * 加载picker配置
	 */
	private void loadPickerSettings() {
		setTemplateName("com/zsq/autocde/ui/exporter/picker.ftl");
		log.info("load fileSuffix info is {}",getFileSuffix());
		setFilePattern("{package-name}/{class-name}."+getFileSuffix());
	}


}
