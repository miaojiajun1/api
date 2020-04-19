package com.lemon.cases;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.utils.EnvironmentsUtils;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.SQLUtils;

public class RegisterCase extends BaseCase {
	/**
	 * 注册用例的测试方法：
	 * @param url		接口请求地址
	 * @param method	接口请求方法
	 * @param params	接口请求参数
	 * @param contentType 	接口类型
	 */
	@Test(dataProvider = "datas", description = "注册测试")
	public void test(API api,Case c) {
		//1、参数化替换
		String params = paramsReplace(c.getParams());
		c.setParams(params);
		String sql = paramsReplace(c.getSql());
		c.setSql(sql);
		
		//2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
		Object beforeSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
		//3、调用接口
		Map<String, String> headers = new HashMap<>();
		//3.1设置默认请求头
		setDefaultHeaders(headers);
		String body = HttpUtils.call(api.getUrl(), api.getMethod(), c.getParams(), api.getContentType(),headers);
		//4、断言响应结果
		String responseAssert = responseAssert(c.getExpect(), body);
		//5、添加接口响应回写内容
		addWriteBackData(1, c.getId(), Constants.ACTUAL_RESPONSE_CELLNUM, body);
		//6、数据库后置查询结果
		Object afterSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
		//7、数据库断言
		if(StringUtils.isNotBlank(c.getSql())) {
			boolean sqlAssertFlag = sqlAssert(beforeSQLResult, afterSQLResult);
			System.err.println("数据库断言：" + sqlAssertFlag);
			//SQL断言回写 三元表达式：结果为真 pass，结果为假 fail
			addWriteBackData(1, c.getId(), Constants.SQL_ASSERT_CELLNUM, sqlAssertFlag ? "Pass" : "Fail");
		}
		//8、添加断言回写内容
		addWriteBackData(1, c.getId(), Constants.RESPONSE_ASSERT_CELLNUM, responseAssert);
		//9、添加日志
		//10、报表断言
		Assert.assertEquals(responseAssert, "断言成功！");
	}
	
	
	
	/**
	 * 数据库断言
	 * @param beforeSQLResult	接口执行之前的数据结果
	 * @param afterSQLResult	接口执行之后的数据结果
	 * @return
	 */
	public boolean sqlAssert(Object beforeSQLResult,Object afterSQLResult) {
		Long beforeValue = (Long)beforeSQLResult;
		Long afterValue = (Long)afterSQLResult;
		if(beforeValue != null && afterValue != null && beforeValue == 0 && afterValue == 1) {
			//接口执行之前手机号统计为0，接口执行后手机号统计为1,断言成功
			return true;
		}
		return false;
	}
	
	
	@DataProvider
	public Object[][] datas(){
//		Object[][] datas= ExcelUtils.read();
//		return datas;
		Object[][] datas = ExcelUtils.getAPIAndCaseByApiId("1");
		return datas;
	}
	
}
