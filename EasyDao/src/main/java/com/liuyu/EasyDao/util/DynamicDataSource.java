package com.liuyu.EasyDao.util;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
public class DynamicDataSource extends AbstractRoutingDataSource {

	/* 
	 * 该方法必须要重写  方法是为了根据数据库标示符取得当前的数据库
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		DataSourceType dataSourceType= DBContextHolder.getDataSourceType();
		return dataSourceType;
	}

	@Override
	public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
		super.setDataSourceLookup(dataSourceLookup);
	}

	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		super.setDefaultTargetDataSource(defaultTargetDataSource);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setTargetDataSources(Map targetDataSources) {
		super.setTargetDataSources(targetDataSources);
	}

}
