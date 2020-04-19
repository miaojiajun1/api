package com.lemon.constants;

public class Constants {
	//常量：恒定不变
	//final 修饰类 类不能被继承，修饰方法 方法不能被重写，修饰变量 变成常量
	//常量：只能赋值一次,基本数据类型 值不能改变，引用数据类型 地址值不能改变，但是可以调用方法
	//常量的命名规则：所有英文大写，单词用下划线分割
	public static final String EXCEL_PATH = "src/test/resources/cases_v5.xlsx";
	
	//鉴权请求头
	public static final String MEDIA_TYPE = "lemonban.v2";
	
	//实际响应列号
	public static final int ACTUAL_RESPONSE_CELLNUM = 5;
	//响应断言列号
	public static final int RESPONSE_ASSERT_CELLNUM = 6;
	//数据库断言列号
	public static final int SQL_ASSERT_CELLNUM = 8;


	
	//数据库
	//如果为Oracle，则为 jdbc:oracle:thin:@localhost:1521:orcl,另外 导的包 坐标也要改掉
	public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
	public static final String JDBC_USERNAME = "future";
	public static final String JDBC_PASSWORD = "123456";
	
	
	//参数化
	public static final String PARAM_REGISTER_MOBILEPHONE = "${register_mobilephone}";
	public static final String PARAM_REGISTER_PASSWORD = "${register_password}";
	public static final String PARAM_LOGIN_MOBILEPHONE = "${login_mobilephone}";
	public static final String PARAM_LOGIN_PASSWORD = "${login_password}";
	public static final String PARAM_TOKEN = "${token}";
	public static final String PARAM_MEMBER_ID = "${member_id}";
	
	
}
