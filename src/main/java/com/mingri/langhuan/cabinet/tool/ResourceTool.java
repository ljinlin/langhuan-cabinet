package com.mingri.langhuan.cabinet.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

public class ResourceTool {
	public static final String CLASSPATH_PREFIX = "classpath:";
	public static final String FILEPATH_PREFIX = "file:";

	/**
	 * 
	 * @param path, 必须是 file:或者 classpath: 开头
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static InputStream getInputStream(String path) throws IOException, FileNotFoundException {
		if (path.startsWith(ResourceTool.FILEPATH_PREFIX)) {
			path = path.substring(ResourceTool.FILEPATH_PREFIX.length());
			return new FileInputStream(new File(path));
		} else if (path.startsWith(ResourceTool.CLASSPATH_PREFIX)) {
			path = path.substring(ResourceTool.CLASSPATH_PREFIX.length());
			ClassPathResource crs = new ClassPathResource(path);
			return crs.getInputStream();
		}else {
			throw new FileNotFoundException(path);
		}
	}
}
