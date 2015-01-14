package com.liuyu.EasyDao.automatic;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.liuyu.EasyDao.annotation.EasyDao;
import com.liuyu.EasyDao.util.DaoUtil;
public class DaoBeanFactory implements BeanFactoryPostProcessor {
	private static final Logger logger = Logger.getLogger(DaoBeanFactory.class);
	/**Dao扫描路径*/
	private List<String> packages;
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		try {
			for(String pack : packages){
				if(org.apache.commons.lang.StringUtils.isNotEmpty(pack)){
				   Set<Class<?>> classSet = ScanUtil.getClasses(pack);
				   for(Class<?> easyDaoClass : classSet){
					   if(easyDaoClass.isAnnotationPresent(EasyDao.class)){
						   //单独加载一个接口的代理类
						   ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
						   proxyFactoryBean.setBeanFactory(beanFactory);
						   proxyFactoryBean.setInterfaces(new Class[]{easyDaoClass});
						   proxyFactoryBean.setInterceptorNames(new String[]{"easyHandler"});
						   String beanName = DaoUtil.getFirstSmall(easyDaoClass.getSimpleName());
						   if(!beanFactory.containsBean(beanName)){
							   logger.info("Dao Interface [/"+easyDaoClass.getName()+"/] onto Spring Bean '"+beanName+"'");
							   beanFactory.registerSingleton(beanName,proxyFactoryBean.getObject());
						   }
					   }
				   }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<String> getPackages() {
		return packages;
	}
	public void setPackages(List<String> packages) {
		this.packages = packages;
	}
}
