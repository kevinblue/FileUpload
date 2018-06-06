package com.tw.fileupload.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.tw.fileupload.bean.FileUploadBean;
import com.tw.fileupload.utils.FileUploadAppProperties;

/**
 * Servlet implementation class UploadSevlet
 */

public class UploadSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadSevlet() {
        super();
        // TODO Auto-generated constructor stub
    }



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.获取请求信息：
		//InputStream  in= request.getInputStream();
	//	  //String charset=get_charset(in);  
//		Reader reader=new InputStreamReader(in);
//		BufferedReader bufferReader=new BufferedReader(reader);
//		String str="";
//		while((str=bufferReader.readLine())!=null){
//			System.out.println(str);
//		}
		//测试文件约束
		//String exts =FileUploadAppProperties.getInstance().getProperty("exts");
		//String fileMaxSize =FileUploadAppProperties.getInstance().getProperty("file.MaxSize");
		//String totalfilemaxsize =FileUploadAppProperties.getInstance().getProperty("total.file.max.size");
		
		//System.out.println(exts);
		//System.out.println(fileMaxSize);
		//System.out.println(totalfilemaxsize);
		
		ServletFileUpload upload = getServletFileUpload();
		//upload.setFileSizeMax();
		
		try {
			
			//解析请求，得到FileItem的集合
			List<FileItem> items=upload.parseRequest(request);
			
			//1构建FileUploadBean的集合
			List<FileUploadBean> beans=buildFileUploadBeans(items);
			
			//2校验扩展名
			vaidateExtName(beans);
			//3校验文件大小:在解析的时候已经校验了，我只需要通过异常得到结果
			//4进行文件的上传操作
			//upload();
			//5.吧上传的信息保存到数据库
		    //saveFileUploadBeans()
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}

/**
 * 校验扩展名
 * @param beans
 */
private void vaidateExtName(List<FileUploadBean> beans) {
		// TODO Auto-generated method stub
		
	}



private List<FileUploadBean> buildFileUploadBeans(List<FileItem> items) {
		// TODO Auto-generated method stub
		return null;
	}



/**
 * 获取约束条件
 * @return
 */
	private ServletFileUpload getServletFileUpload() {
		String exts =FileUploadAppProperties.getInstance().getProperty("exts");
	    String fileMaxSize =FileUploadAppProperties.getInstance().getProperty("file.MaxSize");
	    String totalfilemaxsize =FileUploadAppProperties.getInstance().getProperty("total.file.max.size");
		DiskFileItemFactory factory=new DiskFileItemFactory();
		factory.setSizeThreshold(1025*500);		
		File tempDirectory=new File("d:\\tempDirectory");
		factory.setRepository(tempDirectory);
		
		ServletFileUpload upload=new ServletFileUpload(factory);
		upload.setSizeMax(Integer.parseInt(fileMaxSize));
		return upload;
	}
	
    public static String get_charset(File file) {  
        String charset = "GBK";  
        byte[] first3Bytes = new byte[3];//首先3个字节  
        try {  
            boolean checked = false;  
            ;  
            BufferedInputStream bis = new BufferedInputStream(  
                    new FileInputStream(file));  
            bis.mark(0);  
            int read = bis.read(first3Bytes, 0, 3);  
            if (read == -1)  
                return charset;  
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {  
                charset = "UTF-16LE";  
                checked = true;  
            } else if (first3Bytes[0] == (byte) 0xFE  
                    && first3Bytes[1] == (byte) 0xFF) {  
                charset = "UTF-16BE";  
                checked = true;  
            } else if (first3Bytes[0] == (byte) 0xEF  
                    && first3Bytes[1] == (byte) 0xBB  
                    && first3Bytes[2] == (byte) 0xBF) {  
                charset = "UTF-8";  
                checked = true;  
            }  
            bis.reset();  
            if (!checked) {  
                // int len = 0;  
                int loc = 0;  
  
                while ((read = bis.read()) != -1) {  
                    loc++;  
                    if (read >= 0xF0)  
                        break;  
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK  
                        break;  
                    if (0xC0 <= read && read <= 0xDF) {  
                        read = bis.read();  
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)  
                            // (0x80  
                            // - 0xBF),也可能在GB编码内  
                            continue;  
                        else  
                            break;  
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小  
                        read = bis.read();  
                        if (0x80 <= read && read <= 0xBF) {  
                            read = bis.read();  
                            if (0x80 <= read && read <= 0xBF) {  
                                charset = "UTF-8";  
                                break;  
                            } else  
                                break;  
                        } else  
                            break;  
                    }  
                }  
  
            }  
  
            bis.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return charset;  
    }  

}
