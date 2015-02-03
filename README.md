EasyDao是一款轻量的Dao层框架，采用自定义注解、拦截器及反射，基于spring容器开发。
主要目的是简化后台的DAO层的代码开发，只声明接口及sql语句，即可获取返回的结果。
使用场景：
        1.一般查询
	  @executeSql("select * from demo")
	  public List<Map<String,Object>> queryAll();
	  @executeSql注解是执行的sql语句，在注解内写上要查询的sql即可。
	  上面的查询语句是写例子方便，正式环境不建议这么写，做好做缓存查询或者集群。
	2.条件匹配
	 @Params({"id","username"})
	 @executeSql("insert into user_info (id, username) values (:id, :username)")
 	 public int insertUserInfo(String id,String username);
 	 @Params是要传递的条件，顺序、个数和参数保持一致。
	3.参数自匹配查询
	 @Conditions({"id","name"})
	 @Params({"id","name"})
	 @executeSql("select * from demo")
	 public List<Map<String,Object>> queryCondition(String id,String name);
	 @Conditions是定义条件，@Params是传递的参数，两者中的参数顺序要保持一致。
	 @Params传递的参数要是为空，对应的@Conditions中的条件也不查询。适合页面多条件查询。
	4.从资源文件中获取sql
	 @Params("id")
	 @Statement("queryNode")
	 public List<Map<String,Object>> queryStatement(String id);
         @Statement是直接获取写在资源文件中的sql，资源文件采用键值的方式存放。
