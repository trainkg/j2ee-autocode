package com.zsq.autocde.form.dom;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import lombok.Getter;
import lombok.Setter;

import com.zsq.autocde.form.IFormInputType;

/**
 * 表单对象 
 */
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Form extends GenericDom implements IFormInputType<Form> {
	
	private List<Section> sections;
	
	public String generateHtml(Form config) {
		return null;
	}
	
	@Override
	String getDefaultTemplate() {
		return "com/zsq/autocde/form/dom/template/form.ftl";
	}
	
	
	
	public static void main(String[] args) {
		Form form = new Form();
		form.setId("123");
		
		try {
			JAXBContext context = JAXBContext.newInstance(Form.class);  
			  
			Marshaller m = context.createMarshaller();  
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			context.generateSchema(new SchemaOutputResolver() {
				@Override
				public Result createOutput(String paramString1, String paramString2)
						throws IOException {
					return new StreamResult(new File("D:/zsq-form.xsd"));
				}
			});
			//m.marshal(form, System.out);
			
			/*DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=factory.newDocumentBuilder();
			Document xmldoc=db.parse(new File("D://text.xml"));*/
//			JAXBElement<Form> form1 = context.createUnmarshaller().unmarshal(xmldoc,Form.class);
//			System.out.println(form1.getValue().getInputs().get(0));
			//form = (Form) context.createUnmarshaller().unmarshal(new File("D://text.xml"));
			/*System.out.println(form.getInputs().get(0));*/
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
