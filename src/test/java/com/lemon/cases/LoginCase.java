package com.lemon.cases;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;

public class LoginCase extends BaseCase {

	/**
	 * 注册用例的测试方法：
	 * @param url		接口请求地址
	 * @param method	接口请求方法
	 * @param params	接口请求参数
	 * @param contentType 	接口类型
	 */
	@Test(dataProvider = "datas")
	public void test(API api,Case c) {
		//1、参数化替换
		String params = paramsReplace(c.getParams());
		c.setParams(params);
		String sql = paramsReplace(c.getSql());
		c.setSql(sql);
		//2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
		//3、调用接口
		Map<String, String> headers = new HashMap<>();
		//3.1 设置默认请求头
		setDefaultHeaders(headers);
		String body = HttpUtils.call(api.getUrl(), api.getMethod(), c.getParams(), api.getContentType(),headers);
		//3.2 从body里面获取token，token存储到环境变量中
		setVariableInEnv(body,"$.data.token_info.token",Constants.PARAM_TOKEN);
//		System.out.println("body:==="+ body);
		//3.3 member_id存储到环境变量中
		setVariableInEnv(body,"$.data.id",Constants.PARAM_MEMBER_ID);
		//4、断言响应结果
		String responseAssert = responseAssert(c.getExpect(), body);
		 
		//5、添加接口响应回写内容
		addWriteBackData(1, c.getId(), Constants.ACTUAL_RESPONSE_CELLNUM, body);
		//6、数据库后置查询结果
		//7、数据库断言  登录没有SQL断言
		//8、添加断言回写内容
		addWriteBackData(1, c.getId(), Constants.RESPONSE_ASSERT_CELLNUM, responseAssert);
		//9、添加日志
		//10、报表断言
		Assert.assertEquals(responseAssert, "断言成功！");

	}





	@DataProvider
	public Object[][] datas(){
//		Object[][] datas= ExcelUtils.read();
//		return datas;
		Object[][] datas = ExcelUtils.getAPIAndCaseByApiId("2");
		return datas;
	}
	

	
}
