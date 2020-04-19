package com.lemon.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class API {
	//都可以用字符串来操作，除非定义的参数要做加减乘除的运算
	//必须私有属性、空参构造、get/set方法
	//接口编号
	@Excel(name="接口编号")	//excel列和实体类的成员变量映射关系
//	@NotNull
	private String id;
	//接口名称
	@Excel(name="接口名称")
	private	String name;
	//接口提交方式
	@Excel(name="接口提交方式")
	private String method;
	//接口地址
	@Excel(name="接口地址")
//	@URL(protocol = "http",host = "api.lemonban.com")
	private String url;
	//参数类型
	@Excel(name="参数类型")
	private String contentType;
	
	/**
	 * @param id
	 * @param name
	 * @param method
	 * @param url
	 * @param contentType
	 */
	public API(String id, String name, String method, String url, String contentType) {
		super();
		this.id = id;
		this.name = name;
		this.method = method;
		this.url = url;
		this.contentType = contentType;
	}

	
	/**
	 * 
	 */
	public API() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	@Override
	public String toString() {
		return "API [id=" + id + ", name=" + name + ", method=" + method + ", url=" + url + ", contentType="
				+ contentType + "]";
	}

	
	

}
