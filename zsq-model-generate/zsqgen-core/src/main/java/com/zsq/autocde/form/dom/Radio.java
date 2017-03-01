package com.zsq.autocde.form.dom;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * html radio 
 * @author Administrator
 */
@XmlRootElement
public class Radio extends GenericInput{

	@Override
	String getDefaultTemplate() {
		return "com/zsq/autocde/form/dom/template/radio.ftl";
	}
}
