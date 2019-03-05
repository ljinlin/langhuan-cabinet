package com.ws.commons.tool;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.examples.CellTypes;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ws.commons.bean.ExcelResolveBean;
import com.ws.commons.exception.DataMatchException;
import com.ws.commons.interfac.HandleKV;
import com.ws.commons.interfac.Result;
import com.ws.commons.interfac.ValidObj; 


/**
 * @Comments : 导入导出Excel工具类
 */

public final class NewExcelUtil {

	/**
	 * @MethodName : excelToList
	 * @Description : 将Excel转化为List
	 * @param in           ：承载着Excel的输入流
	 * @param sheetIndex   ：要导入的工作表序号
	 * @param entityClass  ：List中对象的类型（Excel中的每一行都要转化为该类型的对象）
	 * @param fieldMap     ：Excel中的中文列头和类的英文属性的对应关系Map
	 * @param uniqueFields ：指定业务主键组合（即复合主键），这些列的组合不能重复
	 * @return ：List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> excelToList(ExcelResolveBean excelReadBean) throws Exception {
		Class<T> entityClass = (Class<T>) excelReadBean.getMapClass();
		InputStream in = excelReadBean.getExcelInputStream();
		String sheetName = excelReadBean.getSheetName();
		LinkedHashMap<String, String> fieldMap = excelReadBean.getFieldMap();
		int dataStartIndex = excelReadBean.getDataStartIndex();
		int titleFieldIndex = excelReadBean.getTitleFieldIndex();

		// 定义要返回的list
		List<T> resultList = new ArrayList<T>();
		
			
		
		try {

			// 根据Excel数据源创建WorkBook
			Workbook wb = null;
			try {
				wb = new XSSFWorkbook(in);
			} catch (Exception ex) {
				wb = new HSSFWorkbook(in);
			}

			// 获取工作表
			Sheet sheet = wb.getSheet(sheetName);

			// 获取工作表的有效行数
			int rowLen = sheet.getLastRowNum();
			
			// 如果Excel中没有数据则提示错误
			if (rowLen - 1 <= dataStartIndex) {
				throw new ExcelException("Excel文件中没有任何数据");
			}


			Row titleRow = sheet.getRow(titleFieldIndex);
			int cellLen=titleRow.getLastCellNum();

			LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
			
			Map<String,Field> classFieldMap=null;
			if(!Map.class.isAssignableFrom(entityClass)) {
				classFieldMap=ClassTool.getDecararedFieldsMap(entityClass, false);
			}
			
			// 获取Excel中的列名
			for (int i = 0; i < cellLen; i++) {
				String titleName = titleRow.getCell(i).getStringCellValue().trim().replace("\n", "");
				if (StrTool.isNotEmpty(titleName)) {
					// 将列名和列号放入Map中,这样通过列名就可以拿到列号
					colMap.put(titleName, i);
				}

			}

			for (String cnName : fieldMap.keySet()) {
				if (!colMap.containsKey(cnName)) {
					// 如果有列名不存在，则抛出异常，提示错误
					throw new ExcelException("Excel中缺少必要的字段，或字段名称有误");
				}
			}

			HandleKV handleKV = excelReadBean.getHandleKV();
			ValidObj validObj = excelReadBean.getValidObj();

			LinkedHashMap<Integer, String> respRowMsg = excelReadBean.getRespRowMsg();
			for (int r = dataStartIndex; r < rowLen; r++) {
				// 新建要转换的对象
				T entity = entityClass.newInstance();
				StringBuilder rowMsg = new StringBuilder();
				Field field=null;
				Map<String, Object> map=null;
				if(classFieldMap==null) {
					map=(Map) entity;
				}
				Cell cell = null;
				Row row=sheet.getRow(r);
				if(row==null) {
					continue;
				}
				// 给对象中的字段赋值
				for (Entry<String, String> entry : fieldMap.entrySet()) {
					// 获取中文字段名
					String cnNormalName = entry.getKey();
					// 获取英文字段名
					String enNormalName = entry.getValue();
					// 根据中文字段名获取列号
					int col = colMap.get(cnNormalName);
					cell=row.getCell(col);
					if(cell==null) {
						continue;
					}
					// 获取当前单元格中的内容
					Object cellVal=getCellVal(cell);
					// 给对象赋值
					if (handleKV != null) {
						try {
							cellVal = handleKV.handle(enNormalName, cellVal);
							if(map!=null) {
								map.put(enNormalName, cellVal);
							}else {
								field=classFieldMap.get(enNormalName);
								if(field==null) {
									throw new ExcelException(entityClass.getSimpleName() + "类不存在字段名：" + enNormalName);
								}
								setFieldValueByName(field, cell, entity);
							} 
						} catch (Exception e) {
							if (validObj != null) {
								rowMsg.append("第" + col + "列是非法值");
							}
							e.printStackTrace();
						}
					}
					
				}
				if (validObj != null) {
					Result valiRes = validObj.valid(entity);
					if (rowMsg.length() > 0) {
						if (valiRes.isSuccess()) {
							valiRes = Result.paramError(rowMsg.toString());
						} else {
							valiRes.status.setMsg(rowMsg.append(valiRes.status.getMsg()).toString());
						}
					}
					if (!valiRes.isSuccess()) {
						if (respRowMsg != null) {
							respRowMsg.put(r, valiRes.status.getMsg());
						} else {
							valiRes.status.setMsg("第" + r + "行：" + valiRes.status.getMsg());
							valiRes.throwError();
						}
					} else {
						resultList.add(entity);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}


	/*
	 * <-------------------------辅助的私有方法------------------------------------------
	 * ----->
	 */
	
