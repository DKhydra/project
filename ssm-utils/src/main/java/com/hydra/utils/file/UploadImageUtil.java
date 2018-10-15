package com.hydra.utils.file;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 上传图片工具类（需要根据上传的name创建一个文件夹来存放图片）
 * @author Administrator
 *
 */
public class UploadImageUtil {
	public static String upload(String upPath, MultipartFile myfile)throws Exception{
//		String newName = null;
//		if(!ts.isEmpty()){
//			//获取服务器路径
//			String path = upPath + "//upload//"+ts.getName()+"//";
//			//获取原文件名 避免文件名重复覆盖
//			String oldName = ts.getOriginalFilename();
//			//通过UUID来创建新的文件名
//			newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));
//			path +=newName;
//			File file = new File(path);
//			ts.transferTo(file);
//		}
//		return newName;
		
		List<String> imgPathList = new ArrayList<String>();
            if(myfile.isEmpty()){  
                System.out.println("文件未上传");  
            }else{  
                System.out.println("文件长度: " + myfile.getSize());  
                System.out.println("文件类型: " + myfile.getContentType());  
                System.out.println("文件名称: " + myfile.getName());  
                System.out.println("文件原名: " + myfile.getOriginalFilename());  
                System.out.println("========================================");  
                
//                //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中  
//                String realPath = request.getSession().getServletContext().getRealPath("/upload"); 
                //使用自定义文件资源库
                String realPath = upPath+"/"+myfile.getName();
//                String realPath = upPath;
                //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
                try {
                	//重置文件名
                	long time = System.currentTimeMillis();
                	String timeStr = String.valueOf(time);
                	String[] originalFileName = myfile.getOriginalFilename().split("\\.");
                	String fileName = timeStr+"."+originalFileName[1];
					FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, fileName));
					//配置图片访问路径					
					imgPathList.add("upload/"+myfile.getName()+"/"+fileName);
                } catch (IOException e) {
					e.printStackTrace();
					return null;
				}  
            }  
		
		return imgPathList.get(0);
	}
}
