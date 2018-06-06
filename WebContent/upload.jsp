<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.js"></script>
<!--  <script src="../../scripts/jquery-1.7.2.js"></script> -->

<script type="text/javascript">
  
$(function(){
	   
	   var i=2;
	   $("#addFile").click(function(){
		   $(this).parent().parent().before("<tr><td>File"
				   +i+":</td><td><input type='file' name='file"
				   +i+"'/></td></tr>"
				   +"<tr><td>Desc"
				   +i+":</td><td><input type='text' name='desc'"
				   +i+"'/><button id='delete"
				   +i+"'>删除</button></td></tr>");
		 
		//   i=i+1;
		   i++;
		   
		   $("delete"+(i-1)).click(function(){		   
			var $tr=$(this).parent().parent();
			$tr.prev("tr").remove();
			$tr.remove();
			
			 //排序
			   $(".file").each(function(index){
				   var n=index+1;
				   $(this).find("td:first").text("File"+n);
				   $(this).find("td:last input").attr("name","file"+n);
				   
				   
			   });
			   
			   $(".desc").each(function(index){
				   var n=index+1;
				   $(this).find("td:first").text("Desc"+n);
				   $(this).find("td:last input").attr("name","desc"+n);
				   			   
			   });
			   i--;
			
		   });
		   
		   
		  
		   return false;//取消行为
	   });
	   
   });


</script>
</head>
<% String context = request.getContextPath();//cloudnoteDemo%>
<body>
     <form action="<%=context%>/servlet/UploadSevlet"  method="post" enctype="multipart/form-data">
       <table>
         <tr>
              <td> File1:</td>
              <td><input type="file" name="file1"/></td>
         </tr>
         <tr>
              <td> Desc1:</td>
              <td><input type="text" name="desc1"/></td>
         </tr>
         <tr>
              <td><input type="submit" value="提交"/></td>
              <td><button  id="addFile">新增一个附件</button></td>
         </tr>
       </table>
           
     </form>
     
     
     
</body>
</html>