package com.zsq.autocde.form.dom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import lombok.Getter;
import lombok.Setter;

/**
 * 表单章节
 * 
 * @author peculiar.1@163.com
 * @version $ID: Section.java, V1.0.0 2017年3月19日 下午4:52:12 $
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Section extends GenericDom {

	/**
	 * 章节标题
	 */
	@XmlAttribute
	private String title;

	/**
	 * 表单元素列表
	 */
	@XmlElements({ @XmlElement(name = "select", type = Select.class),
			@XmlElement(name = "radio", type = Radio.class),
			@XmlElement(name = "checkboxList", type = CheckboxList.class),
			@XmlElement(name = "radioList", type = RadioList.class),
			@XmlElement(name = "text", type = Text.class),
			@XmlElement(name = "textarea", type = TextArea.class) })
	private java.util.List<GenericInput> inputs;

	@Override
	String getDefaultTemplate() {
		return "com/zsq/autocde/form/dom/template/section.ftl";
	}

}
