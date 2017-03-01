package com.zsq.autocde.form.dom;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.zsq.autocde.form.IFormInputType;

/**
 * checkbox group 
 */
@Getter
@Setter
public class CheckboxList extends GenericInput implements IFormInputType<CheckboxList>{
	
	private List<Checkbox> checkboxs;

	public String generateHtml(CheckboxList config) {
		return null;
	}

	@Override
	String getDefaultTemplate() {
		return "com/zsq/autocde/form/dom/template/checkbox-list.ftl";
	}
}
