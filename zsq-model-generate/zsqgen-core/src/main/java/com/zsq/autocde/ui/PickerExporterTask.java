package com.zsq.autocde.ui;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.tool.ant.ExporterTask;
import org.hibernate.tool.ant.HibernateToolTask;
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.tool.hbm2x.GenericExporter;

import com.zsq.autocde.ui.exporter.PickerExporter;

/**
 * 对于企业级应用来说，选择器是一个非常重要的功能, 可以支持在任何场景中快速选取自己想要的信息。
 * <ol>
 * <li>查询条件表单构建</li>
 * <li>查询服务实现</li>
 * <li>查询结果设定</li>
 * <li>回调函数</li>
 * </ol>
 * <strong> 由于系统的查询条件的部分DOM生成和表单构造器有关，这个部分功能十分复杂，只能有简入难，依靠版本迭代进行替换，初期只支持最简单的表单结构
 * </strong>
 * <p>
 * 每个任务 都是有一个 {@link ExporterTask} + {@link GenericExporter} 组成 <br>
 * </p>
 * <p>
 * TASK: 负责配置文件的加载和解析<br>
 * GenericExporter:负责拿到配置文件的后续处理<br>
 * </p>
 * 
 * @author peculiar.1@163.com
 * @version $ID: PickerExporterTask.java, V1.0.0 2017年3月4日 下午7:30:08 $
 */
public class PickerExporterTask extends ExporterTask {

	@Getter
	@Setter
	private String fileSuffix;
	
	@Getter
	@Setter
	private File staticdir;
	
	@Getter
	@Setter
	private String cssTemplate;
	
	@Getter
	@Setter
	private String jsTemplate;
	
	@Getter
	@Setter
	private String pickerCfgDir; 

	public PickerExporterTask(HibernateToolTask parent) {
		super(parent);
	}

	/**
	 * 采用 {@link #PickerExporterTask(HibernateToolTask)}
	 */
	@Override
	protected Exporter createExporter() {
		PickerExporter exporter =  new PickerExporter();
		return exporter;
	}
	
	/**
	 * 先创建再配置
	 */
	@Override
	protected Exporter configureExporter(Exporter exporter) {
		PickerExporter picExporter = (PickerExporter) exporter;
		picExporter.setFileSuffix(getFileSuffix());
		picExporter.setStaticdir(getStaticdir());
		picExporter.setCssTemplate(getCssTemplate());
		picExporter.setJsTemplate(getJsTemplate());
		picExporter.setPickerCfgDir(getPickerCfgDir());
		super.configureExporter(exporter);
		return exporter;
	}

	@Override
	public String getName() {
		return "com.zsq generate model picker info.";
	}

}
