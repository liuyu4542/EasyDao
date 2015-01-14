package com.liuyu.EasyDao.demo;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.liuyu.EasyDao.dao.TestDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false)
@Transactional	//声明开启事务
@Service("testService")
public class TestDemo {
	@Autowired
	private TestDao testdao;
	//@Test
	public void count(){
		System.out.println(testdao.getCount()); 
	}
	//@Test
	public void insert(){
		testdao.insertUserInfo("18", "otooo");
	}
	//@Test
	public void update(){
		testdao.updateUserInfo("12", "otuuu");
	}
	//@Test
	public void queryAll(){
		List<Map<String, Object>> mp=testdao.queryAll();
		for(Map<String, Object> m:mp){
			System.out.println(m.get("id").toString());
			System.out.println(m.get("username").toString());
		}
	}
	@Test
	public void queryCondition(){
		List<Map<String, Object>> mp=testdao.queryCondition("12","");
		for(Map<String, Object> m:mp){
			System.out.println(m.get("id").toString());
			System.out.println(m.get("username").toString());
		}
	}
}
