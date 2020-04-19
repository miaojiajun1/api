package com.lemon.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import com.lemon.pojo.Member;

public class SQLUtils {

	public static void main(String[] args) throws Exception {
		//DBUtils
		QueryRunner qr = new QueryRunner();
		//数据库的连接
		Connection conn = JDBCUtils.getConnection();
		
//		Object[] objects = qr.query(conn, "select * from member where id = 10;", new ArrayHandler());
//		for (Object object : objects) {
//			System.out.println(object);
//		}
		Object object = qr.query(conn, "select count(*) from member where id = 10;",new ScalarHandler<Object>());
		System.out.println(object);
		JDBCUtils.close(conn);
	}
	
	/**
	 * 根据SQL语句执行查询单个结果集
	 * @param sql	SQL语句
	 * @return		
	 */
	public static Object getSQLSingleResult(String sql) {
		//1、如果SQL语句为空，则不执行SQL查询
		if(StringUtils.isBlank(sql)) {
			return null;
		}
		//2、DBUtils操作SQL语句核心类
		QueryRunner qr = new QueryRunner();
		//3、获取数据库连接
		Connection conn = JDBCUtils.getConnection();
		//4、定义返回值
		Object result = null;
		try {
			//5、执行SQL语句
			result = qr.query( conn,sql,new ScalarHandler<Object>());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn);
		}
		return result;
	}
	
	/**
	 * 获取一个随机的Member对象
	 * @param sql	SQL语句
	 * @return		
	 */
	public static Member getOneRandomMember() {
		String sql = "select * from member ORDER BY RAND() LIMIT 1;";

		
		//2、DBUtils操作SQL语句核心类
		QueryRunner qr = new QueryRunner();
		//3、获取数据库连接
		Connection conn = JDBCUtils.getConnection();
		//4、定义返回值
		Member result = null;
		try {
			//5、执行SQL语句
			result = qr.query( conn,sql,new BeanHandler<Member>(Member.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn);
		}
		return result;
	}
	

}
