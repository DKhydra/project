package com.hydra.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.hydra.exception.XbaseError;
import com.hydra.exception.XbaseException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * @author
 * @date
 * desc:
 */


public class OssUtils {
    private static Logger logger = Logger.getLogger(OssUtils.class);

    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
    // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
    // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
    // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
    // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
    // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
    private static String accessKeyId = "***************";
    private static String accessKeySecret = "*****************";

    // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
    private static String bucketName = "************";

    // 对上传图片进行分片: 模块名称+ "/" +时间戳+文件类型 组成唯一 key
    // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件。详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Object命名规范如下：使用UTF-8编码，长度必须在1-1023字节之间，不能以“/”或者“\”字符开头。
    //文件分片目录
    private String filedir;


    private OSSClient ossClient;

    public OssUtils() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        this.filedir="data/";
    }

    public OssUtils(String moduleName) {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        this.filedir = moduleName + "/";
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 上传图片并获取图片url
     */
    public String uploadImage(MultipartFile file) throws IOException{
        if (file == null || file.getSize() <= 0) {
            throw new XbaseException(XbaseError.IMAGE_UPLOAD_NULL_ERROR);
        }
        String name = uploadImg2Oss(file);
        String imgUrl = getImgUrl(name);
        return imgUrl;
    }

    /**
     * 删除图片
     */
    public void deleteImage(String imageUrl) {
        int index = imageUrl.indexOf("data");
        String key = "";
        if(index!= -1){
            key = imageUrl.substring(index, imageUrl.length() );
            // 删除Object
            ossClient.deleteObject(bucketName, key);
        }else {
            throw new XbaseException(XbaseError.IMAGE_DELETE_URL_ERROR);
        }
    }

    public static void main(String[] args) {


    }

    /**
     * 下面一些基础方法
     */

    public String uploadImg2Oss(MultipartFile file) {
        if (file.getSize() > 5*1024 * 1024) {
            throw new XbaseException(XbaseError.IMAGE_OVERLOAD_ERROR);
        }
        String originalFilename = file.getOriginalFilename();
        String imgType = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + imgType; //
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new XbaseException(XbaseError.IMAGE_UPLOAD_FAILER_ERROR);
        }
    }

    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        String uniqeKey = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            uniqeKey = filedir + fileName;
            System.out.println("AAA: "+uniqeKey);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, uniqeKey, instream, objectMetadata);
            System.out.println("AAA: "+uniqeKey);
            //设置读写权限
            ossClient.setObjectAcl(bucketName, uniqeKey, CannedAccessControlList.PublicReadWrite);
            ret = putResult.getETag();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return getUrl(this.filedir + split[split.length - 1]);
        }
        return null;
    }

    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

}
