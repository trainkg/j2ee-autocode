package com.zsq.autocde.form.dom;

/**
 * html textarea
 */
public class TextArea extends GenericInput {

	@Override
	String getDefaultTemplate() {
		return "/com/zsq/autocde/form/dom/template/textarea.ftl";
	}
}
