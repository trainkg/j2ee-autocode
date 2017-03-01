package com.zsq.autocde.form.dom;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * radio列表
 */
@Getter
@Setter
public class RadioList extends GenericInput{
	private List<Radio> radios;

	@Override
	String getDefaultTemplate() {
		return "com/zsq/autocde/form/dom/template/radio-list.ftl";
	}
}
