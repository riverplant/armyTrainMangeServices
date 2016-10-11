package com.ue.auditmanage.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Controller
@RequestMapping("/file")
public class downloadAndUploadController {
	/**
	 * 文件上传
	 */
	@RequestMapping("/upload")
	/**
	 * 
	 * @param file1
	 * @param file2
	 * @param request
	 * @return
	 * @RequestParam("file1")中的名字必须和 选择文件1: <input type="file" name="file1"/> 中name的值一致
	 */
	public String upload(@RequestParam("file1") CommonsMultipartFile file1,
			@RequestParam("file2") CommonsMultipartFile file2,
			HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		System.out.println("file1的名称-》" + file1.getOriginalFilename());
		System.out.println("file2的名称-》" + file2.getOriginalFilename());
		String success = "{\"reulst\":\"success\"}";
		String error = "{\"reulst\":\"error\"}";
		ArrayList<CommonsMultipartFile> files = new ArrayList<CommonsMultipartFile>();

		if (!file1.isEmpty())
			files.add(file1);
		if (!file2.isEmpty())
			files.add(file2);
		for (CommonsMultipartFile f : files) {
			if (!f.isEmpty()) {
				try {
					// 定义输出流
					FileOutputStream os = new FileOutputStream(path
							+ new Date().getTime() + f.getOriginalFilename());
					System.out.println("文件保存路径是:"+path
							+ new Date().getTime() + f.getOriginalFilename());
					// 定义输入流
					InputStream in = f.getInputStream();
					int b = 0;
					byte[] buffer = new byte[8192];
					while ((b = in.read(buffer)) != -1) {
						os.write(buffer, 0, b);
					}
					os.flush();
					os.close();
					in.close();
					System.out.println(success);
					request.setAttribute("result", success);
				} catch (Exception e) {
					request.setAttribute("result", error);
					e.printStackTrace();
				}
			}

		}

		return "/uploadOk";
	};
	
	
	/**
	 * 文件上传
	 */
	@RequestMapping("/upload2")
	/**
	 * 上传优化,利用springMVC自己定义的MultipartHttpServletRequest
	 * @param request
	 * @param response
	 * @return
	 */
	public String upload2(HttpServletRequest request,HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/");
		//获得springMVC解析器
		//getServletContext()获得springmvc初始化的一些数据
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
         if(multipartResolver.isMultipart(request)){
        	//当request传过来的数据是 Multipart 
        	 MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        //springMVC自定义
        	 Iterator<String> iters = multiRequest.getFileNames();
        	 while(iters.hasNext()){
        		 MultipartFile file = multiRequest.getFile((String)iters.next());
        		 if(file!=null){
        			 File localfle = new File(path+file.getOriginalFilename());
            		 try {
    					file.transferTo(localfle);
    					System.out.println("文件上传为:"+path+file.getOriginalFilename());
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}	 
        		 }
        		 
        	 }
         }
		

		return "/uploadOk";
	};

	 @RequestMapping("/download")
	    public String download(String fileName, HttpServletRequest request,
	            HttpServletResponse response) {
	        response.setCharacterEncoding("utf-8");
	        response.setContentType("multipart/form-data");
	        response.setHeader("Content-Disposition", "attachment;fileName="
	                + fileName);
	        try {
	            String path = Thread.currentThread().getContextClassLoader()
	                    .getResource("").getPath()
	                    + "download";//这个download目录为啥建立在classes下的
	            InputStream inputStream = new FileInputStream(new File(path
	                    + File.separator + fileName));
	 
	            OutputStream os = response.getOutputStream();
	            byte[] b = new byte[2048];
	            int length;
	            while ((length = inputStream.read(b)) > 0) {
	                os.write(b, 0, length);
	            }
	 
	             // 这里主要关闭。
	            os.close();
	 
	            inputStream.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	            //  返回值要注意，要不然就出现下面这句错误！
	            //java+getOutputStream() has already been called for this response
	        return null;
	    }

	/**
	 * 前往下载文件页面
	 * 
	 * @return
	 */
	@RequestMapping("/godownload")
	public String godownload() {

		return "/upload";
	};
}
