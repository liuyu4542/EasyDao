package com.liuyu.EasyDao.util;

public class DBContextHolder {

	@SuppressWarnings("rawtypes")
	private static final ThreadLocal contextHolder=new ThreadLocal();
	
	@SuppressWarnings("unchecked")
	public static void setDataSourceType(DataSourceType dataSourceType){
		contextHolder.set(dataSourceType);
	}
	public static DataSourceType getDataSourceType(){
		return (DataSourceType) contextHolder.get();
	}
	public static void clearDataSourceType(){
		contextHolder.remove();
	}
	
}
