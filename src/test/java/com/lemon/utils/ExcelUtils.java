package com.lemon.utils;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.pojo.WriteBackData;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

public class ExcelUtils {
	// 所有的API集合
	public static List<API> apiList;
	// 所有的Case集合
	public static List<Case> caseList;

	// Excel回写集合
	public static List<WriteBackData> wbdList = new ArrayList<>();

	public static void main(String[] args) throws Exception {

	}

	/**
	 * excel批量回写
	 */
	public static void batchWrite() {
		// poi回写代码
		// 1、加载Excel
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(Constants.EXCEL_PATH);
			// 2、创建workbook
			Workbook workbook = WorkbookFactory.create(fis);
			// 遍历wdbList集合
			for (WriteBackData wbd : wbdList) {
				// 3、获取对应的sheet
				Sheet sheet = workbook.getSheetAt(wbd.getSheetIndex());
				// 4、获取对应Row
				Row row = sheet.getRow(wbd.getRowNum());
				// 5、获取对应cell
				Cell cell = row.getCell(wbd.getCellNum(),MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellType(CellType.STRING);
				// 6、setCellValue 设置回写内容
				cell.setCellValue(wbd.getContent());
			}
			//7、回写Excel文件(文件会先被清空)
			fos = new FileOutputStream(Constants.EXCEL_PATH);
			workbook.write(fos);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 8、关流
			close(fis);
			close(fos);
		}
	}
	
	/**
	 * 关流方法
	 * @param stream	任意流对象
	 */
	private static void close(Closeable stream) {
		try {
			if(stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据apiId获取对应的API和List<Case>对象
	 * 
	 * @param apiId 获取API和Case的apiId
	 * @return Object[API][Case]
	 */
	public static Object[][] getAPIAndCaseByApiId(String apiId) {
//		//所有的API集合
//		List<API> apiList = read(0, 1, API.class);
//		//所有的Case集合
//		List<Case> caseList = read(1, 1, Case.class);
		// 需要的一个API
		API wantAPI = null;
		// 需要的多个case集合
		List<Case> wantList = new ArrayList<>();
		// 遍历集合找到符合的API
		for (API api : apiList) {
			// 找到了符合要求的API对象(apiId相等)
			if (apiId.equals(api.getId())) {
				wantAPI = api;
				break;
			}
		}

		// 遍历集合找到符合的Case
		for (Case c : caseList) {
			if (apiId.equals(c.getApiId())) {
				wantList.add(c);
			}
		}
		// wantList和wantAPI有值了
		// API和Case装到Object[apiId对应的Case个数][2个参数]
		Object[][] datas = new Object[wantList.size()][2];
		for (int i = 0; i < datas.length; i++) {
			datas[i][0] = wantAPI;
			datas[i][1] = wantList.get(i);
		}
		return datas;

	}

	// 泛型
	/**
	 * 读取excel中的sheet，转成对象的List集合
	 * 
	 * @param <E>        实体类型
	 * @param sheetIndex sheet开始索引
	 * @param sheetNum   读取几个sheet
	 * @param clazz      实体类型的字节码对象
	 * @return List<实体类型>的集合
	 */
	public static <E> List<E> read(int sheetIndex, int sheetNum, Class<E> clazz) {
		List<E> list = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Constants.EXCEL_PATH);
			// 导入参数设置类
			ImportParams params = new ImportParams();
			params.setStartSheetIndex(sheetIndex);
			params.setSheetNum(sheetNum);
			// fis:数据来源；Case.class:列和实体类的映射；params:从第几个开始读
			list = ExcelImportUtil.importExcel(fis, clazz, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(fis);
		}
		return list;
	}

}
