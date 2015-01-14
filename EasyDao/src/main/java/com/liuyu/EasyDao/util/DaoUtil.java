package com.liuyu.EasyDao.util;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.liuyu.EasyDao.constant.FuncConst;
public class DaoUtil {
	/** 判断Class是否是基本包装类型 */
	@SuppressWarnings("rawtypes")
	public static boolean isWrapClass(Class clz) {
		try {
			return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
	/**判断方法是否是抽象方法*/
	public static boolean isAbstract(Method method) {
		int mod = method.getModifiers();
		return Modifier.isAbstract(mod);
	}
	/**返回首字母变为小写的字符串*/
	public static String getFirstSmall(String name){
		name = name.trim();
		if(name.length()>=2){
			return name.substring(0, 1).toLowerCase()+name.substring(1);
		}else{
			return name.toLowerCase();
		}
		
	}
	/**判断方法名称是否符合约定*/
	public static boolean checkIsUpdateOrAdd(String methodName) {
		String keys[] = FuncConst.INF_METHOD_UPDATE.split(",");
		for (String s : keys) {
			if (methodName.startsWith(s))
				return true;
		}
		return false;
	}
	/**判断方法名称是批量方法*/
	public static boolean checkBatchKey(String methodName) {
		String keys[] = FuncConst.INF_METHOD_BATCH.split(",");
		for (String s : keys) {
			if (methodName.startsWith(s))
				return true;
		}
		return false;
	}
	/**判断是否是查询方法*/
	public static boolean checkIsQuery(String methodName) {
		String keys[] = FuncConst.INF_METHOD_QUERY.split(",");
		for (String s : keys) {
			if (methodName.startsWith(s))
				return true;
		}
		return false;
	}
	/**判断拼接查询*/
	public static String parseQueryCondition(String executeSql,Map<String, Object> sqlParamsMap){
		Set set=sqlParamsMap.entrySet();
		Iterator  iterator=set.iterator();
		while (iterator.hasNext()) { 
			Map.Entry  mapentry = (Map.Entry) iterator.next();
			if(!mapentry.getValue().equals("")){
				executeSql=executeSql+mapentry.getKey().toString()+"=:"+mapentry.getKey().toString();
			}
		}
		return executeSql;
	}
}
