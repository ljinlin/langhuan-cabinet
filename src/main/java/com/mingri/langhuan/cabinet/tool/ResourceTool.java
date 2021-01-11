package com.mingri.langhuan.cabinet.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ResourceTool {
	public static final String CLASSPATH_PREFIX = "classpath:";
	public static final String FILEPATH_PREFIX = "file:";

	/**
	 * 
	 * @param path, 必须是 file:或者 classpath: 开头
	 * @return 文件输入流
	 * @throws IOException  io流异常
	 * @throws FileNotFoundException  找不到文件异常
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
	
	
	public static List<Resource> getResource(String locationPattern){
    	String[] locationPatternAry=locationPattern.split(",");
 	   ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
 	    List<Resource> resources = new ArrayList<Resource>();
 	    if (locationPatternAry != null) {
 	      for (String mapperLocation : locationPatternAry) {
 	        try {
 	          Resource[] mappers = resourceResolver.getResources(mapperLocation);
 	          resources.addAll(Arrays.asList(mappers));
 	        } catch (IOException e) {
 	          // ignore
 	        }
 	      }
 	    }
 	    return resources;
	}
}
