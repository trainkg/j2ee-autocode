package com.zsq.autocde.form.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({Dog.class,Lion.class})
public class Zoo {

	@XmlAnyElement
	public List<Animal> animals;

	public Zoo() {
		animals = new ArrayList<Animal>();
		animals.add(new Dog());
		animals.add(new Lion());
	}
}
