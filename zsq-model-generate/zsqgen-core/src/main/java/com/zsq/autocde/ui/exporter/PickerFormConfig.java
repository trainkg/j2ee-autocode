package com.zsq.autocde.ui.exporter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.tool.hbm2x.pojo.POJOClass;

import com.zsq.autocde.form.FormGenerator;

/**
 * 单个picker的数据模型
 * 
 * @author peculiar.1@163.com
 * @version $ID: PickerFormConfig.java, V1.0.0 2017年3月24日 下午8:42:35 $
 */
@Getter
@Setter
@AllArgsConstructor
public class PickerFormConfig {
	
	private FormGenerator form;
	private POJOClass pojo;
	private Object clazz;
	
	/**
	 * 是否picker配置了指定的字段
	 * @param feildname
	 * @return
	 */
	public boolean containsPojoFeild(String feildname){
		return false;
	}
	
}
