package com.zsq.autocde.form.dom;

import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 普通的配置信息 
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class GenericInput extends GenericDom {
	
	/**
	 * JS的验证规则
	 */
	@XmlAttribute
	private String validate;
	/**
	 * 页面label
	 */
	@XmlAttribute
	private String label;
	/**
	 * TODO 当我们的页面模板交给jsp的时候，就可以利用JSP级别的功能对于模板进行控制
	 */
	@XmlAttribute
	private String convertClass;
	
	/**
	 * javascript 转换方法
	 */
	@XmlAttribute
	private String jsConvert;
	
	/**
	 * javascript 格式化方法
	 */
	@XmlAttribute
	private String jsFormatter;
	
	/**
	 * 默认值
	 */
	@XmlAttribute
	private String defValue;
	
	
	@XmlAttribute
	private String name;
	
	/**
	 * 值
	 */
	@XmlAttribute
	private String value;

	public GenericInput(String label) {
		super();
		this.label = label;
	}
	
}
