package com.zsq.autocde.form;

import com.zsq.autocde.form.dom.Dom;

/**
 * 页面元素类型
 * <P>
 * 宗旨在于根据配置生成相应的页面,和相应的引擎进行结合使用或者单独使用
 * 基于不同的配置信息,和响应式设计的页面框架结合使用
 * </P>
 */
public interface IFormInputType<T extends Dom>{
	
	/**
	 * 生成配置
	 * @return
	 */
	public String generateHtml(T config);
}