	/**
	 * @MethodName : getFieldValueByName
	 * @Description : 根据字段名获取字段值
	 * @param fieldName 字段名
	 * @param o         对象
	 * @return 字段值
	 */
	private static Object getFieldValueByName(String fieldName, Object o) throws Exception {

		Object value = null;
		Field field = getFieldByName(fieldName, o.getClass());

		if (field != null) {
			field.setAccessible(true);
			value = field.get(o);
			if (value != null) {
				if (field.getType() == Date.class) {
					if (DateUtils.truncate(value, Calendar.DAY_OF_MONTH).equals(value)) {
						value = DateTool.y_M_d_FORMAT.format(value);
					} else {
						value = DateTool.DATETIME_FORMAT.format(value);
					}
				} else if (BigDecimal.class == field.getType()) {
					value = ((BigDecimal) value).toPlainString();
				}
			}
		} else {
			throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
		}

		return value;
	}

	/**
	 * @MethodName : getFieldByName
	 * @Description : 根据字段名获取字段
	 * @param fieldName 字段名
	 * @param clazz     包含该字段的类
	 * @return 字段
	 */
	private static Field getFieldByName(String fieldName, Class<?> clazz) {
		// 拿到本类的所有字段
		Field[] selfFields = clazz.getDeclaredFields();

		// 如果本类中存在该字段，则返回
		for (Field field : selfFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		// 否则，查看父类中是否存在此字段，如果有则返回
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			return getFieldByName(fieldName, superClazz);
		}

		// 如果本类和父类都没有，则返回空
		return null;
	}

	/**
	 * @MethodName : getFieldValueByNameSequence
	 * @Description : 根据带路径或不带路径的属性名获取属性值
	 *              即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
	 * 
	 * @param fieldNameSequence 带路径的属性名或简单属性名
	 * @param o                 对象
	 * @return 属性值
	 * @throws Exception
	 */
	private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {

		Object value = null;

		// 将fieldNameSequence进行拆分
		String[] attributes = fieldNameSequence.split("\\.");
		if (attributes.length == 1) {
			value = getFieldValueByName(fieldNameSequence, o);
		} else {
			// 根据属性名获取属性对象
			Object fieldObj = getFieldValueByName(attributes[0], o);
			String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
			value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
		}
		return value;

	}

	
	
	
	

