package com.zsq.autocde.form.test;

import javax.xml.bind.JAXBContext;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		JAXBContext context = JAXBContext.newInstance(Zoo.class);
		Zoo zoo = new Zoo();
		context.createMarshaller().marshal(zoo, System.out);
	}
}
