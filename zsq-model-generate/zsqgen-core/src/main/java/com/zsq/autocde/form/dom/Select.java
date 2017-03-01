package com.zsq.autocde.form.dom;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * select 配置
 * @author Administrator
 */
@Getter
@Setter
@NoArgsConstructor
public class Select extends GenericInput{
	private List<Options> options;
	
	@Override
	String getDefaultTemplate() {
		return "com/zsq/autocde/form/dom/template/select.ftl";
	}

	public Select(String label) {
		super(label);
		options = new ArrayList<Options>();
		options.add(new Options("1","2"));
		//getProps().put("test", "123");
		//getProps().put("test1", "2123");
	}
}
