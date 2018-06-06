package com.tw.fileupload.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式
 * @author 公共
 *
 */
public class FileUploadAppProperties {
   
	
	private  Map<String,String> properties=new  HashMap<>();
	
	private FileUploadAppProperties(){};
	
	
	private  static FileUploadAppProperties instance= new FileUploadAppProperties();
	
	public  static FileUploadAppProperties getInstance(){
		return instance;
	}
	
	public void  addProperty(String propertyName,String propertyValue){
		properties.put(propertyName,propertyValue);
	}
	
	public String getProperty(String propertyName){
		return  properties.get(propertyName);
	}
}
