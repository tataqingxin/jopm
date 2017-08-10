package com.zjy.jopm.service;

import java.util.UUID;

import org.junit.Test;

public class ApplicationTest {

	@Test
	public void getMenuTest(){
		int uuCode = UUID.randomUUID().toString().hashCode();
		
		String uucode = UUID.randomUUID().toString();
		//有可能是负数
		if(uuCode<0){
			uuCode = -uuCode;
		}
		// 0 代表前面补充0     
        // 4 代表长度为4     
        // d 代表参数为正数型
		String code = String.format("%015d", uuCode);
		System.out.println(code);
		
	}
}
