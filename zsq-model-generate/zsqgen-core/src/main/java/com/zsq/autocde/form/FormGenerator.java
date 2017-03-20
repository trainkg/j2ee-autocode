package com.zsq.autocde.form;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;

import lombok.Getter;
import lombok.Setter;

import org.apache.tools.ant.BuildException;

import com.zsq.autocde.form.dom.Form;

/**
 * 表单生成器
 * 
 * @author peculiar.1@163.com
 * @version $ID: FormGenerator.java, V1.0.0 2017年3月20日 下午8:16:54 $
 */
public class FormGenerator {

	/**
	 * DOM form config info.
	 */
	@Getter
	@Setter
	private Form configForm;

	public Form loadFormConfig(InputStream is) {
		try {
			JAXBContext context = JAXBContext.newInstance(Form.class);
			Form form = (Form) context.createUnmarshaller().unmarshal(is);
			return form;
		} catch (Exception e) {
			throw new BuildException(e);
		} 
	}

}
