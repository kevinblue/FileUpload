package com.tw.fileupload.listener;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.tw.fileupload.utils.FileUploadAppProperties;

/**
 * Application Lifecycle Listener implementation class FileUploadListener
 *
 */
@WebListener
public class FileUploadListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public FileUploadListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         
    	InputStream in=getClass().getClassLoader().getResourceAsStream("/upload.properties");
    	Properties properties=new Properties();
    	
    	try {
    		properties.load(in);
    		for(Map.Entry<Object, Object> prop:properties.entrySet()){
    		String propertiesName=(String)prop.getKey();
    	 	String propertiesValue=(String)prop.getValue();
    	 	
    	 	FileUploadAppProperties.getInstance().addProperty(propertiesName,propertiesValue);
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println();
    }
	
}
