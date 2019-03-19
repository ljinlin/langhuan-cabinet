package com.ws.commons.pool.sql;

import org.springframework.core.InfrastructureProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource implements InfrastructureProxy{
	
	 private static final ThreadLocal<Object> contextHolder = new ThreadLocal<>();
	 
	 public static void select(Object dataSourceKey) {
		 contextHolder.set(dataSourceKey);
	 }
	 
	 public static void unSelect() {
		 contextHolder.remove();
	 }
	 
	 
	 
	@Override
	protected Object determineCurrentLookupKey() {
		return contextHolder.get();
	}
 
	@Override
	public Object getWrappedObject() {
		return determineTargetDataSource();
	}

//	@Override  ScopedObject
//	public Object getTargetObject() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void removeFromScope() {
//		// TODO Auto-generated method stub
//	}
	

	
	

}  
