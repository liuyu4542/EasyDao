package com.liuyu.EasyDao.handler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.liuyu.EasyDao.annotation.Conditions;
import com.liuyu.EasyDao.annotation.Params;
import com.liuyu.EasyDao.annotation.executeSql;
import com.liuyu.EasyDao.util.DaoUtil;
//拦截处理
public class DaoHandler implements MethodInterceptor {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Method method = methodInvocation.getMethod();
		Object[] args = methodInvocation.getArguments();
		//返回结果
		Object returnObj = null;
		//参数
		Map<String, Object> sqlParamsMap = null;
		//判断是否是抽象方法，如果是非抽象方法，则不执行拦截器
		if (!DaoUtil.isAbstract(method)) {
	        return methodInvocation.proceed();
	    }
		//获取执行的参数集合
		sqlParamsMap = getParamsData(method, sqlParamsMap, args);
		// 获取可执行的sql语句
		String executeSql = parseSqlTemplate(method,sqlParamsMap);
		// 获取SQL执行返回值
		returnObj = getResult(method,executeSql,sqlParamsMap);
		return returnObj;
	}
	/**返回查询结果*/
	private Object getResult(Method method, String executeSql,Map<String, Object> sqlParamsMap) {
		String methodName = method.getName();
		if (DaoUtil.checkIsUpdateOrAdd(methodName)) {
			return getUpdateResultNotBatch(executeSql,sqlParamsMap);
		}else if(DaoUtil.checkIsQuery(methodName)){
			return queryResult(method,executeSql,sqlParamsMap);
		}
		return null;
	}
	/**获取参数集合*/
    private Map<String, Object> getParamsData(Method method,Map<String, Object> sqlParamsMap,Object[] args) throws Exception{
    	boolean flag = method.isAnnotationPresent(Params.class);
    	sqlParamsMap=new HashMap<String, Object>();
    	if(flag){
    		Params arguments = method.getAnnotation(Params.class);
    			if(arguments.value().length > args.length){
    				throw new Exception("实际参数个数与于定义参数个数不符");
    			}
    			int args_num = 0;
    			for(String v:arguments.value()){
    				/**除去空值*/
    				if(!args[args_num].equals("")){
    					sqlParamsMap.put(v, args[args_num]);
    					args_num++;
    				}
    			}
    		}
		return sqlParamsMap;
    }
	/**获取执行sql*/
	private String parseSqlTemplate(Method method,Map<String, Object> sqlParamsMap){
		String executeSql = null;
		/**直接执行sql*/
		if(method.isAnnotationPresent(executeSql.class)){
			executeSql sql = method.getAnnotation(executeSql.class);
			if(StringUtils.isNotEmpty(sql.value())){
				executeSql = sql.value();
			}
		}
		/**sql条件信息*/
		if(method.isAnnotationPresent(Conditions.class)){
			if(sqlParamsMap!=null){
				executeSql=executeSql+"where ";
			}
			/**获取多条件执行语句*/
			executeSql=DaoUtil.parseQueryCondition(executeSql, sqlParamsMap);
			
		}
		return executeSql;
	}
	/**非批量更新*/
	private Object getUpdateResultNotBatch(String executeSql,Map<String, Object> ParamsMap) {
		if(ParamsMap!=null){
			return namedParameterJdbcTemplate.update(executeSql, ParamsMap);
		}else{
			return jdbcTemplate.update(executeSql);
		}
	}
	/**查询*/
	private Object queryResult(Method method,String executeSql,Map<String, Object> ParamsMap){
		Class<?> returnType = method.getReturnType();
		if (returnType.isAssignableFrom(List.class)) {
			if(ParamsMap!=null){
				return namedParameterJdbcTemplate.queryForList(executeSql, ParamsMap);
			}else{
				return jdbcTemplate.queryForList(executeSql);
			}
		}else if (returnType.isAssignableFrom(Map.class)) {
			if(ParamsMap!=null){
				return namedParameterJdbcTemplate.queryForMap(executeSql, ParamsMap);
			}else{
				return jdbcTemplate.queryForMap(executeSql);
			}
		}else if (returnType.isAssignableFrom(String.class)) {
			if(ParamsMap!=null){
			    return namedParameterJdbcTemplate.queryForList(executeSql, ParamsMap);
			}
		}
		if(DaoUtil.isWrapClass(returnType)){
			if(ParamsMap!=null){
				return namedParameterJdbcTemplate.queryForObject(executeSql, ParamsMap, String.class);
			}else{
				return jdbcTemplate.queryForInt(executeSql);
			}
		}
		return null;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
