package com.zsq.autocde.form.dom;

import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.Setter;

/**
 * 页面text的config配置 
 * @author Administrator
 */
@Getter
@Setter
public class Text extends GenericInput {
	/**
	 * text 输入框最大长度
	 */
	@XmlAttribute
	private String maxLength;

	@Override
	String getDefaultTemplate() {
		return "/com/zsq/autocde/form/dom/template/text.ftl";
	}
}
