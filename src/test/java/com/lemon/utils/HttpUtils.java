package com.lemon.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.lemon.cases.BaseCase;

import io.qameta.allure.Step;

public class HttpUtils {
	public static Logger log = Logger.getLogger(HttpUtils.class);
	
	/**
	 * 静态在工具类中的作用：方便调用，直接用类名点的方法调用
	 * 静态另一个作用：共享
	 * @param args
	 * @throws Exception
	 */
	
	/**
	 * 接口调用方法
	 * @param url			接口请求地址
	 * @param method		接口请求方法
	 * @param params		接口请求参数
	 * @param contentType	接口类型
	 * @return 
	 */
	@Step("发送HTTP请求")
	public static String call(String url,String method,String params,String contentType,Map<String, String>headers) {
		String body=null;
		try {
			if("post".equalsIgnoreCase(method)) {
				if("form".equalsIgnoreCase(contentType)) {
					params = json2KeyValue(params);
					body = HttpUtils.formPost(url, params,headers);
				}else if("json".equalsIgnoreCase(contentType)) {
					body = HttpUtils.post(url, params,headers);
				}
			}else if("get".equalsIgnoreCase(method)) {
				body = HttpUtils.get(url,headers);
			}else if("patch".equalsIgnoreCase(method)) {
				body = HttpUtils.patch(url, params,headers);
			}
		} catch (Exception e) {
			log.error(e);//报错时，这样打印出错误
			e.printStackTrace();
		}
		return body;
//		System.out.println(body);
	}
	
	/**
	 * JSON字符串转成 key=value 字符串
	 * @param jsonStr	json字符串
	 * @return
	 */
	private static String json2KeyValue(String jsonStr) {
		//json -->> key=value
		//json-->Map-->String
		Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
		Set<String> keySet = map.keySet();
		String result="";
		for (String key : keySet) {
			String value = map.get(key);
			result +=key + "=" + value + "&";
		}
		jsonStr=result.substring(0,result.length()-1);
		return jsonStr;
	}
	
	/**
	 * 发送一个get请求
	 * 如果要修改请求头，请修改get方法源码
	 * @param url	接口的请求地址+请求参数
	 * @throws Exception
	 */
	public static String get(String url,Map<String, String>headers) throws Exception {
		HttpGet get = new HttpGet(url);
//		get.setHeader("X-Lemonban-Media-Type", "lemonban.v1");		
		setHeaders(get, headers);
		HttpClient client = HttpClients.createDefault();
		//设置代理
//		HttpHost proxy = new HttpHost("127.0.0.1", 8888);//fiddler端口
//		HttpResponse response = client.execute(proxy, get);
		HttpResponse response = client.execute(get);
		return printResponseAndReturnBody(response);
	}
	
	/**
	 * 发一个post请求
	 * 如果要修改请求头，请修改post方法源码
	 * @param url			接口请求地址
	 * @param jsonParams	json格式的请求参数
	 * @throws Exception
	 */
	
	public static String post(String url,String jsonParams,Map<String, String>headers) throws Exception {
		//1、new request
		//2、add method 请求方式
		//3、url  填写url
		HttpPost post = new HttpPost(url);
		//4、body header，如果有参数和header，则需进行添加
//		post.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//		post.setHeader("Content-Type","application/json");
		setHeaders(post, headers);
		post.setEntity(new StringEntity(jsonParams,"utf-8"));
		//5、send 点击发送，创建HttpClient客户端
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(post);
		//6、response body 格式化响应体
		return printResponseAndReturnBody(response);

	}
	
	/**
	 * 发一个patch请求
	 * 如果要修改请求头，请修改patch方法源码
	 * @param url			接口请求地址
	 * @param jsonParams	json格式的请求参数
	 * @throws Exception
	 */
	
	public static String patch(String url,String jsonParams,Map<String, String>headers) throws Exception {
		HttpPatch patch = new HttpPatch(url);
//		patch.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//		patch.setHeader("Content-Type","application/json");
		setHeaders(patch, headers);
		patch.setEntity(new StringEntity(jsonParams,"utf-8"));
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(patch);
		return printResponseAndReturnBody(response);

	}
	
	/**
	 * 发一个post请求
	 * 如果要修改请求头，请修改post方法源码
	 * @param url			接口请求地址
	 * @param formParams	form格式的请求参数
	 * @throws Exception
	 */
	
	public static String formPost(String url,String formParams,Map<String, String>headers) throws Exception {
		HttpPost post = new HttpPost(url);
//		post.setHeader("Content-Type","application/x-www-form-urlencoded");
//		post.setEntity(new StringEntity(formParams,"utf-8"));
		setHeaders(post, headers);
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(post);
		return printResponseAndReturnBody(response);

	}
	
	/**
	 * 给请求添加对应的请求头
	 * @param request	请求对象(post、get、patch)
	 * @param headers	请求头map
	 */
	private static void setHeaders(HttpRequest request,Map<String, String>headers) {
		//1、获取所有请求头里的key
		Set<String> keySet = headers.keySet();
		//2、遍历所有键
		for (String key : keySet) {
			//3、把对应键和值设置到request的header中
			request.setHeader(key,headers.get(key));
		}
		
		
	}
	
	/**
	 * 打印响应内容，并且返回响应体
	 * @param response	接口响应对象
	 * @return			接口响应体
	 * @throws IOException
	 */
	
	private static String printResponseAndReturnBody(HttpResponse response) throws IOException {
		Header[] allHeaders = response.getAllHeaders();
		log.info("响应头："+ Arrays.toString(allHeaders));
		HttpEntity entity = response.getEntity();
		String body =EntityUtils.toString(entity);
		log.info("响应体"+body);
		int statusCode = response.getStatusLine().getStatusCode();
		log.info("状态码："+statusCode);
		return body;
	}

}
