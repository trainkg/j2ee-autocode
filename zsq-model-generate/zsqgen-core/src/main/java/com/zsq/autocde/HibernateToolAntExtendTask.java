package com.zsq.autocde;

import org.hibernate.tool.ant.HibernateToolTask;

import com.zsq.autocde.ui.PickerExporterTask;

/**
 * 自定义扩展hibernate tools ant 任务列表
 * 
 * 
 * @author peculiar.1@163.com
 * @version $ID: HibernateToolAntExtendTask.java, V1.0.0 2017年3月4日 下午7:02:59 $
 */
public class HibernateToolAntExtendTask extends HibernateToolTask {
	
	
	/**
	 * 扩展支持模型对应的picker生成设定
	 * @return
	 */
	public PickerExporterTask createModel2Picker(){
		PickerExporterTask generator= new PickerExporterTask(this);
        addGenerator( generator );
        return generator;
	}
	
}
