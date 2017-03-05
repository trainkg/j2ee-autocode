package org.hibernate.tool.hbm2x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hibernate.cfg.Configuration;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.mapping.Component;
import org.hibernate.tool.hbm2x.pojo.ComponentPOJOClass;
import org.hibernate.tool.hbm2x.pojo.POJOClass;


public class GenericExporter extends AbstractExporter {
	
	static abstract class ModelIterator {		
		abstract void process(GenericExporter ge);
	}
	
	/**
	 * 模型处理器允许列表, 这个的模型迭代器的范围需要修改为可以扩展的。
	 */
	static Map<String, ModelIterator> modelIterators = new HashMap<String, ModelIterator>();
	static {
		modelIterators.put( "configuration", new ModelIterator() {
			void process(GenericExporter ge) {
				TemplateProducer producer = 
						new TemplateProducer(
								ge.getTemplateHelper(),
								ge.getArtifactCollector());
				producer.produce(
						new HashMap<String, Object>(), 
						ge.getTemplateName(), 
						new File(ge.getOutputDirectory(),ge.filePattern), 
						ge.templateName, 
						"Configuration");				
			}			
		});
		modelIterators.put("entity", new ModelIterator() {		
			void process(GenericExporter ge) {
				Iterator<?> iterator = 
						ge.getCfg2JavaTool().getPOJOIterator(
								ge.getConfiguration().getClassMappings());
				Map<String, Object> additionalContext = new HashMap<String, Object>();
				while ( iterator.hasNext() ) {					
					POJOClass element = (POJOClass) iterator.next();
					ge.exportPersistentClass( additionalContext, element );					
				}
			}
		});
		modelIterators.put("component", new ModelIterator() {
			
			void process(GenericExporter ge) {
				Map<String, Component> components = new HashMap<String, Component>();
				
				Iterator<?> iterator = 
						ge.getCfg2JavaTool().getPOJOIterator(
								ge.getConfiguration().getClassMappings());
				Map<String, Object> additionalContext = new HashMap<String, Object>();
				while ( iterator.hasNext() ) {					
					POJOClass element = (POJOClass) iterator.next();
					ConfigurationNavigator.collectComponents(components, element);											
				}
						
				iterator = components.values().iterator();
				while ( iterator.hasNext() ) {					
					Component component = (Component) iterator.next();
					ComponentPOJOClass element = new ComponentPOJOClass(component,ge.getCfg2JavaTool());
					ge.exportComponent( additionalContext, element );					
				}
			}
		});
	}
	
	/**
	 * 模板名称
	 */
	private String templateName;
	
	/**
	 * 文件名称
	 */
	private String filePattern;
	
	/**
	 * 模型迭代器处理, 支持 ‘,’ 分割
	 */
	private String forEach;
	
	public GenericExporter(Configuration cfg, File outputdir) {
		super(cfg,outputdir);
	}

	public GenericExporter() {
	}
	
	public String getTemplateName() {
		return templateName;
	}
	
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
		
	
	public void setForEach(String foreach) {
		this.forEach = foreach;
	}
	
	
	protected void doStart() {
				
		if(filePattern==null) {
			throw new ExporterException("File pattern not set on " + this.getClass());
		}
		if(templateName==null) {
			throw new ExporterException("Template name not set on " + this.getClass());
		}
		
		List<ModelIterator> exporters = new ArrayList<ModelIterator>();
	
		if(StringHelper.isEmpty( forEach )) {
			if(filePattern.indexOf("{class-name}")>=0) {				
				exporters.add( modelIterators.get( "entity" ) );
				exporters.add( modelIterators.get( "component") );
			} else {
				exporters.add( modelIterators.get( "configuration" ));			
			}
		} else {
			StringTokenizer tokens = new StringTokenizer(forEach, ",");
		 
			while ( tokens.hasMoreTokens() ) {
				String nextToken = tokens.nextToken();
				ModelIterator modelIterator = modelIterators.get(nextToken);
				if(modelIterator==null) {
					throw new ExporterException("for-each does not support [" + nextToken + "]");
				}
				exporters.add(modelIterator);
			}
		}

		Iterator<ModelIterator> it = exporters.iterator();
		while(it.hasNext()) {
			ModelIterator mit = it.next();
			mit.process( this );
		}
	}

	protected void exportComponent(Map<String, Object> additionalContext, POJOClass element) {
		exportPOJO(additionalContext, element);		
	}

	protected void exportPersistentClass(Map<String, Object> additionalContext, POJOClass element) {
		exportPOJO(additionalContext, element);		
	}

	protected void exportPOJO(Map<String, Object> additionalContext, POJOClass element) {
		TemplateProducer producer = new TemplateProducer(getTemplateHelper(),getArtifactCollector());					
		additionalContext.put("pojo", element);
		additionalContext.put("clazz", element.getDecoratedObject());
		String filename = resolveFilename( element );
		log.info("gen file name {}",filename);
		if(filename.endsWith(".java") && filename.indexOf('$')>=0) {
			log.warn("Filename for " + getClassNameForFile( element ) + " contains a $. Innerclass generation is not supported.");
		}
		producer.produce(additionalContext, getTemplateName(), new File(getOutputDirectory(),filename), templateName, element.toString());
	}

	/**
	 * 生成文件名称, 简单的实现, 建议极限修改为Google groovy script表达式。
	 * @param element
	 * @return
	 */
	protected String resolveFilename(POJOClass element) {
		String filename = StringHelper.replace(filePattern, "{class-name}", getClassNameForFile( element )); 
		String packageLocation = StringHelper.replace(getPackageNameForFile( element ),".", "/");
		if(StringHelper.isEmpty(packageLocation)) {
			packageLocation = "."; // done to ensure default package classes doesn't end up in the root of the filesystem when outputdir=""
		}
		filename = StringHelper.replace(filename, "{package-name}", packageLocation);
		return filename;
	}

	protected String getPackageNameForFile(POJOClass element) {
		return element.getPackageName(); 
	}

	protected String getClassNameForFile(POJOClass element) {
		return element.getDeclarationName();
	}

	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;		
	}
	
	public String getFilePattern() {
		return filePattern;
	}
}