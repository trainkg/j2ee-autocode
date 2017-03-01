package com.zsq.autocde.form.dom;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;

/**
 * 公共的DOM对象
 */
@Getter
@Setter
public abstract class GenericDom  implements Dom {
	/**
	 * 页面ID
	 */
	private String id;
	/**
	 * 用户可以指定自己模板,系统采用freemarker作为模板引擎 
	 */
	private String template;
	/**
	 * 当我们扩展累一个元素类型的时候，在配置中，我们如果读取不到对应的配置信息 那么我们就会读取配置中的clazz信息
	 */
	private String clazz;
	
	/**
	 * 其他配置
	 */
	private Map<String,String> props = new HashMap<String, String>();
	
	/**
	 * 获取元素对应的默认模板
	 * @return
	 */
	abstract String getDefaultTemplate();
	
	/**
	 * 获取元素生效的模板
	 * @return
	 */
	public String getTemplate(){
		if(StringUtils.isNotBlank(template)){
			return template;
		}else{
			return getDefaultTemplate();
		}
	}
}
