package com.lemon.cases;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONPath;
import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.utils.EnvironmentsUtils;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.SQLUtils;

public class RechargeCase extends BaseCase{
	/**
	 * 
	 */
	@Test(dataProvider = "datas")
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
		//3.1 设置默认请求头
		setDefaultHeaders(headers);
		//3.2 获取token
//		System.out.println(EnvironmentsUtils.env);	查询是否有token值
		getToken2Header(headers);
		String body = HttpUtils.call(api.getUrl(), api.getMethod(), c.getParams(), api.getContentType(),headers);
		//4、断言响应结果
		String responseAssert = responseAssert(c.getExpect(), body);
		//5、添加接口响应回写内容
		addWriteBackData(1, c.getId(), Constants.ACTUAL_RESPONSE_CELLNUM, body);
		//6、数据库后置查询结果
		Object afterSQLResult = SQLUtils.getSQLSingleResult(c.getSql());
		//7、数据库断言
		boolean sqlAssertFlag = true;
		if(StringUtils.isNotBlank(c.getSql())) {
			sqlAssertFlag = sqlAssert(beforeSQLResult, afterSQLResult,c);
			System.err.println("数据库断言：" + sqlAssertFlag);
			//SQL断言回写 三元表达式：结果为真 pass，结果为假 fail
			addWriteBackData(1, c.getId(), Constants.SQL_ASSERT_CELLNUM, sqlAssertFlag ? "Pass" : "Fail");
		}
		//8、添加断言回写内容
		addWriteBackData(1, c.getId(), Constants.RESPONSE_ASSERT_CELLNUM, responseAssert);
		//9、添加日志
		//10、报表断言
		Assert.assertEquals(responseAssert, "断言成功！");
		//数据库
//		Assert.assertEquals(sqlAssertFlag, true);

	}
	/**
	 * 数据库断言
	 * @param beforeSQLResult	接口执行之前的数据结果
	 * @param afterSQLResult	接口执行之后的数据结果
	 * @return
	 */
	public boolean sqlAssert(Object beforeSQLResult,Object afterSQLResult,Case c) {
		//1、从参数中获取amount的值
		if(beforeSQLResult ==null || afterSQLResult ==null) {
			return false;
		}
		String params = c.getParams();
		String amount = JSONPath.read(params, "$.amount").toString();
		//2、把amount转成BigDecimal
		BigDecimal amountValue = new BigDecimal(amount);
		//3、beforeSQLResult和afterSQLResult转成BigDecimal
		BigDecimal beforeValue = (BigDecimal)beforeSQLResult;
		BigDecimal afterValue = (BigDecimal)afterSQLResult;
		//4、afterValue - beforeValue = 实际充值金额
		BigDecimal subtractResult = afterValue.subtract(beforeValue);
//		System.out.println("前后相减为：" + subrractResult);
//		System.out.println("beforeValue:" + beforeValue);
//		System.out.println("afterValue:" + afterValue);
		//5、参数amount(期望值) 和 subtrartResult(实际值)进行比较，如果是0，说明相等
		if(subtractResult.compareTo(amountValue) == 0 ) {
			return true;
		}
		return false;
	}
	
	@DataProvider
	public Object[][] datas(){
//		Object[][] datas= ExcelUtils.read();
//		return datas;
		Object[][] datas = ExcelUtils.getAPIAndCaseByApiId("3");
		return datas;
	}
	
}