	/**
	 * @MethodName : setFieldValueByName
	 * @Description : 根据字段名给对象的字段赋值
	 * @param fieldName  字段名
	 * @param fieldValue 字段值
	 * @param o          对象
	 */
	private static void setFieldValueByName(Field field, Object cellVal, Object o) throws Exception {
		
		if (o instanceof Map) {
			Map<String, Object> map = (Map) o;
			map.put(field.getName(), cellVal);
		} 
		if (cellVal!=null) {
			field.setAccessible(true);
			// 获取字段类型
			Class<?> fieldType = field.getType();
			
			// 根据字段类型给字段赋值
			if (String.class == fieldType) {
				field.set(o, cellVal);
			} else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
				field.set(o, Integer.parseInt(cellVal.toString()));
			} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
				field.set(o, Long.valueOf(cellVal.toString()));
			} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
				field.set(o, Float.valueOf(cellVal.toString()));
			} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
				field.set(o, Short.valueOf(cellVal.toString()));
			} else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
				field.set(o, Double.valueOf(cellVal.toString()));
			} else if (BigDecimal.class == fieldType) {
				field.set(o, BigDecimal.valueOf(Double.valueOf(cellVal.toString())));
			} else if (Character.TYPE == fieldType) {
				if ( cellVal.toString().length() > 0) {
					field.set(o, Character.valueOf(cellVal.toString().charAt(0)));
				}
			} else if (Date.class == fieldType) {
				field.set(o, cellVal);
			} else {
				field.set(o, cellVal);
			}
		} 
	}
	
	private static Object getCellVal(Cell cell) {
		Object val=null;
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
			     if (DateUtil.isCellDateFormatted(cell)) {  
			    	 val = cell.getDateCellValue();  
			        } else {  
			        	val = NumberToTextConverter.toText(cell.getNumericCellValue());  
			        }  
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				val=cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				val=StrTool.EMPTY;
				break;
			case Cell.CELL_TYPE_STRING:
				val= cell.getRichStringCellValue().getString();  
				break;
		    case Cell.CELL_TYPE_FORMULA:  
		        Workbook wb = cell.getSheet().getWorkbook();  
		        CreationHelper crateHelper = wb.getCreationHelper();  
		        FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();  
		        val = getCellVal(evaluator.evaluateInCell(cell));  
		        break;
			default:
				break;
			}
		return val;
	}
	
	/**
	 * @MethodName : setFieldValueByName
	 * @Description : 根据字段名给对象的字段赋值
	 * @param fieldName  字段名
	 * @param fieldValue 字段值
	 * @param o          对象
	 */
	private static void setFieldValueByName(String fieldName, Object fieldValue, Object o) throws Exception {

		Field field = getFieldByName(fieldName, o.getClass());
		if (field != null) {
			field.setAccessible(true);
			// 获取字段类型
			Class<?> fieldType = field.getType();

			// 根据字段类型给字段赋值
			if (String.class == fieldType) {
				field.set(o, String.valueOf(fieldValue));
			} else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
				field.set(o, Integer.parseInt(fieldValue.toString()));
			} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
				field.set(o, Long.valueOf(fieldValue.toString()));
			} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
				field.set(o, Float.valueOf(fieldValue.toString()));
			} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
				field.set(o, Short.valueOf(fieldValue.toString()));
			} else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
				field.set(o, Double.valueOf(fieldValue.toString()));
			} else if (BigDecimal.class == fieldType) {
				field.set(o, BigDecimal.valueOf(Double.valueOf(fieldValue.toString())));
			} else if (Character.TYPE == fieldType) {
				if ((fieldValue != null) && (fieldValue.toString().length() > 0)) {
					field.set(o, Character.valueOf(fieldValue.toString().charAt(0)));
				}
			} else if (Date.class == fieldType) {
				String str = fieldValue.toString();
				if (str.length() > 10) {
					field.set(o, DateTool.DATETIME_FORMAT.parse(str));
				} else {
					field.set(o, DateTool.y_M_d_FORMAT.parse(str));
				}
			} else {
				field.set(o, fieldValue);
			}
		} else {
			if (o instanceof Map) {
				Map<String, Object> map = (Map) o;
				map.put(fieldName, fieldValue);
			} else {
				throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
			}
		}
	}



	public static class ExcelException extends Exception {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		public ExcelException() {
		}

		public ExcelException(String message) {
			super(message);
		}

		public ExcelException(Throwable cause) {
			super(cause);
		}

		public ExcelException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		FileInputStream inputStream = new FileInputStream("D:\\Import_tab_items_template (3).xlsm");
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
		fieldMap.put("商品号", "itemNbr");
		fieldMap.put("UPC/PLU", "UPC");
		fieldMap.put("商品信息", "itemInfo");
		fieldMap.put("正常毛利", "abcn");
		ExcelResolveBean excelReadBean = new ExcelResolveBean(fieldMap, 4, 3, "submititem", true, HashMap.class);
		excelReadBean.startResolve(inputStream);
		List<HashMap> data = excelToList(excelReadBean);
		System.out.println(data);

//		2.0449268E7
//		6.90005010065E11
//		0.2279434974747475
		
		
	}

}
