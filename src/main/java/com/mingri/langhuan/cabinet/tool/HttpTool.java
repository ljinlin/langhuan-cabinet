package com.mingri.langhuan.cabinet.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class HttpTool {

	private HttpTool() {
	}

	private static final String[] HEADERS_OF_IP = { "x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "X-Real-IP" };

	private static final int HEADER_INDEX_FORWARDED = 0;

	/**
	 * 获取客户端真实请求ip
	 * 
	 * @param request  请求对象
	 * @return 返回ip地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		int headerIndex = 0;
		String ip = null;
		while (true) {
			ip = request.getHeader(HEADERS_OF_IP[headerIndex]);
			if (HttpTool.isValidIp(ip) || ++headerIndex == HEADERS_OF_IP.length) {
				break;
			}
		}
		if (headerIndex == HEADER_INDEX_FORWARDED) {
			// 。多次反向代理后会有多个ip值，第一个ip才是真实ip
			int idx = ip.indexOf(",");
			if (idx != -1) {
				ip = ip.substring(0, idx);
			}
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 依次从header、cookie、request中查找参数
	 * 
	 * @param reques 请求对象
	 * @param paramName 要查找的参数名称
	 * @return 参数值
	 */
	public static String searchForClient(HttpServletRequest reques, String paramName) {
		String value = reques.getHeader(paramName);
		if (StrTool.isNotEmpty(value)) {
			return value;
		}
		Cookie[] cookies = reques.getCookies();
		if (cookies == null) {
			return reques.getParameter(paramName);
		}
		String cookieName = null;
		for (Cookie cookie : cookies) {
			cookieName = cookie.getName();
			value = cookie.getValue();
			if (cookieName.equals(paramName) && StrTool.isNotEmpty(value)) {
				return value;
			}
		}
		return reques.getParameter(paramName);
	}

	public static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";

	public static void responseJson(HttpServletResponse response, Object result, String contentType)
			throws IOException {
		response.setHeader("Content-type", "text/html;charset=" + CharEncoding.UTF_8);
		try (OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), CharEncoding.UTF_8);
				PrintWriter writer = new PrintWriter(osw, true);) {
			response.setCharacterEncoding(CharEncoding.UTF_8);
			String jsonStr = StrTool.EMPTY;
			if (result != null) {
				if (result instanceof String) {
					jsonStr = (String) result;
				} else {
					jsonStr = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
				}
			}
			writer.write(jsonStr);
			writer.flush();
		}
	}

	public static void responseExcel(HttpServletResponse response, InputStream inputStream, String excelName)
			throws IOException {
		response.reset();
		response.setContentType(CONTENT_TYPE_EXCEL); // 改成输出excel文件
		response.setHeader("Content-disposition", "attachment; filename=" + excelName);
		try (OutputStream out = response.getOutputStream()) {// 创建一个文件流，读入Excel文件
			byte[] bt = new byte[1024];
			int n;
			int writeCount = 1;
			while ((n = inputStream.read(bt, 0, bt.length)) != -1) {
				out.write(bt, 0, n);
				if (writeCount % 100 == 0) {
					out.flush();
				}
				writeCount++;
			}
			out.flush();
		}
	}

	public static void responseExcel(HttpServletResponse response, Workbook wb, String respFileName)
			throws IOException {
		response.reset();
		response.setContentType(CONTENT_TYPE_EXCEL); // 改成输出excel文件
		response.setHeader("Content-disposition", "attachment; filename=" + respFileName);
		try (OutputStream stream = response.getOutputStream()) {
			wb.write(stream);
			wb.close();
		}
	}

	public static void responseExcel(HttpServletResponse response, File excel) throws IOException {
		try (InputStream inputStream = new FileInputStream(excel)) {
			responseExcel(response, inputStream, excel.getName());
		}
	}

	public static boolean isValidIp(String ip) {
		return !HttpTool.noValidIp(ip);
	}

	public static boolean noValidIp(String ip) {
		return StrTool.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
	}
}
