package com.mingri.langhuan.cabinet.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class PropertiesTool {

	
	
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
