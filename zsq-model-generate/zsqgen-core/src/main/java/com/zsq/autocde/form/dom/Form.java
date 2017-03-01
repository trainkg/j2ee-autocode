package com.zsq.autocde.form.dom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import lombok.Getter;
import lombok.Setter;

import org.w3c.dom.Document;

import com.zsq.autocde.form.IFormInputType;

/**
 * 表单对象 
 */
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Form extends GenericDom implements IFormInputType<Form> {
	/**
	 * 表单元素列表 
	 */
	@XmlElements({
		@XmlElement(name="select",type=Select.class),
		@XmlElement(name="radio",type=Radio.class),
		@XmlElement(name="checkboxList",type=CheckboxList.class),
		@XmlElement(name="radioList",type=RadioList.class),
		@XmlElement(name="text",type=Text.class),
		@XmlElement(name="textarea",type=TextArea.class)
	})
	private java.util.List<GenericInput> inputs;
	
	public String generateHtml(Form config) {
		return null;
	}

	@Override
	String getDefaultTemplate() {
		return "com/zsq/autocde/form/dom/template/form.ftl";
	}
	
	public static void main(String[] args) {
		Form form = new Form();
		form.setInputs(new ArrayList<GenericInput>());
		form.setId("123");
		form.getInputs().add(new Select("test"));
		form.getInputs().add(new Radio());
		
		try {
			JAXBContext context = JAXBContext.newInstance(Form.class);  
			  
			Marshaller m = context.createMarshaller();  
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			context.generateSchema(new SchemaOutputResolver() {
				@Override
				public Result createOutput(String paramString1, String paramString2)
						throws IOException {
					return new StreamResult(new File("D:/11.xsd"));
				}
			});
			//m.marshal(form, System.out);
			
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=factory.newDocumentBuilder();
			Document xmldoc=db.parse(new File("D://text.xml"));
//			JAXBElement<Form> form1 = context.createUnmarshaller().unmarshal(xmldoc,Form.class);
//			System.out.println(form1.getValue().getInputs().get(0));
			form = (Form) context.createUnmarshaller().unmarshal(new File("D://text.xml"));
			System.out.println(form.getInputs().get(0));
			//System.out.println(form.getInputs().get(0).getClass());
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
