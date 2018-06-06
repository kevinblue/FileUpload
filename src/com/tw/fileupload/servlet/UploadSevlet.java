package com.tw.fileupload.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
	private static final String File_PATH="/WEB-INF/files/";   
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
		//解决乱码问题
		request.setCharacterEncoding("utf-8");
		ServletFileUpload upload = getServletFileUpload();
		//upload.setFileSizeMax();
		
		try {
			//把需要上传的FileItem都放入到该Map中
			//键：文件待 存放的路径 ，值：对应的FileItem对象对
			Map<String,FileItem> uploadFiles=new HashMap<String,FileItem>();
			//解析请求，得到FileItem的集合
			List<FileItem> items=upload.parseRequest(request);
			
			//1构建FileUploadBean的集合，同时填充uploadFiles
			List<FileUploadBean> beans=buildFileUploadBeans(items,uploadFiles);
			 
			
			//2校验扩展名
			vaidateExtName(beans);
			//3校验文件大小:在解析的时候已经校验了，我只需要通过异常得到结果
			//4进行文件的上传操作
			 upload(uploadFiles);
			//5.吧上传的信息保存到数据库中
		    saveFileUploadBeans(beans);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}

private void saveFileUploadBeans(List<FileUploadBean> beans) {
		// TODO Auto-generated method stub
		
	}


/**
 * 上传
 * @param uploadFiles
 * @throws IOException 
 */
private void upload(Map<String, FileItem> uploadFiles) throws IOException {
		
		for(Map.Entry<String, FileItem>  uploadfile:uploadFiles.entrySet()){
			String filePath=uploadfile.getKey();
		    FileItem item= uploadfile.getValue();
		    upload(filePath,item.getInputStream());
		}
	}



private void upload(String filePath, InputStream inputStream) throws IOException {
	
	OutputStream out=new FileOutputStream(filePath);
	byte[] buffer =new byte[1024];
	int len=0;
	while((len=inputStream.read(buffer))!=-1){
		out.write(buffer, 0, len);
	}
	inputStream.close();
	out.close();
	System.out.println(filePath);
}



/**
 * 校验扩展名
 * @param beans
 */
private void vaidateExtName(List<FileUploadBean> beans) {
		// TODO Auto-generated method stub
		
	}



private List<FileUploadBean> buildFileUploadBeans(List<FileItem> items, Map<String, FileItem> uploadFiles) throws UnsupportedEncodingException {
		//1遍历FileItem的集合，先得到desc的Map<String,String>,其中键：fileName(desc1,desc2...),
	//值表单域对应字段的值
	
	List<FileUploadBean> beans=new ArrayList<FileUploadBean>();
	
	Map<String ,String> descs=new HashMap<String ,String>();
	
	for(FileItem item: items){
		if(item.isFormField()){
			String fieldName=item.getFieldName();
			String desc=item.getString("UTF-8");
			
			 descs.put(item.getFieldName(), desc);	
		}
	   
	}
	
	for(FileItem item: items){
		if(!item.isFormField()){
		String fieldName=item.getFieldName();
		String index=fieldName.substring(fieldName.length()-1);
		String fileName=item.getName();
	    String desc	=descs.get("desc"+index);
		String  filePath=getFilePath(fileName);
	    
		System.out.println(filePath);
		FileUploadBean bean=new FileUploadBean(fileName,filePath,desc);
		beans.add(bean);
		
		uploadFiles.put(filePath, item);
		}
	   
	}
	//2在遍历FileItem的集合，得到文件域的FileItem对象。
	//妹得到一个FileItem对象都创建一个FileUploadBean对象
	//得到filename,构建filepath，从1的map中得到当前FileItem对应的那个desc
	//使用fileName后面的数字去匹配
		return beans;
	}



private String getFilePath(String fileName) {
     String extName=fileName.substring(fileName.lastIndexOf(".")+1);
     Random random=new Random();
       int radnum=random.nextInt(100000);
       //F:\testlyf\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\FileUpload\WEB-INF\files\152828083375725746.txt
	 String filePath=getServletContext().getRealPath(File_PATH)+"\\"+System.currentTimeMillis()+radnum+extName;
	 return filePath;
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
