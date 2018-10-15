package com.hydra.utils.file;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BulkUtil {

	//解压
	public static void unZipFiles(File zip,String directory)throws Exception{
		  try
		    {
//				File imgFile = new File("D:/server/community/webapps/communityoa-web/upload/",pimage.getOriginalFilename());
//				pimage.transferTo(imgFile);
//				BulkUtil.unZipFiles(imgFile, "D:\\server\\community\\webapps\\communityoa-web\\upload\\");
		        ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
		        ZipEntry ze = zis.getNextEntry();
		        File parent = new File(directory);
		        if (!parent.exists() && !parent.mkdirs())
		        {
		            throw new Exception("创建解压目录 \"" + parent.getAbsolutePath() + "\" 失败");
		        }
		        while (ze != null)
		        {
		            String name = ze.getName();
		            File child = new File(parent, name);
		                FileOutputStream output = new FileOutputStream(child);
		            byte[] buffer = new byte[10240];
		            int bytesRead = 0;
		            while ((bytesRead = zis.read(buffer)) > 0)
		            {
		                output.write(buffer, 0, bytesRead);
		            }
		            output.flush();
		            output.close();
		                ze = zis.getNextEntry();
		            }
		            zis.close();
		    }
		    catch (IOException e)
		    {
		    }
	}
	//file为上传后的文件，tempFile为临时文件
	public static void transfer(File file,File tempFile) throws IOException{
		OutputStream os = new FileOutputStream(file);  
        InputStream is = new FileInputStream(tempFile);  
        byte[] buf = new byte[1024];  
        int length = 0 ;  
        while(-1 != (length = is.read(buf) ) )  
        {  
            os.write(buf, 0, length) ;  
        }  
        is.close();  
        os.close();  
	}
}
