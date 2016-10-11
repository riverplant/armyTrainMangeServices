package com.ue.auditmanage.controller.annotation;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.JsonDateValueProcessor;
import util.util;
import vo.Message;
import vo.vData;
import vo.vDataType;
import vo.vUser;
import com.ue.auditmanage.controller.service.DataTypeInter;
import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDDataInter;
import com.ue.auditmanage.controller.service.YDUserInter;
import entity.DataType;
import entity.YDData;
import entity.YDUser;

//类的注解
@Controller
@RequestMapping("/yddata")
public class ydDataController {
	@Resource(name = "dataTypeImpl")
	private DataTypeInter dataTypeInter;
	@Resource(name = "ydDataImpl")
	private YDDataInter ydDataInter;
	@Resource(name = "ydUserImpl")
	private YDUserInter ydUserInter;
	@Resource(name = "UserTypeImpl")
	private UserTypeServiceInter userTypeServiceInter;
	
	
	/**
	 * 各分中心发布数据统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAlldataByOrg")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAlldataByOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql = "from YDData yd where 1=1 ";
		String hql_total = " select count(*) from YDData  where 1=1 ";
      try {
    	  List<YDData> datas = ydDataInter.findAllData(hql);
  		if(datas!=null && datas.size()>0){
  			Map<String, Integer> atlasmap = util.geDataByOrg(datas);
  			int id = 1;
  			Iterator<?> iter = atlasmap.entrySet().iterator();
  			List<vUser> ss  = new ArrayList<vUser>();
  			while (iter.hasNext()) {
  			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			vUser user = new vUser();
  			user.setId(id++);
  			user.setUname(entry.getKey());
  			user.setNbrData(entry.getValue());
  			ss.add(user);
  			}
  			long total = ydDataInter.getTotal(hql_total, null);
  			flag = true;
  			Map<String, Object> map = new HashMap<String, Object>();
  			map.put("success", flag);
  			map.put("total", total);
  			map.put("rows",ss );
  			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
  			util.writeJson(objects, request, response);
  			
  		}else {
  			flag = true;List<vUser> ss  = new ArrayList<vUser>();
  			Map<String, Object> map = new HashMap<String, Object>();
  			map.put("success", flag);
  			map.put("total", 0);
  			map.put("rows",ss );
  			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
  			util.writeJson(objects, request, response);

  		}
	} catch (Exception e) {
		e.printStackTrace();
		flag = true;List<vUser> ss  = new ArrayList<vUser>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", 0);
			map.put("rows",ss );
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
	}	
	}

	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getTop3Data")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getTop10Data(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		boolean flag = false;
	   PrintWriter writer = response.getWriter();
		String hql = "from YDData yd order by yd.downloadTimes desc ";
		try {
			response.setContentType("text/json;charset=utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDData> ydDatas = ydDataInter.findAllData(hql);
		List<vData> datas  = new ArrayList<vData>();
		if (ydDatas!=null && ydDatas.size()>0) {
			int temp =ydDatas.size();
			if(ydDatas.size()>3){
				temp = 3;
			}
			for(int i=0;i<temp;i++){
				
				vData dd = new vData();
				dd.setdId(ydDatas.get(i).getDid());
				dd.setDownloadTotalTime(ydDatas.get(i).getDownloadTimes());
				datas.add(dd);
				
			}
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", 3);
		map.put("rows",datas );
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
	 * 查询所有数据4chart
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllData4chart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllData4chart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		PrintWriter writer = response.getWriter();
		String hql = "from YDData page where 1=1 ";
		try {
			response.setContentType("text/json;charset=utf-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDData> YDPages = ydDataInter.findAllData(hql);
		List<vData> pages  = new ArrayList<vData>();
		if (YDPages!=null && YDPages.size()>0) {
			
			for(YDData s : YDPages){
				
				vData ser = new vData();
				ser.setId(s.getId());
				ser.setdId(s.getDid());
				ser.setDownloadTotalTime(s.getDownloadTimes());
				pages.add(ser);
			}
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", YDPages.size());
		map.put("rows",pages );
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
	 * 查询数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllData")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		boolean flag = false;
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date updateDateStart;
		Date registerDateStart;
		Date updateDateEnd;
		 int rows = 10;int page = 1;PrintWriter writer = response.getWriter();
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		String hql = "from YDData yd where 1=1 ";
		String hql_total = " select count(*) from YDData  where 1=1 ";
		List<Date> paras = new ArrayList<Date>();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("did") != null
					&& !request.getParameter("did").trim().equals("")) {
				hql = hql + "" + " and yd.did like '%"
						+ request.getParameter("did").trim() + "%' ";
				hql_total = hql_total + "" + " and did like '%"
						+ request.getParameter("did").trim() + "%' ";
			}
			if (request.getParameter("registerDateStart") != null
					&& !request.getParameter("registerDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and yd.registerDate > ? ";
				hql_total = hql_total + "" + " and registerDate > ? ";
				String registerDateStart_temp = request
						.getParameter("registerDateStart");
				registerDateStart = sdf_fmg.parse(registerDateStart_temp);

				paras.add(registerDateStart);
			}
			if (request.getParameter("registerDateEnd") != null
					&& !request.getParameter("registerDateEnd").trim()
							.equals("")) {
				hql = hql + " and yd.registerDate < ? ";
				hql_total = hql_total + " and registerDate < ? ";
				registerDateEnd = sdf_fmg.parse(request
						.getParameter("registerDateEnd"));
				paras.add(registerDateEnd);
			}
			if (request.getParameter("updateDateStart") != null
					&& !request.getParameter("updateDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and yd.updateDate > ? ";
				hql_total = hql_total + "" + " and updateDate > ? ";
				updateDateStart = sdf_fmg.parse(request
						.getParameter("updateDateStart"));
				paras.add(updateDateStart);
			}
			if (request.getParameter("updateDateEnd") != null
					&& !request.getParameter("updateDateEnd").trim().equals("")) {
				hql = hql + "" + " and yd.updateDate < ? ";
				hql_total = hql_total + "" + " and updateDate < ? ";

				updateDateEnd = sdf_fmg.parse(request
						.getParameter("updateDateEnd"));
				paras.add(updateDateEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDData> ydDatas = ydDataInter.findDataByFenye(hql, paras, page, rows);
		List<vData> datas  = new ArrayList<vData>();
		if (ydDatas!=null && ydDatas.size()>0) {
			
			for(YDData s : ydDatas){
				
				vData dd = new vData();
				dd.setId(s.getId());
				dd.setRegisterDate(sdf_fmg.format(s.getRegisterDate()));
				dd.setdId(s.getDid());
				dd.setUpdateDate(sdf_fmg.format(s.getUpdateDate()));
				dd.setDownloadTotalTime(s.getDownloadTimes());
				if(s.getDataType()!=null)   dd.setTypeName(s.getDataType().getName());
				if(s.getProvider()!=null)dd.setProvider(s.getProvider().getUname());
				dd.setSearchedTime(s.getSearchedTime());
				dd.setVisitedTime(s.getVisitedTimes());
				dd.setUrl(s.getdUrl());
	
				datas.add(dd);
			}
			
		long total = ydDataInter.getTotal(hql_total, null);
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", total);
		map.put("rows",datas );
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
	 * 查询数据类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllDataType")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllDataType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		boolean flag = false;
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date updateDateStart;
		Date registerDateStart;
		Date updateDateEnd;
		 int rows = 10;int page = 1;PrintWriter writer = response.getWriter();
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		String hql = "from DataType dt where 1=1 ";
		String hql_total = " select count(*) from DataType  where 1=1 ";
		List<Date> paras = new ArrayList<Date>();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("name") != null
					&& !request.getParameter("name").trim().equals("")) {
				hql = hql + "" + " and dt.name like '%"
						+ request.getParameter("name").trim() + "%' ";
				hql_total = hql_total + "" + " and name like '%"
						+ request.getParameter("name").trim() + "%' ";
			}
			if (request.getParameter("registerDateStart") != null
					&& !request.getParameter("registerDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and dt.registerDate > ? ";
				hql_total = hql_total + "" + " and registerDate > ? ";
				String registerDateStart_temp = request
						.getParameter("registerDateStart");
				registerDateStart = sdf_fmg.parse(registerDateStart_temp);

				paras.add(registerDateStart);
			}
			if (request.getParameter("registerDateEnd") != null
					&& !request.getParameter("registerDateEnd").trim()
							.equals("")) {
				hql = hql + " and dt.registerDate < ? ";
				hql_total = hql_total + " and registerDate < ? ";
				registerDateEnd = sdf_fmg.parse(request
						.getParameter("registerDateEnd"));
				paras.add(registerDateEnd);
			}
			if (request.getParameter("updateDateStart") != null
					&& !request.getParameter("updateDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and dt.updateDate > ? ";
				hql_total = hql_total + "" + " and updateDate > ? ";
				updateDateStart = sdf_fmg.parse(request
						.getParameter("updateDateStart"));
				paras.add(updateDateStart);
			}
			if (request.getParameter("updateDateEnd") != null
					&& !request.getParameter("updateDateEnd").trim().equals("")) {
				hql = hql + "" + " and dt.updateDate < ? ";
				hql_total = hql_total + "" + " and updateDate < ? ";

				updateDateEnd = sdf_fmg.parse(request
						.getParameter("updateDateEnd"));
				paras.add(updateDateEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<DataType> dataTypes = dataTypeInter.findDataByFenye(hql, paras, page, rows);
		List<vDataType> vdataTypes  = new ArrayList<vDataType>();
		if (dataTypes!=null && dataTypes.size()>0) {
			
			for(DataType s : dataTypes){
				
				vDataType dd = new vDataType();
				dd.setId(s.getId());
				dd.setRegisterDate(sdf_fmg.format(s.getRegisterDate()));
				dd.setName(s.getName());
				dd.setUpdateDate(sdf_fmg.format(s.getUpdateDate()));
				
				
				vdataTypes.add(dd);
			}
			
		long total = dataTypeInter.getTotal(hql_total, null);
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", total);
		map.put("rows",vdataTypes );
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
	 * 得到数据类型列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getDataTypelist")
	public void getDataTypelist(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String hql = "from DataType  where 1=1";
			List<DataType> dataTypes = dataTypeInter.findAllData(hql);
			boolean flag = false;
			String json = "[";
          if(dataTypes!=null &&dataTypes.size()>0 ){
        	  for (int i = 0; i < dataTypes.size(); i++) {
        		  if (i == 0) {
  					DataType ut = dataTypes.get(i);
  					flag = true;
  					if(dataTypes.size()==1){
      					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
      							+ "\"text\":" + "\"" + ut.getName() + "\""
      							+ "," + "\"selected\":" + flag + "}";
          		  } else{
          			json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
  							+ "\"text\":" + "\"" + ut.getName() + "\""
  							+ "," + "\"selected\":" + flag + "},"; 
          		  }
  				} else if (i!=0 && i == dataTypes.size() - 1) {
  					DataType ut = dataTypes.get(i);
  					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
  							+ "\"text\":" + "\"" + ut.getName()
  							+ "\"}";

  				} else {
  					DataType ut = dataTypes.get(i);
  					json += "{\"id\":" + "\"" +  ut.getId() + "\"" + ","
  							+ "\"text\":" + "\"" +ut.getName()
  							+ "\"},";
  				}

  			}
          }
			
			json += "]";
			writer.write(json);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		

	// 添加数据类型
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addDataType")
	@ResponseBody
	public void addDataType(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String name = request.getParameter("name");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (dataTypeInter.findServiceByName(name) != null) {
			Message ms = new Message();
			ms.setText("该数据类型已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			DataType dataType = new DataType();
			try {
				dataType.setName(name);
				dataType.setRegisterDate(new java.util.Date());
				dataType.setUpdateDate(new java.util.Date());

				dataTypeInter.save(dataType);
				flag = true;

				vDataType uType = new vDataType();
				uType.setId(dataType.getId());
				uType.setName(dataType.getName());
				uType.setRegisterDate(sdf.format(dataType.getRegisterDate()));
				uType.setUpdateDate(sdf.format(dataType.getRegisterDate()));
				JSONObject object = JSONObject.fromObject(uType, jsonConfig);
				System.out.println("添加数据类型成功");
				util.writeJson(object, flag, request, response);
			} catch (Exception e) {
				try {
					writer = response.getWriter();
					writer.write("{\"success\":false,\"result\":添加数据类型失败}");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}

	}
	
	// 添加数据
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addYdData")
	@ResponseBody
	public void addYdData(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		String did = request.getParameter("did");
		String dtId = request.getParameter("DT_ID");
		String Provider_ID = request.getParameter("Provider_ID");
		String dUrl = request.getParameter("dUrl");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		
		if (ydDataInter.findServiceByDid(did) != null || ydDataInter.findServiceBydUrl(dUrl)!=null ) {
			Message ms = new Message();
			ms.setText("该数据标识已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			YDData ydData = new YDData();
			try {
				ydData.setDid(did);
				ydData.setdUrl(dUrl);
				ydData.setDownloadTimes(0);
				ydData.setVisitedTimes(0);
				ydData.setSearchedTime(0);
				ydData.setRegisterDate(new java.util.Date());
				ydData.setUpdateDate(new java.util.Date());
				ydData.setPrice(Integer.parseInt(request.getParameter("price")));
				ydData.setProvider(ydUserInter.getObject(YDUser.class, Integer.parseInt(Provider_ID)));
				ydData.setDataType(dataTypeInter.getObject(DataType.class, Integer.parseInt(dtId)));
				ydDataInter.save(ydData);
				flag = true;

				vData vdata = new vData();
				vdata.setdId(ydData.getDid());
				vdata.setId(ydData.getId());
				JSONObject object = JSONObject.fromObject(vdata, jsonConfig);
				
				util.writeJson(object, flag, request, response);
			} catch (Exception e) {
				try {
					writer = response.getWriter();
					writer.write("{\"success\":false,\"result\":添加数据失败}");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}

	}

	
	
	/**
	 * 删除数据类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delDataType")
	public void delDataType(HttpServletRequest request,
			HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<vDataType> dataTypes = new ArrayList<vDataType>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				DataType dataType = dataTypeInter.getObject(DataType.class, Integer.parseInt(id));
				try {
					if (dataTypeInter.deletById(Integer.parseInt(id)))
						flag = true;
					vDataType vdataType = new vDataType();
					vdataType.setId(dataType.getId());
					vdataType.setName(dataType.getName());
					dataTypes.add(vdataType);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		util.writeJson(JSONArray.fromObject(dataTypes), flag, request,
				response);
	}
	
	/**
	 * 删除数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delData")
	public void delData(HttpServletRequest request,
			HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<vData> vDatas = new ArrayList<vData>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				YDData ydData = ydDataInter.getObject(YDData.class, Integer.parseInt(id));
				if (ydDataInter.deletById(Integer.parseInt(id))){
					flag = true;
					vData data = new vData();
					data.setdId(ydData.getDid());
					data.setdId(ydData.getDid());
					vDatas.add(data);
				}
			}
		}
		util.writeJson(JSONArray.fromObject(vDatas), flag, request,
				response);
	}
		
	
	/**
	 * 跟新数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateData")
	public void updateData(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
        String dUrl =  request.getParameter("dUrl");
        String Provider_ID = request.getParameter("Provider_ID");
		boolean flag = false;
		util util = new util();
		String did = request.getParameter("did");
		String ddid = request.getParameter("ddid");
		String DT_ID = request.getParameter("DT_ID");
		   YDUser provider =ydUserInter.getObject(YDUser.class, Integer.parseInt(Provider_ID));
	
		try {
			YDData ydData = ydDataInter.findServiceByDid(ddid);
			ydData.setDid(did);
			ydData.setdUrl(dUrl);
			ydData.setDataType(dataTypeInter.getObject(DataType.class, Integer.parseInt(DT_ID)));
			ydData.setUpdateDate(new java.util.Date());
			ydData.setPrice(Integer.parseInt(request.getParameter("price")));
			ydData.setProvider(provider);
			ydDataInter.update(ydData);
			flag = true;
			vData mo = new vData();
			mo.setdId(ydData.getDid());
			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
			util.writeJson(object, flag, request, response);
		
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}
	/**
	 * 跟新数据类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateDataType")
	public void updateDataType(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();

		String id = request.getParameter("id");

		try {

			DataType dataType = dataTypeInter.getObject(DataType.class, Integer.parseInt(id));
			dataType.setName(request.getParameter("name"));
			dataType.setUpdateDate(new java.util.Date());

			dataTypeInter.update(dataType);
			flag = true;

			vDataType mo = new vDataType();
			mo.setName(dataType.getName());
			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
			util.writeJson(object, flag, request, response);
		
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}

	
}
