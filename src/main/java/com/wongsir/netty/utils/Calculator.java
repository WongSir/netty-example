package com.wongsir.netty.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/** 
* @Description: 服务端中使用到的用于计算的工具类 
* @author hjd
* @date 2017年1月13日 下午5:03:29 
*  
*/
public final class Calculator {
	private final static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
	public static Object cal(String expression) throws ScriptException{
		return jse.eval(expression); //使用JavaScript脚本中的eval()函数来进行字符串表达式计算 
	}
}
