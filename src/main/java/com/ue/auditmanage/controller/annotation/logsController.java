package com.ue.auditmanage.controller.annotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import util.JsonDateValueProcessor;
import util.dom4jdao;
import util.util;
import vo.vLog;

/**
 * 引入Controller注解
 * 
 * @author riverplant
 * 
 */
// 类的注解
@Controller
@RequestMapping("/logsServices")
/**
 * 配置了@RequestMapping("/user")之后，里面所有的new ModelAndView("/annotation","result",result);
 * 都不需要再写/user了
 * @author riverplant
 *
 */
public class logsController {
	static final Map<Integer,String > dic = util.getdicMonoriting();


	/**
	 * 服务实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getLogs")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getLogs(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor()); Date updateDateStart_temp = null;Date updateDateEnd_temp =null;
		boolean flag = false; int id=1;SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 int rows = 10;int page = 1;PrintWriter writer = response.getWriter();
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
	String rootPath = util.getRootPath();
	File files = new File(rootPath.substring(0, rootPath.indexOf("WEB-INF/"))+"logs/");
	File[]fileLists = files.listFiles();
	if(request.getParameter("updateDateStart")!=null && !"".equalsIgnoreCase(request.getParameter("updateDateStart"))){
		updateDateStart_temp =sdf.parse(request
				.getParameter("updateDateStart")) ;
	}
	if(request.getParameter("updateDateEnd")!=null && !"".equalsIgnoreCase(request.getParameter("updateDateEnd"))){
		updateDateEnd_temp =sdf.parse(request
				.getParameter("updateDateEnd")) ;
	}
	
	List<vLog> logs  = new ArrayList<vLog>();
		 if(fileLists!=null && fileLists.length>0){
			 for(int i=0;i<fileLists.length;i++) {
				if(fileLists[i].isFile() && util.isdansintervall(updateDateStart_temp, updateDateEnd_temp, util.getModifiedTime(fileLists[i].getAbsolutePath()))){
					vLog log = new vLog();
					log.setFilename(fileLists[i].getName());
					log.setId(id++);
					log.setSize(fileLists[i].getTotalSpace());
					log.setUpdateDate(sdf.format(util.getModifiedTime(fileLists[i].getAbsolutePath())));
					logs.add(log);
				}
					
				} 
			 List<vLog> fenyeLogs = util.getFenyeFiles(logs, rows, page);
			 flag = true;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total",logs.size());
				map.put("rows",fenyeLogs );
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
		 } else {
				String json = "[]";
				writer.write(json);
				writer.flush();
				writer.close();

			}
		 
		
	

	}
	

	/**
	 * 删除日志
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delLogs")
	public void delLogs(HttpServletRequest request,
			HttpServletResponse response) {
		
		boolean flag = false;
		String[] filenames = request.getParameter("filename").split(",");
		ArrayList<vLog> vLogs = new ArrayList<vLog>();
		String rootPath = util.getRootPath();
		String desPath = rootPath.substring(0, rootPath.indexOf("WEB-INF/")) + "logs/";
		if (filenames != null && filenames.length > 0) {
			for (String filename : filenames) {
				
				File files = new File(desPath);
				System.out.println(rootPath+"logs/");
				File[]fileLists = files.listFiles();
				if( fileLists!=null && fileLists.length>0 ){
					for(int i=0;i<fileLists.length;i++){
						if(filename.equalsIgnoreCase(fileLists[i].getName())){
							flag=true;
							vLog log = new vLog();
							log.setFilename(fileLists[i].getName());
							fileLists[i].delete();
							vLogs.add(log);
							break;
						}
					}
				}else{
					System.out.println(rootPath+"logs/"+"下无日志");
				}
				
				
			}
		}
		util.writeJson(JSONArray.fromObject(vLogs), flag, request,
				response);
	}


	/**
	 * 显示日志
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/showLogs")
	public void showLogs(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 request.setCharacterEncoding("utf-8");  
	        response.setContentType("text/xml; charset=UTF-8"); String rootPath = util.getRootPath(); 
	       
			String desPath = rootPath.substring(0, rootPath.indexOf("WEB-INF/")) + "logs/";
		String filename = request.getParameter("filename");PrintWriter out = response.getWriter();
		File files = new File(desPath);
		File[]fileLists = files.listFiles();
		String count;
		for(int i=0;i< fileLists.length;i++){
			if(fileLists[i].getName().equalsIgnoreCase(filename)){
				count =util.getDPLogContent(fileLists[i]);
				out.write(count);
				break;
				
			}
		}
		out.flush();
		out.close();
		
	}
	
	/**
	 * 显示日志
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/showLogsByBrower")
	public void showLogsByBrower(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 request.setCharacterEncoding("utf-8");  
	        response.setContentType("text/xml; charset=UTF-8"); String rootPath = util.getRootPath(); 
	        String desPath = rootPath.substring(0, rootPath.indexOf("WEB-INF/")) + "logs/"; 
		String filename = request.getParameter("filename");PrintWriter out = response.getWriter();
		File files = new File(desPath);
		File[]fileLists = files.listFiles();
		for(int i=0;i< fileLists.length;i++){
			if(fileLists[i].getName().equalsIgnoreCase(filename)){
				
				out.write(rootPath+"baklogs/"+filename);
				break;
				
			}
		}
		out.flush();
		out.close();
		
	}
	/**
	 * 显示日志
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/EditeQuartz")
	public void EditeQuartz(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 request.setCharacterEncoding("utf-8");  
	        response.setContentType("text/xml; charset=UTF-8"); 
	    String rootPath = util.getRootPath();     
	    String path = rootPath + "classes/beans.xml";
		Document doc = dom4jdao.getParse(path);
		
		Element root = doc.getRootElement();
		
		@SuppressWarnings("unchecked")
		List<Element> beans = root.elements("bean");
		for(Element e : beans){
			if(e.attribute("id").getText().trim().equalsIgnoreCase("doTime")){
				Element cronExpression = (Element) e.elements("property").get(1);
				cronExpression.element("value").setText("");
				dom4jdao.reload(doc, path);
			}
		}
		
	}
	
	
	/**
	 * 导出日志
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	/**
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportLogs")
	public  void exportLogs(HttpServletRequest request,
			HttpServletResponse response) {
		
		String[] filenames = request.getParameter("filename").split(",");
		if (filenames != null && filenames.length > 0) {
			for (String filename : filenames) {
				String rootPath = util.getRootPath();
				File files = new File(rootPath+"logs/");
				
				File[]fileLists = files.listFiles();
				for(int i=0;i<fileLists.length;i++){
					if(filename.equalsIgnoreCase(fileLists[i].getName())){
						try {
							 response.setCharacterEncoding("utf-8");
						        response.setContentType("multipart/form-data");
						        response.setHeader("Content-Disposition","attachment;fileName="+fileLists[i].getName());
						        OutputStream outputStream = response.getOutputStream();
					               InputStream inputStream = new FileInputStream(fileLists[i].getAbsolutePath());
					               byte[] buffer = new byte[1024];
					               int len = -1;
					               while ((len = inputStream.read(buffer)) != -1) {
					                outputStream.write(buffer, 0, len);
					               }
					               inputStream.close();
					               outputStream.flush();
					               outputStream.close();
					               
						} catch (Exception e) {
							  
							e.printStackTrace();
						}
						
					}
				}
				
			}
		}

		
	
}
}
