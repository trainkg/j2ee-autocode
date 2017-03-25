package org.hibernate.tool.hbm2x;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * 
 * 
 * @author peculiar.1@163.com
 * @version $ID: TemplateProducer.java, V1.0.0 2017年3月17日 下午9:07:58 $
 */
public class TemplateProducer {

	private static final Logger log = LoggerFactory.getLogger(TemplateProducer.class);
	private final TemplateHelper th;
	private ArtifactCollector ac;
	
	public TemplateProducer(TemplateHelper th, ArtifactCollector ac) {
		this.th = th;
		this.ac = ac;
	}
	
	/**
	 * 
	 * @param additionalContext
	 * @param templateName
	 * @param destination
	 * @param identifier  只是一个标志信息
	 * @param fileType
	 * @param rootContext
	 */
	public void produce(Map<String,Object> additionalContext, String templateName, File destination, String identifier, String fileType, String rootContext) {
		produce(additionalContext, templateName, destination, identifier, fileType, rootContext, false);
	}
	
	public void produce(Map<String,Object> additionalContext, String templateName, File destination, String identifier, String fileType, String rootContext, boolean prettyxml) {
			
			String tempResult = produceToString( additionalContext, templateName, rootContext );
			if(tempResult.trim().length()==0) {
				log.warn("Generated output is empty. Skipped creation for file " + destination);
				return;
			}
			//FileWriter fileWriter = null;
			Writer fileWriter = null;
			
			try {
				th.ensureExistence( destination );    
				ac.addFile(destination, fileType);
				log.debug("Writing " + identifier + " to " + destination.getAbsolutePath() );
				fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination), "UTF-8"));
	            fileWriter.write(tempResult);			
			} 
			catch (Exception e) {
			    throw new ExporterException("Error while writing result to file", e);	
			} finally {
				if(fileWriter!=null) {
					try {
						fileWriter.flush();
						fileWriter.close();
					}
					catch (IOException e) {
						log.warn("Exception while flushing/closing " + destination,e);
					}				
				}
			}
			
			if(prettyxml){
				try {
					XMLPrettyPrinter.prettyPrintFile(XMLPrettyPrinter.getDefaultTidy(), destination, destination, true);
				} catch (Exception e) {
					log.warn("format {} failed.", templateName);
				}
			}
	}


	/**
	 * 使用freemarker生成最终的页面数据
	 * @param additionalContext
	 * @param templateName
	 * @param rootContext
	 * @return
	 */
	private String produceToString(Map<String,Object> additionalContext, String templateName, String rootContext) {
		Map<String,Object> contextForFirstPass = additionalContext;
		putInContext( th, contextForFirstPass );		
		StringWriter tempWriter = new StringWriter();
		BufferedWriter bw = new BufferedWriter(tempWriter);
		// First run - writes to in-memory string
		th.processTemplate(templateName, bw, rootContext);
		removeFromContext( th, contextForFirstPass );
		try {
			bw.flush();
		}
		catch (IOException e) {
			throw new RuntimeException("Error while flushing to string",e);
		}
		return tempWriter.toString();
	}

	private void removeFromContext(TemplateHelper templateHelper, Map<String,Object> context) {
		Iterator<Entry<String,Object>> iterator = context.entrySet().iterator();
		while ( iterator.hasNext() ) {
			Entry<String,Object> element = iterator.next();
			templateHelper.removeFromContext((String) element.getKey(), element.getValue());
		}
	}

	private void putInContext(TemplateHelper templateHelper, Map<String,Object> context) {
		Iterator<Entry<String,Object>> iterator = context.entrySet().iterator();
		while ( iterator.hasNext() ) {
			Entry<String,Object> element = iterator.next();
			templateHelper.putInContext((String) element.getKey(), element.getValue());
		}
	}
	
	/**
	 * @param additionalContext 额外的参数控制
	 * @param templateName 模板名称
	 * @param outputFile   文件输出路径
	 * @param identifier
	 */
	public void produce(Map<String,Object> additionalContext, String templateName, File outputFile, String identifier) {
		String fileType = outputFile.getName();
		fileType = fileType.substring(fileType.indexOf('.')+1);
		produce(additionalContext, templateName, outputFile, identifier, fileType, null);
	}
	
	public void produce(Map<String,Object> additionalContext, String templateName, File outputFile, String identifier, String rootContext) {
		String fileType = outputFile.getName();
		fileType = fileType.substring(fileType.indexOf('.')+1);
		produce(additionalContext, templateName, outputFile, identifier, fileType, rootContext);
	}	
	
	public void produce(Map<String,Object> additionalContext, String templateName, File outputFile, String identifier, boolean prettyxml) {
		String fileType = outputFile.getName();
		fileType = fileType.substring(fileType.indexOf('.')+1);
		produce(additionalContext, templateName, outputFile, identifier, fileType, null, prettyxml);
	}	
}
