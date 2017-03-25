package com.zsq.autocde.ui.exporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.hibernate.tool.hbm2x.GenericExporter;
import org.hibernate.tool.hbm2x.TemplateProducer;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

import com.zsq.autocde.form.FormGenerator;

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
 * <p>
 * <b>我的策略是不能将这部分复杂的和系统紧密联系的内容引进到模板阶段，可以结合服务后端引擎来解决权限部分问题，尤其一些权限控制框架，标签是不可能引身到前端的。</b>
 * </p>
 * 在picker的常规组成中包含以下内容
 * <ul>
 * <li>模板资源(html/JPS/freemarker 或者其他资源)</li>
 * <li>css资源</li>
 * <li>JavaScript资源</li>
 * </ul>
 * <strong>picker使用的服务资源我们采取在其他的exporter提供</strong>
 * @author Administrator
 */
public class PickerExporter extends GenericExporter {
	
	/**
	 * 在设计中PICKER信息有一些静态资源以及一些模板(可能是动态的组合完成)
	 * 静态文件大致包含(css/javascript) <br>
	 * 总体文件分布
	 * <pre>
	 * {pickName}|
	 * 	 -- cssFile
	 * 	 -- javascriptFile
	 * </pre>
	 */
	@Setter
	private File staticdir;
	
	
	/**
	 * 在常规情况下，css可能有整体框架管理,并不是必须的, 只有当css模板设定时候，才会生成相应的css文件。
	 */
	@Getter
	@Setter
	private String cssTemplate;
	
	@Getter
	@Setter
	private String jsTemplate;
	
	@Getter
	@Setter
	private String encoding;
	
	/**
	 * picker 相关配置文件存放路径
	 */
	@Getter
	@Setter
	private String pickerCfgDir; 
	
	
	@Override
	public void start() {
		loadPickerSettings();
		super.start();
	}
	
	/**
	 * 读取配置文件
	 */
	protected void setupContext() {
		String fileSuffix = getFileSuffix();
		if(StringUtils.isBlank(fileSuffix)){
			setFileSuffix("html"); //default
		}
		getTemplateHelper().putInContext("encoding", getEncoding());
		super.setupContext();
	}
	
	/**
	 * 覆盖 GenericExporter 的生成业务
	 */
	@Override
	protected void doStart() {
		log.info("[PICK] picker config dir is {}",getPickerCfgDir());
		Iterator<?> iterator = getCfg2JavaTool().getPOJOIterator(getConfiguration().getClassMappings());
		while ( iterator.hasNext() ) {					
			POJOClass element = (POJOClass) iterator.next();
			Map<String, Object> additionalContext = new HashMap<String, Object>();
			TemplateProducer producer = new TemplateProducer(getTemplateHelper(), getArtifactCollector());
			additionalContext.put("pojo", element);
			additionalContext.put("pickerCfgDir", getPickerCfgDir());
			additionalContext.put("clazz", element.getDecoratedObject());
			String filename = resolveFilename( element);
			log.info("[PICK] file name {}",filename);
			
			FormGenerator formGenerator = new FormGenerator();
			InputStream isConfig = resolveConfigInputStream(element);
			if (isConfig == null) {
				log.info("[PICK] not fount {} picker config file, it will skip.",element.getDeclarationName());
				continue;
			}
			formGenerator.loadFormConfig(isConfig);
			additionalContext.put("form", formGenerator);
			try {
				isConfig.close();
			} catch (IOException e) {
			}
			additionalContext.put("pickerModel", new PickerFormConfig(formGenerator,element,element.getDecoratedObject()));
			//生成对应的模板文件
			producer.produce(additionalContext, getTemplateName(), new File(getOutputDirectory(),filename), element.toString(), true);
			//生成对应静态资源
			//css
			if(StringUtils.isNotBlank(getCssTemplate())){
				producer.produce(additionalContext, getCssTemplate(), new File(getStaticdir(),resolveCssFileName(element)), element.toString());	
			}
			//javascript
			producer.produce(additionalContext, getJsTemplate(), new File(getStaticdir(),resolveJsFileName(element)), element.toString());
		}
	}
	
	/**
	 * 在picker 配置目录中找去相应的picker配置文件信息
	 * @param element
	 * @return
	 */
	private InputStream resolveConfigInputStream(POJOClass element) {
		if(getPickerCfgDir() != null){
			String modalName =  element.getDeclarationName();
			File configFile = new File(getPickerCfgDir(), modalName+".xml");
			if(configFile.exists() && configFile.isFile()){
				try {
					return new FileInputStream(configFile);
				} catch (FileNotFoundException e) {
					return null;
				}
			}
		}
		return null;
	}

	private String resolveCssFileName(POJOClass element) {
		String className = getClassNameForFile(element);
		return className.toLowerCase()+".css";
	}

	private String resolveJsFileName(POJOClass element) {
		String className = getClassNameForFile(element);
		return className.toLowerCase()+".js";
	}

	/**
	 * 加载picker配置
	 */
	private void loadPickerSettings() {
		if(StringUtils.isBlank(getJsTemplate())){
			setJsTemplate("com/zsq/autocde/form/dom/template/pickerJs.ftl");
		}
		if(StringUtils.isBlank(getTemplateName())){
			setTemplateName("com/zsq/autocde/form/dom/template/picker.ftl");	
		}
		if(StringUtils.isBlank(getEncoding())){
			setEncoding("UTF-8");
		}
		log.info("load fileSuffix info is {}",getFileSuffix());
		setFilePattern("{class-name}."+getFileSuffix());
	}

	public File getStaticdir() {
		if(staticdir != null){
			return staticdir;	
		}else{
			return getOutputDirectory();
		}
	}

}
