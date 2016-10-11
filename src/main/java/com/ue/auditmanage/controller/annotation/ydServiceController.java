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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.util;
import vo.Message;
import vo.Service;
import vo.sType;
import vo.vUser;
import com.ue.auditmanage.controller.service.ServiceTypeInter;
import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDServiceInter;
import com.ue.auditmanage.controller.service.YDUserInter;
import entity.ServiceType;
import entity.YDService;
import entity.YDUser;
/**
 * 引入Controller注解
 * 
 * @author riverplant
 * 
 */
// 类的注解
@Controller
@RequestMapping("/ydService")
/**
 * 配置了@RequestMapping("/user")之后，里面所有的new ModelAndView("/annotation","result",result);
 * 都不需要再写/user了
 * @author riverplant
 *
 */
public class ydServiceController {
	@Resource(name = "ydServiceImpl")
	private YDServiceInter ydServiceInter;
	@Resource(name = "ServiceTypeImpl")
	private ServiceTypeInter serviceTypeInter;

	@Resource(name = "UserTypeImpl")
	private UserTypeServiceInter userTypeServiceInter;
	@Resource(name = "ydUserImpl")
	private YDUserInter ydUserInter;
	
	
	/**
	 * 各分中心发布服务统计
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
	@RequestMapping(value = "/getAllserviceByOrg")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllserviceByOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql = "from YDService yd where 1=1 ";
		String hql_total = " select count(*) from YDService  where 1=1 ";
      try {
    	  List<YDService> services = ydServiceInter.findAllService(hql);
  		if(services!=null && services.size()>0){
  			Map<String, Integer> servicemap = util.geServiceByOrg(services);
  			int id = 1;
  			Iterator<?> iter = servicemap.entrySet().iterator();
  			List<vUser> ss  = new ArrayList<vUser>();
  			while (iter.hasNext()) {
  			@SuppressWarnings("unchecked")
			Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
  			vUser user = new vUser();
  			user.setId(id++);
  			user.setUname(entry.getKey());
  			user.setNbrService(entry.getValue());
  			ss.add(user);
  			}
  			long total = ydServiceInter.getTotal(hql_total, null);
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
	 * 服务被访问Top10
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getTop3Service")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getTop10Service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		PrintWriter writer = response.getWriter();
		
		String hql = "from YDService u order by u.visitedTime desc ";
		try {
			response.setContentType("text/json;charset=utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDService> services = ydServiceInter.findAllService(hql);
		List<Service> ss  = new ArrayList<Service>();
		if (services!=null && services.size()>0) {
			int temp =services.size();
			if(services.size()>3){
				temp = 3;
			}
			for(int i=0;i<temp;i++){		
				Service ser = new Service();
				ser.setsId(services.get(i).getSid());
				ser.setVisiteTotalTime(services.get(i).getVisitedTime());
				ss.add(ser);
			}
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", 3);
		map.put("rows",ss );
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
	 * 服务实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllService4chart")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllService4chart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;PrintWriter writer = response.getWriter();
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date updateDateStart;
		Date registerDateStart;
		Date updateDateEnd;
		String hql = "from YDService yd where 1=1 ";
		String hql_total = " select count(*) from YDService  where 1=1 ";
		List<Date> paras = new ArrayList<Date>();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("surl") != null
					&& !request.getParameter("surl").trim().equals("")) {
				hql = hql + "" + " and yd.surl like '%"
						+ request.getParameter("surl").trim() + "%' ";
				hql_total = hql_total + "" + " and surl like '%"
						+ request.getParameter("surl").trim() + "%' ";
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
		List<YDService> services = ydServiceInter.findAllService(hql);
		List<Service> ss  = new ArrayList<Service>();
		if (services!=null && services.size()>0) {
			
			for(YDService s : services){
				
				Service ser = new Service();
				ser.setId(s.getId());
				ser.setsId(s.getSid());
				ser.setVisiteTotalTime(s.getVisitedTime());
				ss.add(ser);
			}
			
		long total = ydServiceInter.getTotal(hql_total, null);
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", total);
		map.put("rows",ss );
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
	 * 服务实时访问
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getAllService")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllService(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date updateDateStart;
		Date registerDateStart;
		Date updateDateEnd;
		 int rows = 10;int page = 1;PrintWriter writer = response.getWriter();
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		String hql = "from YDService yd where 1=1 ";
		String hql_total = " select count(*) from YDService  where 1=1 ";
		List<Date> paras = new ArrayList<Date>();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("surl") != null
					&& !request.getParameter("surl").trim().equals("")) {
				hql = hql + "" + " and yd.surl like '%"
						+ request.getParameter("surl").trim() + "%' ";
				hql_total = hql_total + "" + " and surl like '%"
						+ request.getParameter("surl").trim() + "%' ";
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
		List<YDService> services = ydServiceInter.findServiceByFenye(hql, paras, page, rows);
		List<Service> ss  = new ArrayList<Service>();
		if (services!=null && services.size()>0) {
			
			for(YDService s : services){
				
				Service ser = new Service();
				ser.setId(s.getId());
				ser.setSname(s.getsName());
				ser.setRegisterDate(sdf_fmg.format(s.getRegisterDate()));
				ser.setsId(s.getSid());
				ser.setUpdateDate(sdf_fmg.format(s.getUpdateDate()));
				ser.setVisiteTotalTime(s.getVisitedTime());
				if(s.getProvider().getUname()!=null && !"".equalsIgnoreCase(s.getProvider().getUname())){
					ser.setProvider(s.getProvider().getUname());
				}else{
					ser.setProvider(s.getProvider().getUserIp());
				}
				
				ser.setSurl(s.getSurl());
				ser.setSearchedTime(s.getSearchedTime());
				ser.setServiceType(s.getServiceType().getName());
				ss.add(ser);
			}
			
		long total = ydServiceInter.getTotal(hql_total, null);
		flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("total", total);
		map.put("rows",ss );
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
	 * 服务获得服务类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/getAllServiceType")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void getAllServiceType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();

		int rows = 10;
		int page = 1;
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("rows")) > 0)
			rows = Integer.parseInt(request.getParameter("rows"));
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("page")) > 0)
			page = Integer.parseInt(request.getParameter("page"));

		boolean flag = false;
		String hql = " from  ServiceType st where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from ServiceType where 1=1 ";
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date updateDateStart;
		Date registerDateStart;
		Date updateDateEnd;

		util util = new util();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("name") != null
					&& !request.getParameter("name").trim().equals("")) {
				hql = hql + "" + " and st.name like '%"
						+ request.getParameter("name").trim() + "%' ";
				hql_total = hql_total + "" + " and name like '%"
						+ request.getParameter("name").trim() + "%' ";
			}
			if (request.getParameter("registerDateStart") != null
					&& !request.getParameter("registerDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and st.registerDate > ? ";
				hql_total = hql_total + "" + " and registerDate > ? ";
				String registerDateStart_temp = request
						.getParameter("registerDateStart");
				registerDateStart = sdf_fmg.parse(registerDateStart_temp);

				paras.add(registerDateStart);
			}
			if (request.getParameter("registerDateEnd") != null
					&& !request.getParameter("registerDateEnd").trim()
							.equals("")) {
				hql = hql + " and st.registerDate < ? ";
				hql_total = hql_total + " and registerDate < ? ";
				registerDateEnd = sdf_fmg.parse(request
						.getParameter("registerDateEnd"));
				paras.add(registerDateEnd);
			}
			if (request.getParameter("updateDateStart") != null
					&& !request.getParameter("updateDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and st.updateDate > ? ";
				hql_total = hql_total + "" + " and updateDate > ? ";
				updateDateStart = sdf_fmg.parse(request
						.getParameter("updateDateStart"));
				paras.add(updateDateStart);
			}
			if (request.getParameter("updateDateEnd") != null
					&& !request.getParameter("updateDateEnd").trim().equals("")) {
				hql = hql + "" + " and st.updateDate < ? ";
				hql_total = hql_total + "" + " and updateDate < ? ";

				updateDateEnd = sdf_fmg.parse(request
						.getParameter("updateDateEnd"));
				paras.add(updateDateEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<ServiceType> serviceTypes = serviceTypeInter.findByFenyeServiceType(hql, paras, page, rows);

		Long total = serviceTypeInter.getServiceTypeTotal(hql_total, paras);
		
		if (serviceTypes != null && serviceTypes.size() > 0) {
			
			List<sType> sTypes = new ArrayList<sType>();
			for (ServiceType st : serviceTypes) {
				sType so = new sType();
				so.setId(st.getId());
				so.setName(st.getName());
				so.setRegisterDate(sdf_fmg.format(st.getRegisterDate()));
				so.setUpdateDate(sdf_fmg.format(st.getUpdateDate()));
				sTypes.add(so);
			}
			flag = true;
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", total);
			map.put("rows", sTypes);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			List<sType> sTypes = new ArrayList<sType>();
			sType so = new sType();
			sTypes.add(so);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", sTypes);
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object, jsonConfig);
			util.writeJson(object, request, response);

		}
	
	}
	
	
	/**
	 * 得到服务类型列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getServiceTypelist")
	public void getServiceTypelist(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String hql = "from ServiceType  where 1=1";
			List<ServiceType> serviceTypes = serviceTypeInter.findAllServiceType(hql);
			boolean flag = false;
			String json = "[";

			for (int i = 0; i < serviceTypes.size(); i++) {
				
				if (i == 0) {
					ServiceType ut = serviceTypes.get(i);
					flag = true;
					if(serviceTypes.size()==1){
      					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
      							+ "\"text\":" + "\"" + ut.getName() + "\""
      							+ "," + "\"selected\":" + flag + "}";
          		  }else{
          			json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
							+ "\"text\":" + "\"" + ut.getName() + "\""
							+ "," + "\"selected\":" + flag + "},";  
          		  }
				} else if (i == serviceTypes.size() - 1) {
					ServiceType ut = serviceTypes.get(i);
					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
							+ "\"text\":" + "\"" + ut.getName()
							+ "\"}";

				} else {
					ServiceType ut = serviceTypes.get(i);
					json += "{\"id\":" + "\"" +  ut.getId() + "\"" + ","
							+ "\"text\":" + "\"" +ut.getName()
							+ "\"},";
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
	
	// 添加服务类型
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addServiceType")
	@ResponseBody
	public void addServiceType(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String name = request.getParameter("name");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (serviceTypeInter.findByName(name) != null) {
			Message ms = new Message();
			ms.setText("该服务类型已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			ServiceType userType = new ServiceType();
			try {
				userType.setName(name);
				userType.setRegisterDate(new java.util.Date());
				userType.setUpdateDate(new java.util.Date());

				serviceTypeInter.saveServiceType(userType);
				flag = true;

				sType uType = new sType();
				uType.setId(userType.getId());
				uType.setName(userType.getName());
				uType.setRegisterDate(sdf.format(userType.getRegisterDate()));
				uType.setUpdateDate(sdf.format(userType.getRegisterDate()));
				JSONObject object = JSONObject.fromObject(uType, jsonConfig);
				System.out.println("添加用户类型成功");
				util.writeJson(object, flag, request, response);
			} catch (Exception e) {
				try {
					writer = response.getWriter();
					writer.write("{\"success\":false,\"result\":添加服务类型失败}");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}

	}
	
	// 添加服务
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addYdServices")
	@ResponseBody
	public void addYdServices(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		String sid = request.getParameter("sid");
		String stId = request.getParameter("stId");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		
		if (ydServiceInter.findServiceBySid(sid) != null ) {
			Message ms = new Message();
			ms.setText("该服务id已存在,或服务地址已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			YDService ydService = new YDService();
			try {
				ydService.setSid(sid);
				ydService.setsName(request.getParameter("sname"));
				ydService.setSurl(request.getParameter("surl"));
				ydService.setRegisterDate(new java.util.Date());
				ydService.setUpdateDate(new java.util.Date());
				ydService.setVisitedTime(0);
				ydService.setSearchedTime(0);
				ydService.setProvider(ydUserInter.getObject(YDUser.class, Integer.parseInt(request.getParameter("providerId"))));
				ydService.setServiceType(serviceTypeInter.getServiceTypeById(ServiceType.class, Integer.parseInt(stId)));
				ydServiceInter.save(ydService);
				flag = true;

				Service service = new Service();
				service.setsId(ydService.getSid());
				
				JSONObject object = JSONObject.fromObject(service, jsonConfig);
				
				util.writeJson(object, flag, request, response);
			} catch (Exception e) {
				try {
					writer = response.getWriter();
					writer.write("{\"success\":false,\"result\":添加服务失败}");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}

	}
	
	/**
	 * 删除服务类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delServiceType")
	public void delServiceType(HttpServletRequest request,
			HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		String hql_total = "select count(*) from YDService ys where 1=1  and ys.serviceType.id = ? ";
		ArrayList<ServiceType> serviceTypes = new ArrayList<ServiceType>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				List<Object> param = new ArrayList<Object>();
				param.add(Integer.parseInt(id));	
			Long count = ydServiceInter.getTotal(hql_total, param);
			if(count==0){
				ServiceType bmodule = serviceTypeInter.getServiceTypeById(ServiceType.class, Integer.parseInt(id));
				if (serviceTypeInter.deletById(Integer.parseInt(id)))
					flag = true;
				serviceTypes.add(bmodule);
			}}			
		}
		util.writeJson(JSONArray.fromObject(serviceTypes), flag, request,
				response);
	}
	
	/**
	 * 删除服务
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delServices")
	public void delServices(HttpServletRequest request,
			HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<Service> ydservices = new ArrayList<Service>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				YDService ydservice = ydServiceInter.getObject(YDService.class,Integer.parseInt(id));
				if (ydServiceInter.deletBySid(Integer.parseInt(id))){
					Service s = new Service();
					s.setId(ydservice.getId());
					s.setsId(ydservice.getSid());
					flag = true;ydservices.add(s);
				}
			}
		}
		util.writeJson(JSONArray.fromObject(ydservices), flag, request,
				response);
	}
	
	
	/**
	 * 跟新服务
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateServices")
	public void updateServices(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
        String stId =  request.getParameter("stId");
		boolean flag = false;
		util util = new util();
		String sId = request.getParameter("sId");
		String sid = request.getParameter("sid");
	
			try {
				YDService ydService = ydServiceInter.findServiceBySid(sId);
				ydService.setSid(sid);
				ydService.setsName(request.getParameter("sname"));
				ydService.setSurl(request.getParameter("surl"));
				ydService.setProvider(ydUserInter.getObject(YDUser.class, Integer.parseInt(request.getParameter("providerId"))));
			ydService.setServiceType(serviceTypeInter.getServiceTypeById(ServiceType.class, Integer.parseInt(stId)));
				ydService.setUpdateDate(new java.util.Date());
				ydServiceInter.update(ydService);
				flag = true;
				Service mo = new Service();
				mo.setsId(ydService.getSid());
				JSONObject object = JSONObject.fromObject(mo, jsonConfig);
				util.writeJson(object, flag, request, response);
			
			} catch (Exception e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				util.writeJson("修改失败", flag, request, response);
			}
	
		
	}
	/**
	 * 跟新服务类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateServiceType")
	public void updateServiceType(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();

		String id = request.getParameter("id");

		try {

			ServiceType serviceType = serviceTypeInter.getServiceTypeById(ServiceType.class,Integer.parseInt(id));
			serviceType.setName(request.getParameter("name"));
			serviceType.setUpdateDate(new java.util.Date());

			serviceTypeInter.updateServiceType(serviceType);
			flag = true;

			sType mo = new sType();
			mo.setName(serviceType.getName());
			mo.setRegisterDate(sdf.format(serviceType.getRegisterDate()));
			mo.setUpdateDate(sdf.format(serviceType.getUpdateDate()));
			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
			util.writeJson(object, flag, request, response);
		
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}
	}
	
	
}
