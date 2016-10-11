package com.ue.auditmanage.controller.annotation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import util.md5Encoding;
import util.util;
import vo.Message;
import vo.vUser;
import vo.auditAdmin;
import vo.uType;
import vo.vuserType;

import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDUserInter;

import entity.UserType;
import entity.YDUser;
/**
 * 引入Controller注解
 * 
 * @author riverplant
 * 
 */
// 类的注解
@Controller
@RequestMapping("/ydVisiter")
/**
 * 配置了@RequestMapping("/user")之后，里面所有的new ModelAndView("/annotation","result",result);
 * 都不需要再写/user了
 * @author riverplant
 *
 */
public class ydUserController {
	@Resource(name = "ydUserImpl")
	private YDUserInter ydVisiterInter;
	@Resource(name = "UserTypeImpl")
	private UserTypeServiceInter userTypeServiceInter;
	/**
	 * 查询Top10用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/getTop3Users")
	public void getTop10Users(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql = " from  YDUser u order by u.visitServiceTimes desc ";
		util util = new util();
		try {
			response.setContentType("text/json;charset=utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDUser> userTypes = ydVisiterInter.findAll(hql);
		if (userTypes != null && userTypes.size() > 0) {
			List<vUser> vUers = new ArrayList<vUser>();
			int temp =userTypes.size();
			if(userTypes.size()>3){
				temp = 3;
			}
			for(int i=0;i<temp;i++){
				vUser u = new vUser();
				u.setUid(userTypes.get(i).getUid());
				u.setDownloadAtlasTime(userTypes.get(i).getDownloadAtlasTime());
				
				
				u.setVisitPageTime(userTypes.get(i).getVisitPageTime());
				vUers.add(u);
			}
			flag = true;
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", 3);
			map.put("rows", vUers);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			List<uType> modules = new ArrayList<uType>();
			uType u = new uType();
			modules.add(u);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", modules);
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object, jsonConfig);
			util.writeJson(object, request, response);

		}
	}

	/**
	 * 验证用户登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/login")
	@ResponseBody
	// 将json对象直接转换成字符串
	public void login(HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonConfig jsonConfig = new JsonConfig();
		util util = new util();
		boolean flag = false;
		String userName = request.getParameter("name").trim();
		String password = request.getParameter("passwprd").trim();

		password = md5Encoding.getMd5Encrypter(password);
		String requestIp = util.getIpAddr(request);
		String d = sdf.format(new Date());
		if (ydVisiterInter.login(userName, password, requestIp, d)) {
			flag = true;
			auditAdmin admin = new auditAdmin();
			admin.setUsername(userName);
			admin.setPasswd(password);
			admin.setLogin_ip(requestIp);
			admin.setLogin_time(d);
			try {
				jsonConfig = util.getjsonConfig();
				JSONObject object = JSONObject.fromObject(admin, jsonConfig);
				util.writeJson(object, flag, request, response);

			} catch (Exception e) {
				e.printStackTrace();
				util.writeJson(null, flag, request, response);
			}

		} else {
			util.writeJson(null, flag, request, response);

		}

	}

	// 添加用户类型
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addUserType")
	@ResponseBody
	public void addUserType(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String name = request.getParameter("name");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (userTypeServiceInter.findByName(name) != null) {
			Message ms = new Message();
			ms.setText("该模块名称已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			UserType userType = new UserType();
			try {
				userType.setName(name);
				userType.setRegisterDate(new java.util.Date());
				userType.setUpdateDate(new java.util.Date());

				userTypeServiceInter.saveUserType(userType);
				flag = true;

				uType uType = new uType();
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
					writer.write("{\"success\":false,\"result\":添加用户类型失败}");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}

	}
	/**
	 * 得到用户类型列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUserTypelist")
	public void getUserTypelist(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String hql = "from UserType  where 1=1";
			List<UserType> UserTypes = userTypeServiceInter.findAllUserType(hql);
			boolean flag = false;
			String json = "[";
			 if(UserTypes!=null &&UserTypes.size()>0 ){
				 
				 for (int i = 0; i < UserTypes.size(); i++) {
						if (i == 0) {
							UserType ut = UserTypes.get(i);
							flag = true;
							if(UserTypes.size()==1){
		      					json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
		      							+ "\"text\":" + "\"" + ut.getName() + "\""
		      							+ "," + "\"selected\":" + flag + "}";
		          		  }else{
		          			json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
									+ "\"text\":" + "\"" + ut.getName() + "\""
									+ "," + "\"selected\":" + flag + "},";  
		          		  }
						} else if (i == UserTypes.size() - 1) {
							UserType ut = UserTypes.get(i);
							json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
									+ "\"text\":" + "\"" + ut.getName()
									+ "\"}";

						} else {
							UserType ut = UserTypes.get(i);
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
	
	/**
	 * 得到供应商列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getProviderlist")
	public void getProviderlist(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String hql = "from YDUser yd where 1=1 and yd.userType.name=?";
			String[]paras ={"供应商"};
			List<YDUser> providers = ydVisiterInter.find(hql, paras);
			boolean flag = false;
			String json = "[";
if(providers!=null && providers.size()>0){
	for (int i = 0; i < providers.size(); i++) {
		if (i == 0) {
			YDUser ut = providers.get(i);
			flag = true;
			if(providers.size()==1){
				json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
						+ "\"text\":" + "\"" + ut.getUname() + "\""
						+ "," + "\"selected\":" + flag + "}";
			}else{
				json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
						+ "\"text\":" + "\"" + ut.getUname() + "\""
						+ "," + "\"selected\":" + flag + "},";	
			}
		} else if (i == providers.size() - 1) {
			YDUser ut = providers.get(i);
			json += "{\"id\":" + "\"" + ut.getId() + "\"" + ","
					+ "\"text\":" + "\"" + ut.getUname()
					+ "\"}";

		} else {
			YDUser ut = providers.get(i);
			json += "{\"id\":" + "\"" +  ut.getId() + "\"" + ","
					+ "\"text\":" + "\"" +ut.getUname()
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

	/**
	 * 分页查询所有用户类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/findAllUserTypeByFenye")
	public void findAllUserTypeByFenye(HttpServletRequest request,
			HttpServletResponse response) {
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
		String hql = " from  UserType ut where 1=1 ";
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from UserType where 1=1 ";
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
				hql = hql + "" + " and ut.name like '%"
						+ request.getParameter("name").trim() + "%' ";
				hql_total = hql_total + "" + " and name like '%"
						+ request.getParameter("name").trim() + "%' ";
			}
			if (request.getParameter("registerDateStart") != null
					&& !request.getParameter("registerDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and ut.registerDate > ? ";
				hql_total = hql_total + "" + " and registerDate > ? ";
				String registerDateStart_temp = request
						.getParameter("registerDateStart");
				registerDateStart = sdf_fmg.parse(registerDateStart_temp);

				paras.add(registerDateStart);
			}
			if (request.getParameter("registerDateEnd") != null
					&& !request.getParameter("registerDateEnd").trim()
							.equals("")) {
				hql = hql + " and ut.registerDate < ? ";
				hql_total = hql_total + " and registerDate < ? ";
				registerDateEnd = sdf_fmg.parse(request
						.getParameter("registerDateEnd"));
				paras.add(registerDateEnd);
			}
			if (request.getParameter("updateDateStart") != null
					&& !request.getParameter("updateDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and ut.updateDate > ? ";
				hql_total = hql_total + "" + " and updateDate > ? ";
				updateDateStart = sdf_fmg.parse(request
						.getParameter("updateDateStart"));
				paras.add(updateDateStart);
			}
			if (request.getParameter("updateDateEnd") != null
					&& !request.getParameter("updateDateEnd").trim().equals("")) {
				hql = hql + "" + " and ut.updateDate < ? ";
				hql_total = hql_total + "" + " and updateDate < ? ";

				updateDateEnd = sdf_fmg.parse(request
						.getParameter("updateDateEnd"));
				paras.add(updateDateEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<UserType> userTypes = userTypeServiceInter.findByFenyeUserType(
				hql, paras, page, rows);

		Long total = userTypeServiceInter.getUserTypeTotal(hql_total, paras);
		System.out.println(userTypes.size());
		if (userTypes != null && userTypes.size() > 0) {
			List<uType> uTypes = new ArrayList<uType>();
			for (UserType bm : userTypes) {
				uType mo = new uType();
				mo.setId(bm.getId());
				mo.setName(bm.getName());
				mo.setRegisterDate(sdf_fmg.format(bm.getRegisterDate()));
				mo.setUpdateDate(sdf_fmg.format(bm.getUpdateDate()));
				uTypes.add(mo);
			}
			flag = true;
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", total);
			map.put("rows", uTypes);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {

			List<uType> modules = new ArrayList<uType>();
			uType u = new uType();
			modules.add(u);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", modules);
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object, jsonConfig);
			util.writeJson(object, request, response);

		}
	}

	/**
	 * 删除用户类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delUserType")
	public void delUserType(HttpServletRequest request,
			HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("id").split(",");
		ArrayList<UserType> bmodules = new ArrayList<UserType>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				UserType bmodule = userTypeServiceInter.getUserTypeById(
						UserType.class, Integer.parseInt(id));
				if (userTypeServiceInter.deletById(Integer.parseInt(id)))
					flag = true;
				bmodules.add(bmodule);
			}
		}
		util.writeJson(JSONArray.fromObject(bmodules), flag, request,
				response);
	}
	
	/**
	 * 删除用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delUser")
	public void delUser(HttpServletRequest request, HttpServletResponse response) {

		boolean flag = false;
		String[] ids = request.getParameter("uid").split(",");
		ArrayList<vUser> us =new ArrayList<vUser>();
		if(ids!=null && ids.length>0){
			for(String id : ids){
				YDUser user =  ydVisiterInter.findVisitorByUid(id);
				
					if(ydVisiterInter.deletUserByUId(id)){
						flag = true;
						vUser u = new vUser();
						u.setId(user.getId());
						u.setUid(user.getUid());
						u.setUname(user.getUname());
						us.add(u);
					}
					
				}
		}
		util.writeJson(JSONArray.fromObject(us), flag, request, response);
	}
	/**
	 * 跟新用户类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateUserType")
	public void updateUserType(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonConfig jsonConfig = new JsonConfig();

		boolean flag = false;
		util util = new util();

		String id = request.getParameter("id");

		try {

			UserType bmodule = userTypeServiceInter.getUserTypeById(
					UserType.class, Integer.parseInt(id));
			bmodule.setName(request.getParameter("name"));
			bmodule.setUpdateDate(new java.util.Date());

			userTypeServiceInter.updateUserType(bmodule);
			flag = true;

			uType mo = new uType();
			mo.setName(bmodule.getName());
			mo.setRegisterDate(sdf.format(bmodule.getRegisterDate()));
			mo.setUpdateDate(sdf.format(bmodule.getUpdateDate()));
			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
			util.writeJson(object, flag, request, response);
		
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}

	}
	
	
	/**
	 * 跟新用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/updateUser")
	public void updateUser(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		util util = new util();
		String uuid = request.getParameter("uuid");
		String uid = request.getParameter("uid");
		String uname = request.getParameter("uname");
		String UserIp = request.getParameter("UserIp");
		try {
			YDUser ydVisiter = ydVisiterInter.findVisitorByUid(uuid);
			String utId = request.getParameter("utId");
			UserType userType = userTypeServiceInter.getUserTypeById(UserType.class,Integer.parseInt(utId));
			ydVisiter.setUid(uid);
			ydVisiter.setUserType(userType);
			ydVisiter.setUname(uname);
			ydVisiter.setUserIp(UserIp);
			ydVisiterInter.update(ydVisiter);
			flag = true;
			vUser mo = new vUser();
			mo.setId(ydVisiter.getId());
			mo.setUid(ydVisiter.getUid());mo.setUname(ydVisiter.getUname());mo.setVisiteIp(ydVisiter.getUserIp());
			mo.setTypeName(ydVisiter.getUserType().getName());
			mo.setRegisterDate(sdf.format(ydVisiter.getRegisterDate()));
			if(ydVisiter.getLoginDate()!=null){
				mo.setLoginDate(sdf.format(ydVisiter.getLoginDate()));
			}
			JSONObject object = JSONObject.fromObject(mo, jsonConfig);
			util.writeJson(object, flag, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			util.writeJson("修改失败", flag, request, response);
		}
	}

	
	/**
	 * 查询所有用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/findAllUser4chart")
	public void findAllUser(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag = false;
		String hql = " from  YDUser visiter where 1=1 ";
		util util = new util();
		try {
			response.setContentType("text/json;charset=utf-8");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDUser> ydVisiters = ydVisiterInter.findAll(hql);
			
		
		if (ydVisiters != null && ydVisiters.size() > 0) {
			
			List<vUser> json = new ArrayList<vUser>();
			for (YDUser ydVisiter : ydVisiters) {
				vUser u = new vUser();
				u.setId(ydVisiter.getId());
				u.setTypeName(ydVisiter.getUserType().getName());
				u.setVisiteIp(ydVisiter.getUserIp());
				u.setRegisterDate(sdf_fmg.format(ydVisiter.getRegisterDate()));
				u.setUpdateDate(sdf_fmg.format(ydVisiter.getUpdateDate()));
				u.setUname(ydVisiter.getUname());
				u.setUid(ydVisiter.getUid());
				u.setDownloadAtlasTime(ydVisiter.getDownloadAtlasTime());
                u.setSeartchTimes(ydVisiter.getSearchTime());
				u.setVisitAppTimes(ydVisiter.getVisitAppTimes());
				u.setVisitAtlasTimes(ydVisiter.getVisitAtlasTimes());
				u.setVisitDataTimes(ydVisiter.getVisitDataTimes());
				u.setVisitProductionTimes(ydVisiter.getVisitProductionTimes());
				u.setVisitServiceTimes(ydVisiter.getVisitServiceTimes());
				u.setVisitPageTime(ydVisiter.getVisitPageTime());
				
				json.add(u);
			}
			flag = true;
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", json.size());
			map.put("rows", json);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {
            System.out.println("没有查询到用户");
			List<vUser> visitors = new ArrayList<vUser>();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", visitors);
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object, jsonConfig);
			util.writeJson(object, request, response);

		}
	}
	/**
	 * 查询所有用户类型图标
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/findAllUserType4chart")
	public void findAllUserType4chart(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql = " from  UserType  where 1=1 ";
		util util = new util();
		try {
			response.setContentType("text/json;charset=utf-8");	
			List<UserType> userTypes = userTypeServiceInter.findAllUserType(hql);
			if (userTypes != null && userTypes.size() > 0) {
				
				List<vuserType> vuTypes = new ArrayList<vuserType>();
				int id=1;
				for(UserType utype:userTypes){
					String hql_user = "from YDUser v where 1=1 and v.userType.id = ";
					int user_visiteService=0;
					int user_visiteAtlas=0;
					int user_downloadAtlas=0;
					int user_visiteApp=0;
					int user_visiteData=0;
					int user_visiteProduction=0;
					int user_searchTime=0;
					hql_user = hql_user+""+utype.getId();
					List<YDUser> users = ydVisiterInter.findAll(hql_user);
					if(users!=null && users.size()>0){
						for(YDUser user : users){
							user_visiteService = user_visiteService+user.getVisitServiceTimes();
							user_visiteAtlas = user_visiteAtlas + user.getVisitAtlasTimes();
							user_downloadAtlas = user_downloadAtlas+user.getDownloadAtlasTime();
							user_visiteApp = user_visiteApp + user.getVisitAppTimes();
							user_visiteData = user_visiteData + user.getVisitDataTimes();
							user_visiteProduction = user_visiteProduction+user.getVisitProductionTimes();
							user_searchTime = user_searchTime+user.getSearchTime();
						}	
					}
					vuserType vType = new vuserType();	
					vType.setId(id++);vType.setTypeName(utype.getName());
					vType.setVisitServiceTimes(user_visiteService);
					vType.setVisitAppTimes(user_visiteApp);
					vType.setVisitAtlasTimes(user_visiteAtlas);
					vType.setVisitDataTimes(user_visiteData);
					vType.setVisitProductionTimes(user_visiteProduction);
					vType.setDownloadAtlasTime(user_downloadAtlas);
					vType.setSeartchTimes(user_searchTime);
					vuTypes.add(vType);
				}
				flag = true;			
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", vuTypes.size());
				map.put("rows", vuTypes);
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			} else {
		        flag = true;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", 0);
				map.put("rows", userTypes);
				JSONObject object = JSONObject.fromObject(map);
				object.fromObject(object, jsonConfig);
				util.writeJson(object, request, response);

			}
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", 0);
			map.put("rows", "[]");
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object, jsonConfig);
			util.writeJson(object, request, response);
			e.printStackTrace();
		}
		

	}
	
	/**
	 * 查询所有用户类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/findAllUserType")
	public void findAllUserType(HttpServletRequest request,
			HttpServletResponse response) {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String hql = " from  UserType  where 1=1 ";
		util util = new util();
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		try {
			response.setContentType("text/json;charset=utf-8");	
			List<UserType> userTypes = userTypeServiceInter.findAllUserType(hql);
			if (userTypes != null && userTypes.size() > 0) {
				List<vuserType> vuTypes = new ArrayList<vuserType>();
				int id=1;
				for(UserType utype:userTypes){
					String hql_user = "from YDUser v where 1=1 and v.userType.id = ";
					int user_visiteService=0;
					int user_visiteAtlas=0;
					int user_downloadAtlas=0;
					int user_visiteApp=0;
					int user_visiteData=0;
					int user_visiteProduction=0;
					int user_searchTime=0;
					hql_user = hql_user+""+utype.getId();
					List<YDUser> users = ydVisiterInter.findAll(hql_user);
					if(users!=null && users.size()>0){
						for(YDUser user : users){
							user_visiteService = user_visiteService+user.getVisitServiceTimes();
							user_visiteAtlas = user_visiteAtlas + user.getVisitAtlasTimes();
							user_downloadAtlas = user_downloadAtlas+user.getDownloadAtlasTime();
							user_visiteApp = user_visiteApp + user.getVisitAppTimes();
							user_visiteData = user_visiteData + user.getVisitDataTimes();
							user_visiteProduction = user_visiteProduction+user.getVisitProductionTimes();
							user_searchTime = user_searchTime+user.getSearchTime();
						}	
					}
					vuserType vType = new vuserType();	
					vType.setId(id++);vType.setTypeName(utype.getName());
					vType.setVisitServiceTimes(user_visiteService);
					vType.setVisitAppTimes(user_visiteApp);
					vType.setVisitAtlasTimes(user_visiteAtlas);
					vType.setVisitDataTimes(user_visiteData);
					vType.setVisitProductionTimes(user_visiteProduction);
					vType.setDownloadAtlasTime(user_downloadAtlas);
					vType.setSeartchTimes(user_searchTime);
					vuTypes.add(vType);
				}
				flag = true;			
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", vuTypes.size());
				map.put("rows", util.getFenyeFiles(vuTypes, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			} else {
		        flag = true;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", 0);
				map.put("rows", userTypes);
				JSONObject object = JSONObject.fromObject(map);
				object.fromObject(object, jsonConfig);
				util.writeJson(object, request, response);

			}
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", 0);
			map.put("rows", "[]");
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object, jsonConfig);
			util.writeJson(object, request, response);
			e.printStackTrace();
		}
		

	}
	
	/**
	 * 查询所有细节用户类型
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/findAllUserDetailType")
	public void findAllUserDetailType(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JsonConfig jsonConfig = new JsonConfig();
		boolean flag = false;
		String typeName =  URLDecoder.decode(request.getParameter("typeName"), "UTF-8");
		String hql = " from YDUser v where 1=1 and v.userType.name=? ";
		List<String> para = new ArrayList<String>();
		para.add(typeName);
		util util = new util();
		 int rows = 10;int page = 1;
		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
		try {
			response.setContentType("text/json;charset=utf-8");	
			List<YDUser> Users = ydVisiterInter.findUsers(hql, para);
			if (Users != null && Users.size() > 0) {
				List<vUser> vuser = new ArrayList<vUser>();
				int id=1;
				for(YDUser user : Users){
					vUser vu = new vUser();
					vu.setId(id++);
					vu.setSeartchTimes(user.getSearchTime());
					vu.setUid(user.getUid());
					vu.setVisitAppTimes(user.getVisitAppTimes());
					vu.setVisitAtlasTimes(user.getVisitAtlasTimes());
					vu.setVisitDataTimes(user.getVisitDataTimes());
					vu.setDownloadAtlasTime(user.getDownloadAtlasTime());
					vu.setVisitProductionTimes(user.getVisitProductionTimes());
					vu.setVisitedServiceTimes(user.getVisitServiceTimes());
					vuser.add(vu);
				}
				flag = true;			
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", vuser.size());
				map.put("rows", util.getFenyeFiles(vuser, rows, page));
				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
				util.writeJson(objects, request, response);
			} else {
		        flag = true;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", flag);
				map.put("total", 0);
				map.put("rows", Users);
				JSONObject object = JSONObject.fromObject(map);
				object.fromObject(object, jsonConfig);
				util.writeJson(object, request, response);

			}
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total", 0);
			map.put("rows", "[]");
			JSONObject object = JSONObject.fromObject(map);
			object.fromObject(object, jsonConfig);
			util.writeJson(object, request, response);
			e.printStackTrace();
		}
		

	}
	
//	/**
//	 * 
//                     用户类型历史查询4chart
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getUserTypeHistory4chart")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getUserTypeHistory4chart(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		List<Date> paras =new ArrayList<Date>();
//		String hql_data = "from YDVisiterData vs where 1=1 ";
//		String hql_service = "from YDVisiterService vs where 1=1 ";
//		String hql_page = "from YDVisiterPage vs where 1=1 ";
//		
//		try {
//			if (request.getParameter("DateStart") != null
//					&& !request.getParameter("DateStart").trim()
//							.equals("")) {
//				hql_data = hql_data + "" + " and vs.downloadDate > ? ";
//				hql_service = hql_service + "" + " and vs.visisteDate > ? ";
//				hql_page = hql_page + "" + " and vs.visitedTime > ? ";
//				String DateStart = request
//						.getParameter("DateStart");
//				Date d_start = sdf.parse(DateStart);
//
//				paras.add(d_start);
//			}
//			
//			if (request.getParameter("DateEnd") != null
//					&& !request.getParameter("DateEnd").trim()
//							.equals("")) {
//				hql_data = hql_data + "" + " and vs.downloadDate < ? ";
//				hql_service = hql_service + "" + " and vs.visisteDate < ? ";
//				hql_page = hql_page + "" + " and vs.visitedTime < ? ";
//				String DateEnd = request
//						.getParameter("DateEnd");
//				Date d_end = sdf.parse(DateEnd);
//
//				paras.add(d_end);
//			}
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.find(hql_data, paras);
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findBydate(hql_service, paras);
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findBydate(hql_page, paras);
//			Map<String,Integer> pages = new HashMap<String, Integer>() ;
//			Map<String,Integer> datas =new HashMap<String, Integer>() ; 
//			Map<String,Integer> srvices =new HashMap<String, Integer>() ; 
//			ArrayList<vuserType>  json = new ArrayList<vuserType>();
//			int id = 1;
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {				
//				datas= util.getDatamapByuType(ydVisiterDatas);
//				Iterator<?> iter = datas.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				vuserType v = new vuserType();
//				v.setId(id++);
//				v.setTypeName(entry.getKey());
//				v.setDownloadTimes(entry.getValue());
//				json.add(v);}
//			}  
//			if(ydVisiterServices !=null && ydVisiterServices.size()>0) {
//				srvices = util.getServiceBysType(ydVisiterServices);
//				Iterator<?> iter = srvices.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisiteTimes(entry.getValue());
//							fag = false;
//							break;	
//						}	
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisiteTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						json.add(v2);
//					}
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisiteTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					json.add(v2);
//				}
//				
//			}
//				}
//			if(ydVisiterPages !=null && ydVisiterPages.size()>0 ){
//				pages = util.getPagemapByuType(ydVisiterPages);
//				Iterator<?> iter = pages.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisitePageTimes(entry.getValue());
//							fag = false;
//							break;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//							v2.setId(id++);
//							v2.setTypeName(entry.getKey());
//							v2.setVisitePageTimes(entry.getValue());
//							v2.setDownloadTimes(0);
//							v2.setVisiteTimes(0);
//							json.add(v2);
//					}
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisitePageTimes(srvices.get(entry.getValue()));
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(0);
//					json.add(v2);
//				}
//				
//			}	
//			}
//	 			flag = true;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("success", flag);
//				map.put("total", json.size());
//				map.put("rows", json);
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//	}
	
//	/**
//	 * 
//                     用户类型历史查询
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getUserTypeHistory")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getUserTypeHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		int rows = 10;
//		int page = 1;
//		if (request.getParameter("rows") != null
//				&& Integer.parseInt(request.getParameter("rows")) > 0)
//			rows = Integer.parseInt(request.getParameter("rows"));
//		if (request.getParameter("rows") != null
//				&& Integer.parseInt(request.getParameter("page")) > 0)
//			page = Integer.parseInt(request.getParameter("page"));
//		List<Date> paras =new ArrayList<Date>();
//		String hql_data = "from YDVisiterData vs where 1=1 ";
//		String hql_service = "from YDVisiterService vs where 1=1 ";
//		String hql_page = "from YDVisiterPage vs where 1=1 ";
//		
//		try {
//			if (request.getParameter("DateStart") != null
//					&& !request.getParameter("DateStart").trim()
//							.equals("")) {
//				hql_data = hql_data + "" + " and vs.downloadDate > ? ";
//				hql_service = hql_service + "" + " and vs.visisteDate > ? ";
//				hql_page = hql_page + "" + " and vs.visitedTime > ? ";
//				String DateStart = request
//						.getParameter("DateStart");
//				Date d_start = sdf.parse(DateStart);
//
//				paras.add(d_start);
//			}
//			
//			if (request.getParameter("DateEnd") != null
//					&& !request.getParameter("DateEnd").trim()
//							.equals("")) {
//				hql_data = hql_data + "" + " and vs.downloadDate < ? ";
//				hql_service = hql_service + "" + " and vs.visisteDate < ? ";
//				hql_page = hql_page + "" + " and vs.visitedTime < ? ";
//				String DateEnd = request
//						.getParameter("DateEnd");
//				Date d_end = sdf.parse(DateEnd);
//
//				paras.add(d_end);
//			}
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.find(hql_data, paras);
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findBydate(hql_service, paras);
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findBydate(hql_page, paras);
//			Map<String,Integer> pages = new HashMap<String, Integer>() ;
//			Map<String,Integer> datas =new HashMap<String, Integer>() ; 
//			Map<String,Integer> srvices =new HashMap<String, Integer>() ; 
//			ArrayList<vuserType>  json = new ArrayList<vuserType>();
//			int id =(page-1)*rows+1;
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {				
//				datas= util.getDatamapByuType(ydVisiterDatas);
//				Iterator<?> iter = datas.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				vuserType v = new vuserType();
//				v.setId(id++);
//				v.setTypeName(entry.getKey());
//				v.setDownloadTimes(entry.getValue());
//				json.add(v);}
//			}  if(ydVisiterServices !=null && ydVisiterServices.size()>0) {
//				srvices = util.getServiceBysType(ydVisiterServices);
//				Iterator<?> iter = srvices.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisiteTimes(entry.getValue());
//							fag = false;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//							v2.setId(id++);
//							v2.setTypeName(entry.getKey());
//							v2.setDownloadTimes(0);
//							v2.setVisiteTimes(entry.getValue());
//							json.add(v2);
//					}
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(entry.getValue());
//					json.add(v2);	
//				}	
//			}
//				}
//		 if(ydVisiterPages !=null && ydVisiterPages.size()>0 ){
//				pages = util.getPagemapByuType(ydVisiterPages);
//				Iterator<?> iter = pages.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisitePageTimes(entry.getValue());
//							fag = false;
//							break;
//						}	
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setDownloadTimes(0);
//						v2.setVisiteTimes(0);
//						v2.setVisitePageTimes(entry.getValue());
//						json.add(v2);
//					}
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(0);
//					v2.setVisitePageTimes(entry.getValue());
//					json.add(v2);
//				}
//				
//			}	
//			}
//	 			flag = true;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("success", flag);
//				map.put("total", json.size());
//				map.put("rows", util.getFenyeFiles(json, rows, page));
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//	}
	/**
	 * 分页查询所有用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/findAllUserByFenye")
	public void findAllUserByFenye(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JsonConfig jsonConfig = new JsonConfig();
		PrintWriter writer = response.getWriter();
		int rows = 10;
		int page = 1;
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("rows")) > 0)
			rows = Integer.parseInt(request.getParameter("rows"));
		if (request.getParameter("rows") != null
				&& Integer.parseInt(request.getParameter("page")) > 0)
			page = Integer.parseInt(request.getParameter("page"));

		boolean flag = false;
		String hql = " from  YDUser visiter where 1=1 ";
		
		List<Object> paras = new ArrayList<Object>();
		String hql_total = "select count(*) from YDUser where 1=1 ";
		SimpleDateFormat sdf_fmg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		Date registerDateEnd;
		Date loginDateStart;
		Date registerDateStart;
		Date loginDateEnd;

		util util = new util();
		try {
			response.setContentType("text/json;charset=utf-8");

			if (request.getParameter("uname") != null
					&& !request.getParameter("uname").trim().equals("")) {
				hql = hql + "" + " and visiter.uid like '%"
						+ request.getParameter("uname").trim() + "%' ";
				hql_total = hql_total + "" + " and uid like '%"
						+ request.getParameter("uname").trim() + "%' ";
			}
			if (request.getParameter("registerDateStart") != null
					&& !request.getParameter("registerDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and visiter.registerDate > ? ";
				hql_total = hql_total + "" + " and registerDate > ? ";
				String registerDateStart_temp = request
						.getParameter("registerDateStart");
				registerDateStart = sdf_fmg.parse(registerDateStart_temp);

				paras.add(registerDateStart);
			}
			if (request.getParameter("registerDateEnd") != null
					&& !request.getParameter("registerDateEnd").trim()
							.equals("")) {
				hql = hql + " and visiter.registerDate < ? ";
				hql_total = hql_total + " and registerDate < ? ";
				registerDateEnd = sdf_fmg.parse(request
						.getParameter("registerDateEnd"));
				paras.add(registerDateEnd);
			}
			if (request.getParameter("loginDateStart") != null
					&& !request.getParameter("loginDateStart").trim()
							.equals("")) {
				hql = hql + "" + " and visiter.loginDate > ? ";
				hql_total = hql_total + "" + " and loginDate > ? ";
				loginDateStart = sdf_fmg.parse(request
						.getParameter("loginDateStart"));
				paras.add(loginDateStart);
			}
			if (request.getParameter("loginDateEnd") != null
					&& !request.getParameter("loginDateEnd").trim().equals("")) {
				hql = hql + "" + " and visiter.loginDate < ? ";
				hql_total = hql_total + "" + " and loginDate < ? ";

				loginDateEnd = sdf_fmg.parse(request
						.getParameter("updateDateEnd"));
				paras.add(loginDateEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<YDUser> ydVisiters = ydVisiterInter.findByFenye(
				hql, paras, page, rows);

		Long total = ydVisiterInter.getTotal(hql_total, paras);
		
		if (ydVisiters != null && ydVisiters.size() > 0) {
			
			List<vUser> visitors = new ArrayList<vUser>();
			for (YDUser ydVisiter : ydVisiters) {
				vUser u = new vUser();
				u.setId(ydVisiter.getId());
				u.setTypeName(ydVisiter.getUserType().getName());
				u.setVisiteIp(ydVisiter.getUserIp());
				u.setRegisterDate(sdf_fmg.format(ydVisiter.getRegisterDate()));
				u.setUpdateDate(sdf_fmg.format(ydVisiter.getUpdateDate()));
				u.setUname(ydVisiter.getUname());
				u.setUid(ydVisiter.getUid());
				u.setDownloadAtlasTime(ydVisiter.getDownloadAtlasTime());
				u.setSeartchTimes(ydVisiter.getSearchTime());
				u.setVisitAppTimes(ydVisiter.getVisitAppTimes());
				u.setVisitAtlasTimes(ydVisiter.getVisitAtlasTimes());
				u.setVisitDataTimes(ydVisiter.getVisitDataTimes());
				u.setVisitProductionTimes(ydVisiter.getVisitProductionTimes());
				u.setVisitServiceTimes(ydVisiter.getVisitServiceTimes());
				u.setVisitPageTime(ydVisiter.getVisitPageTime());
				visitors.add(u);
			}
			flag = true;
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", flag);
			map.put("total",total );
			map.put("rows", visitors);
			JSONObject objects = JSONObject.fromObject(map, jsonConfig);
			util.writeJson(objects, request, response);
		} else {
			String json = "[]";
			writer.write(json);
			writer.flush();
			writer.close();

		}
	}
	

	// 添加用户
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/addYduser")
	@ResponseBody
	public void addYduser(HttpServletRequest request,
			HttpServletResponse response) {
		util util = new util();
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String uid = request.getParameter("uid");
		response.setContentType("text/json;charset=utf-8");
		PrintWriter writer;
		JsonConfig jsonConfig = util.getjsonConfig();
		if (ydVisiterInter.findVisitorByUid(uid) != null) {
			Message ms = new Message();
			ms.setText("该用户ID已存在");
			JSONObject object = JSONObject.fromObject(ms, jsonConfig);
			util.writeJson(object, flag, request, response);
		} else {
			
			String utId = request.getParameter("utId");
			YDUser ydVisiter = new YDUser();
			try {
				ydVisiter.setUid(uid);
				ydVisiter.setUname(request.getParameter("uname"));
				ydVisiter.setUserIp(request.getParameter("UserIp"));
				ydVisiter.setRegisterDate(new java.util.Date());
				ydVisiter.setUpdateDate(new java.util.Date());
				ydVisiter.setLoginDate(null);
				ydVisiter.setDownloadAtlasTime(0);
				ydVisiter.setSearchTime(0);
				ydVisiter.setVisitAppTimes(0);
				ydVisiter.setVisitAtlasTimes(0);
				ydVisiter.setVisitDataTimes(0);
				ydVisiter.setVisitProductionTimes(0);
				ydVisiter.setVisitServiceTimes(0);
				
				ydVisiter.setVisitPageTime(0);
				UserType ut =	userTypeServiceInter.getUserTypeById(UserType.class, Integer.parseInt(utId));
				ydVisiter.setUserType(ut);
				ydVisiterInter.save(ydVisiter);
				flag = true;
				vUser visitor = new vUser();
				visitor.setId(ydVisiter.getId());
				visitor.setUname(ydVisiter.getUname());
				visitor.setVisitePageTimes(ydVisiter.getVisitPageTime());
				visitor.setUid(ydVisiter.getUid());
				visitor.setRegisterDate(sdf.format(ydVisiter.getRegisterDate()));
				
				JSONObject object = JSONObject.fromObject(visitor, jsonConfig);
				System.out.println("添加用户成功");
				util.writeJson(object, flag, request, response);
			} catch (Exception e) {
				try {
					writer = response.getWriter();
					writer.write("{\"success\":false,\"result\":添加用户失败}");
				} catch (IOException e1) {

	// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}

	}
	
//	/**
//	 * 
//                 服务访问详细记录
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getServiceDetailByUid")
//	@ResponseBody
//	// 将Json对象直接转换成字符串
//	public void getServiceDetailByUid(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 int rows = 10;int page = 1;String uid = request.getParameter("uid"); uid = URLDecoder.decode(uid, "UTF-8");
//		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
//		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
//		PrintWriter writer = response.getWriter();
//		List<Object> paras =new ArrayList<Object>();
//		
//		String hql = " from YDVisiterService ys where 1=1  and ys.visiter.userType like '%" + uid+"%' ";
//		String hql_total = "select count(*) from YDVisiterService ys where 1=1  and ys.visiter.userType like '%"+ uid+"%' ";
//		try {
//			
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findByFenye(hql, null, page, rows);
//			if (ydVisiterServices != null && ydVisiterServices.size() > 0) {
//				flag= true; int id=(page-1)*rows+1;
//				List<vUserServices>	UserServices = new ArrayList<vUserServices>();
//				
//				for(YDVisiterService ys :ydVisiterServices ){
//					vUserServices v = new vUserServices();
//					
//					v.setId(id++);
//					v.setUid(ys.getVisiter().getUid());
//					v.setService(ys.getService().getSid());
//				//	v.setUip(ys.getVisiter().getLastLoginIp());
//					v.setVisitedTime(sdf.format(ys.getVisisteDate()));
//					UserServices.add(v);
//				}
//				long total = ydVisiterServiceInter.getTotal(hql_total, paras);
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("success", flag);
//				map.put("total", total);
//				map.put("rows",UserServices );
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//				
//			} else {
//
//				String json = "[]";
//				writer.write(json);
//				writer.flush();
//				writer.close();
//			}
//	
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
//	/**
//	 * 
//                 数据下载详细记录
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getDataDetailByUid")
//	@ResponseBody
//	// 将Json对象直接转换成字符串
//	public void getDataDetailByUid(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 int rows = 10;int page = 1;String uid = request.getParameter("uid");uid = URLDecoder.decode(uid, "UTF-8");
//		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
//		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
//		PrintWriter writer = response.getWriter();
//		List<Object> paras =new ArrayList<Object>();
//		
//		String hql = " from YDVisiterData ys where 1=1  and ys.ydVisiter.userType like '%" + uid+"%'  ";
//		String hql_total = "select count(*) from YDVisiterData ys where 1=1  and ys.ydVisiter.userType like '%" + uid+"%' ";
//		try {
//			
//			
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.findByFenye(hql, null, page, rows);
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {
//				flag= true; int id=(page-1)*rows+1;
//				List<vUserData>	UserData = new ArrayList<vUserData>();
//				
//				for(YDVisiterData ys :ydVisiterDatas ){
//					vUserData v = new vUserData();
//					
//					v.setId(id++);
//					v.setUid(ys.getYdVisiter().getUid());
//				//	v.setUip(ys.getYdVisiter().getLastLoginIp());
//					v.setData(ys.getYdData().getDid());
//					v.setDownloadDate(sdf.format(ys.getDownloadDate()));
//					UserData.add(v);
//				}
//				long total = ydVisiterDataInter.getTotal(hql_total, paras);
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("success", flag);
//				map.put("total", total);
//				map.put("rows",UserData );
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//				
//			} else {
//
//				String json = "[]";
//				writer.write(json);
//				writer.flush();
//				writer.close();
//			}
//	
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
//	/**
//	 * 
//              页面访问详细记录
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getPageDetailByUid")
//	@ResponseBody
//	// 将Json对象直接转换成字符串
//	public void getPageDetailByUid(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 int rows = 10;int page = 1;String uid = request.getParameter("uid");uid = URLDecoder.decode(uid, "UTF-8");
//		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
//		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
//		PrintWriter writer = response.getWriter();
//		List<Object> paras =new ArrayList<Object>();
//		String hql = " from YDVisiterPage ys where 1=1  and ys.ydVisiter.userType like '%" + uid+"%' ";
//		String hql_total = "select count(*) from YDVisiterPage ys where 1=1  and ys.ydVisiter.userType like '%" + uid+"%' ";
//		try {
//			
//			
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findByFenye(hql, null, page, rows);
//			if (ydVisiterPages != null && ydVisiterPages.size() > 0) {
//				flag= true; int id = (page-1)*rows+1;
//				List<vUserPage>	UserPage = new ArrayList<vUserPage>();
//				
//				for(YDVisiterPage ys :ydVisiterPages ){
//					vUserPage v = new vUserPage();
//					
//					v.setId(id++);
//					v.setUid(ys.getYdVisiter().getUid());
//			//		v.setUip(ys.getYdVisiter().getLastLoginIp());
//					v.setPage(ys.getYdPage().getPid());
//					v.setVisitedTime(sdf.format(ys.getVisitedTime()));
//					UserPage.add(v);
//				}
//				long total = ydUserPageInter.getTotal(hql_total, paras);
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("success", flag);
//				map.put("total", total);
//				map.put("rows",UserPage);
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//				
//			} else {
//
//				String json = "[]";
//				writer.write(json);
//				writer.flush();
//				writer.close();
//			}
//	
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
//	/**
//	 * 
//                     用户类型累计查询4chart
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getUserTypeThisYear4chart")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getUserTypeThisYear4chart(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//		Date d = new Date();
//		List<Date> paras =new ArrayList<Date>();
//		String hql_data = "from YDVisiterData vs where vs.downloadDate > ? and vs.downloadDate < ?";
//		String hql_service = "from YDVisiterService vs where vs.visisteDate > ? and vs.visisteDate < ?";
//		String hql_page = "from YDVisiterPage vs where vs.visitedTime > ? and vs.visitedTime < ?";
//		
//		try {
//			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
//			Date d_start = sdf.parse(start);
//			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
//			Date d_end = sdf.parse(end);
//			paras.add(d_start);paras.add(d_end);
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.find(hql_data, paras);
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findBydate(hql_service, paras);
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findBydate(hql_page, paras);
//			Map<String,Integer> pages = new HashMap<String, Integer>() ;
//			Map<String,Integer> datas =new HashMap<String, Integer>() ; 
//			Map<String,Integer> srvices =new HashMap<String, Integer>() ; 
//			ArrayList<vuserType>  json = new ArrayList<vuserType>();
//			int id = 1;
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {				
//				datas= util.getDatamapByuType(ydVisiterDatas);
//				if(datas.size()>0){
//					Iterator<?> iter = datas.entrySet().iterator();
//					while (iter.hasNext()) {
//					@SuppressWarnings("unchecked")
//					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//					
//					vuserType v = new vuserType();
//					v.setId(id++);
//					v.setTypeName(entry.getKey());
//					v.setDownloadTimes(datas.get(entry.getValue()));
//					v.setVisitePageTimes(0);
//					v.setVisiteTimes(0);
//					json.add(v);}
//				}			
//			} 
//			if(ydVisiterServices !=null && ydVisiterServices.size()>0) {
//				srvices = util.getServiceBysType(ydVisiterServices);
//				if(srvices.size()>0){
//					Iterator<?> iter = srvices.entrySet().iterator();
//					while (iter.hasNext()) {
//					@SuppressWarnings("unchecked")
//					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//					if(json!=null && json.size()>0){
//						boolean fag = true;
//							for(vuserType v : json){
//							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//								v.setVisiteTimes(entry.getValue());
//								fag = false;
//								break;
//							}	
//							}//for
//						if(fag){
//							vuserType v2 = new vuserType();
//							v2.setId(id++);
//							v2.setTypeName(entry.getKey());
//							v2.setVisiteTimes(entry.getValue());
//							v2.setDownloadTimes(0);
//							v2.setVisitePageTimes(0);
//							json.add(v2);
//						}
//							
//						
//					}else{
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisiteTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisitePageTimes(0);
//						json.add(v2);
//					}
//				}//while	
//					
//					
//			}
//				}
//			 if(ydVisiterPages !=null && ydVisiterPages.size()>0 ){
//				pages = util.getPagemapByuType(ydVisiterPages);
//				if(pages.size()>0){
//					Iterator<?> iter = pages.entrySet().iterator();
//					while (iter.hasNext()) {
//					@SuppressWarnings("unchecked")
//					Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//					if(json!=null && json.size()>0){
//						boolean fag = true;
//						for(vuserType v : json){
//							if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//								v.setVisitePageTimes(entry.getValue());
//								fag = false;
//								break;
//							}
//							
//						}
//						if(fag){
//							vuserType v2 = new vuserType();
//							v2.setId(id++);
//							v2.setTypeName(entry.getKey());
//							v2.setVisitePageTimes(entry.getValue());
//							v2.setDownloadTimes(0);
//							v2.setVisiteTimes(0);
//							json.add(v2);	
//						}
//						
//					}else{
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisitePageTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisiteTimes(0);
//						json.add(v2);
//					}
//				}//while
//			}	
//			}
//	 			flag = true;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("success", flag);
//				map.put("total", json.size());
//				map.put("rows", json);
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//	}
	
	
//	/**
//	 * 
//                     用户类型month累计查询4chart
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getUserTypeThisMonth4chart")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getUserTypeThisMonth4chart(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//		Date d = new Date();
//		List<Date> paras =new ArrayList<Date>();
//		String hql_data = "from YDVisiterData vs where vs.downloadDate > ? and vs.downloadDate < ?";
//		String hql_service = "from YDVisiterService vs where vs.visisteDate > ? and vs.visisteDate < ?";
//		String hql_page = "from YDVisiterPage vs where vs.visitedTime > ? and vs.visitedTime < ?";
//		
//		try {
//			Calendar cal = Calendar.getInstance();   cal.setTime(d);
//			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
//			Date d_start = sdf.parse(start);
//			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
//			Date d_end = sdf.parse(end);
//			paras.add(d_start);paras.add(d_end);
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.find(hql_data, paras);
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findBydate(hql_service, paras);
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findBydate(hql_page, paras);
//			Map<String,Integer> pages = new HashMap<String, Integer>() ;
//			Map<String,Integer> datas =new HashMap<String, Integer>() ; 
//			Map<String,Integer> srvices =new HashMap<String, Integer>() ; 
//			ArrayList<vuserType>  json = new ArrayList<vuserType>();
//			int id = 1;
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {				
//				datas= util.getDatamapByuType(ydVisiterDatas);
//				Iterator<?> iter = datas.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				
//				vuserType v = new vuserType();
//				v.setId(id++);
//				v.setTypeName(entry.getKey());
//				v.setDownloadTimes(datas.get(entry.getValue()));
//				v.setVisitePageTimes(0);
//				v.setVisiteTimes(0);
//				json.add(v);}
//				
//			} 
//			if(ydVisiterServices !=null && ydVisiterServices.size()>0) {
//				srvices = util.getServiceBysType(ydVisiterServices);
//				Iterator<?> iter = srvices.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisiteTimes(entry.getValue());
//							fag = false;
//							break;
//						}
//						
//					}//修改
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisiteTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisitePageTimes(0);
//						json.add(v2);
//					}
//				
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisiteTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					v2.setVisitePageTimes(0);
//					json.add(v2);
//				}//添加
//				
//			}
//				}
//			 if(ydVisiterPages !=null && ydVisiterPages.size()>0 ){
//				pages = util.getPagemapByuType(ydVisiterPages);
//				Iterator<?> iter = pages.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisitePageTimes(entry.getValue());
//							fag = false;
//							break;
//						}
//					}//更新
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisitePageTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisiteTimes(0);
//						json.add(v2);
//					}
//					
//				}else{//添加
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisitePageTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(0);
//					json.add(v2);
//				}
//				
//			}	
//			}
//	 			flag = true;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("success", flag);
//				map.put("total", json.size());
//				map.put("rows", json);
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//	}
	
//	/**
//	 * 
//                     用户类型今日累计查询4chart
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getUserTypeToday4chart")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getUserTypeToday4chart(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		Date d = new Date();
//		List<Date> paras =new ArrayList<Date>();
//		String hql_data = "from YDVisiterData vs where vs.downloadDate > ? and vs.downloadDate < ?";
//		String hql_service = "from YDVisiterService vs where vs.visisteDate > ? and vs.visisteDate < ?";
//		String hql_page = "from YDVisiterPage vs where vs.visitedTime > ? and vs.visitedTime < ?";
//		
//		try {
//			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
//			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
//			paras.add(d_start);paras.add(d_end);
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.find(hql_data, paras);
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findBydate(hql_service, paras);
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findBydate(hql_page, paras);
//			Map<String,Integer> pages = new HashMap<String, Integer>() ;
//			Map<String,Integer> datas =new HashMap<String, Integer>() ; 
//			Map<String,Integer> srvices =new HashMap<String, Integer>() ; 
//			ArrayList<vuserType>  json = new ArrayList<vuserType>();
//			int id = 1;
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {				
//				datas= util.getDatamapByuType(ydVisiterDatas);
//				Iterator<?> iter = datas.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				
//				vuserType v = new vuserType();
//				v.setId(id++);
//				v.setTypeName(entry.getKey());
//				v.setDownloadTimes(datas.get(entry.getValue()));
//				v.setVisitePageTimes(0);
//				v.setVisiteTimes(0);
//				json.add(v);}
//				
//			} 
//			if(ydVisiterServices !=null && ydVisiterServices.size()>0) {
//				srvices = util.getServiceBysType(ydVisiterServices);
//				Iterator<?> iter = srvices.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisiteTimes(entry.getValue());
//							fag = false;
//							break;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setDownloadTimes(0);
//						v2.setVisiteTimes(entry.getValue());
//						v2.setVisitePageTimes(0);
//						json.add(v2);
//					}
//					
//					
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisiteTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					v2.setVisitePageTimes(0);
//					json.add(v2);
//				}
//				
//			}
//				}
//			 if(ydVisiterPages !=null && ydVisiterPages.size()>0 ){
//				pages = util.getPagemapByuType(ydVisiterPages);
//				Iterator<?> iter = pages.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisitePageTimes(entry.getValue());
//							fag=false;
//							break;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisitePageTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisiteTimes(0);
//						json.add(v2);
//					}
//					
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisitePageTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(0);
//					json.add(v2);
//				}
//				
//			}	
//			}
//	 			flag = true;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("success", flag);
//				map.put("total", json.size());
//				map.put("rows", json);
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//	}
	
//	/**
//	 * 本日用户类型累计
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getUserTypeToday")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getUserTypeToday(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		 int rows = 10;int page = 1;
//		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
//		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
//		Date d = new Date();
//		List<Date> paras =new ArrayList<Date>();
//		String hql_data = "from YDVisiterData vs where vs.downloadDate > ? and vs.downloadDate < ?";
//		String hql_service = "from YDVisiterService vs where vs.visisteDate > ? and vs.visisteDate < ?";
//		String hql_page = "from YDVisiterPage vs where vs.visitedTime > ? and vs.visitedTime < ?";
//		try {
//			Date d_start = sdf.parse(formatter.format(d) +" "+ "00:00:00");
//			Date d_end = sdf.parse(formatter.format(d) +" "+ "23:59:59");
//			paras.add(d_start);paras.add(d_end);
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.find(hql_data, paras);
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findBydate(hql_service, paras);
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findBydate(hql_page, paras);
//			Map<String,Integer> pages = new HashMap<String, Integer>() ;
//			Map<String,Integer> datas =new HashMap<String, Integer>() ; 
//			Map<String,Integer> srvices =new HashMap<String, Integer>() ; 
//			ArrayList<vuserType>  json = new ArrayList<vuserType>();
//			int id = (page-1)*rows+1;
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {				
//				datas= util.getDatamapByuType(ydVisiterDatas);
//				Iterator<?> iter = datas.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				
//				vuserType v = new vuserType();
//				v.setId(id++);
//				v.setTypeName(entry.getKey());
//				v.setDownloadTimes(datas.get(entry.getValue()));
//				v.setVisitePageTimes(0);
//				v.setVisiteTimes(0);
//				json.add(v);}
//				
//			} 
//			if(ydVisiterServices !=null && ydVisiterServices.size()>0) {
//				srvices = util.getServiceBysType(ydVisiterServices);
//				Iterator<?> iter = srvices.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisiteTimes(entry.getValue());
//							fag= false;
//							break;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisiteTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisitePageTimes(0);
//						json.add(v2);	
//					}
//					
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisiteTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					v2.setVisitePageTimes(0);
//					json.add(v2);
//				}
//				
//			}
//				}
//			 if(ydVisiterPages !=null && ydVisiterPages.size()>0 ){
//				pages = util.getPagemapByuType(ydVisiterPages);
//				Iterator<?> iter = pages.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisitePageTimes(entry.getValue());
//							fag = false;
//							break;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisitePageTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisiteTimes(0);
//						json.add(v2);
//					}
//				
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisitePageTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(0);
//					json.add(v2);
//				}
//				
//			}	
//			}
//	 			flag = true;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("success", flag);
//				map.put("total", json.size());
//				map.put("rows", util.getFenyeFiles(json, rows, page));
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//
//	}
	
//	/**
//	 * 本月数据价格历史查询
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getUserTypeThisMonth")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getUserTypeThisMonth(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//		 int rows = 10;int page = 1;
//		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
//		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
//		Date d = new Date();
//		List<Date> paras =new ArrayList<Date>();
//		String hql_data = "from YDVisiterData vs where vs.downloadDate > ? and vs.downloadDate < ?";
//		String hql_service = "from YDVisiterService vs where vs.visisteDate > ? and vs.visisteDate < ?";
//		String hql_page = "from YDVisiterPage vs where vs.visitedTime > ? and vs.visitedTime < ?";
//		try {
//			Calendar cal = Calendar.getInstance();   cal.setTime(d);
//			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "00:00:00";
//			Date d_start = sdf.parse(start);
//			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), cal.get(Calendar.MONTH)+1) +" "+ "23:59:59";
//			Date d_end = sdf.parse(end);
//			paras.add(d_start);paras.add(d_end);
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.find(hql_data, paras);
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findBydate(hql_service, paras);
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findBydate(hql_page, paras);
//			Map<String,Integer> pages = new HashMap<String, Integer>() ;
//			Map<String,Integer> datas =new HashMap<String, Integer>() ; 
//			Map<String,Integer> srvices =new HashMap<String, Integer>() ; 
//			ArrayList<vuserType>  json = new ArrayList<vuserType>();
//			int id = (page-1)*rows+1;
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {				
//				datas= util.getDatamapByuType(ydVisiterDatas);
//				Iterator<?> iter = datas.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				vuserType v = new vuserType();
//				v.setId(id++);
//				v.setTypeName(entry.getKey());
//				v.setDownloadTimes(entry.getValue());
//				v.setVisitePageTimes(0);
//				v.setVisitePageTimes(0);
//				json.add(v);}
//			} 
//			if(ydVisiterServices !=null && ydVisiterServices.size()>0) {
//				srvices = util.getServiceBysType(ydVisiterServices);
//				Iterator<?> iter = srvices.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisiteTimes(entry.getValue());
//							fag = false;
//							break;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisiteTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisitePageTimes(0);
//						json.add(v2);
//					}
//					
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisiteTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					v2.setVisitePageTimes(0);
//					json.add(v2);
//				}
//				
//			}
//				}
//			 if(ydVisiterPages !=null && ydVisiterPages.size()>0 ){
//				pages = util.getPagemapByuType(ydVisiterPages);
//				Iterator<?> iter = pages.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisitePageTimes(entry.getValue());
//							fag = false;
//							break;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisitePageTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisiteTimes(0);
//						json.add(v2);	
//					}
//					
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisitePageTimes(entry.getValue());
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(0);
//					json.add(v2);
//				}
//				
//			}	
//			}
//	 			flag = true;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("success", flag);
//				map.put("total", json.size());
//				map.put("rows", util.getFenyeFiles(json, rows, page));
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//
//	}
	
	
	
	
//	/**
//	 * 本年度数据价格历史查询
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 */
//	@RequestMapping(value = "/getUserTypeThisYear")
//	@ResponseBody
//	// 将json对象直接转换成字符串
//	public void getDataPriceThisYear(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//		 int rows = 10;int page = 1;
//		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
//		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
//		Date d = new Date();
//		List<Date> paras =new ArrayList<Date>();
//		String hql_data = "from YDVisiterData vs where vs.downloadDate > ? and vs.downloadDate < ?";
//		String hql_service = "from YDVisiterService vs where vs.visisteDate > ? and vs.visisteDate < ?";
//		String hql_page = "from YDVisiterPage vs where vs.visitedTime > ? and vs.visitedTime < ?";
//		try {
//			String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
//			Date d_start = sdf.parse(start);
//			String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
//			Date d_end = sdf.parse(end);
//			paras.add(d_start);paras.add(d_end);
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.find(hql_data, paras);
//			List<YDVisiterService> ydVisiterServices = ydVisiterServiceInter.findBydate(hql_service, paras);
//			List<YDVisiterPage> ydVisiterPages = ydUserPageInter.findBydate(hql_page, paras);
//			Map<String,Integer> pages = new HashMap<String, Integer>() ;
//			Map<String,Integer> datas =new HashMap<String, Integer>() ; 
//			Map<String,Integer> srvices =new HashMap<String, Integer>() ; 
//			ArrayList<vuserType>  json = new ArrayList<vuserType>();
//			int id = (page-1)*rows+1;
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {				
//				datas= util.getDatamapByuType(ydVisiterDatas);
//				Iterator<?> iter = datas.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				vuserType v = new vuserType();
//				v.setId(id++);
//				v.setTypeName(entry.getKey());
//				v.setDownloadTimes(datas.get(entry.getValue()));
//				v.setVisitePageTimes(0);
//				v.setVisiteTimes(0);
//				json.add(v);}
//			} 
//			if(ydVisiterServices !=null && ydVisiterServices.size()>0) {
//				srvices = util.getServiceBysType(ydVisiterServices);
//				Iterator<?> iter = srvices.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisiteTimes(entry.getValue());
//							fag= false;
//							break;
//						}
//						
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setVisiteTimes(entry.getValue());
//						v2.setDownloadTimes(0);
//						v2.setVisitePageTimes(0);
//						json.add(v2);	
//					}
//					
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(entry.getValue());
//					v2.setVisitePageTimes(0);
//					json.add(v2);
//				}
//				
//			}
//				}
//			 if(ydVisiterPages !=null && ydVisiterPages.size()>0 ){
//				pages = util.getPagemapByuType(ydVisiterPages);
//				Iterator<?> iter = pages.entrySet().iterator();
//				while (iter.hasNext()) {
//				@SuppressWarnings("unchecked")
//				Map.Entry<String , Integer> entry =  (Entry<String, Integer>) iter.next();
//				if(json!=null && json.size()>0){
//					boolean fag = true;
//					for(vuserType v : json){
//						if(v.getTypeName().equalsIgnoreCase(entry.getKey())){
//							v.setVisitePageTimes(entry.getValue());
//							fag = false;
//							break;
//						}
//					}
//					if(fag){
//						vuserType v2 = new vuserType();
//						v2.setId(id++);
//						v2.setTypeName(entry.getKey());
//						v2.setDownloadTimes(0);
//						v2.setVisiteTimes(0);
//						v2.setVisitePageTimes(entry.getValue());
//						json.add(v2);	
//					}
//					
//				}else{
//					vuserType v2 = new vuserType();
//					v2.setId(id++);
//					v2.setTypeName(entry.getKey());
//					v2.setVisitePageTimes(srvices.get(entry.getValue()));
//					v2.setDownloadTimes(0);
//					v2.setVisiteTimes(0);
//					json.add(v2);
//				}
//				
//			}	
//			}
//	 			flag = true;
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("success", flag);
//				map.put("total", json.size());
//				map.put("rows", util.getFenyeFiles(json, rows, page));
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//
//	}
//	
	
//	/**
//	 * 
//                本年度价格详细记录
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException 
//	 * @throws ParseException 
//	 */
//	@RequestMapping(value = "/getDetailByPrice4thisyear")
//	@ResponseBody
//	// 将Json对象直接转换成字符串
//	public void getDetailByPrice4thisyear(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
//		JsonConfig jsonConfig = new JsonConfig();
//		boolean flag = false;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//		 int rows = 10;int page = 1; String price = request.getParameter("price");
//		 if(request.getParameter("rows")!=null &&Integer.parseInt( request.getParameter("rows"))>0 ) rows = Integer.parseInt(request.getParameter("rows"));
//		 if(request.getParameter("page")!=null &&Integer.parseInt( request.getParameter("page"))>0 ) page = Integer.parseInt(request.getParameter("page"));	
//		Date d = new Date();
//		PrintWriter writer = response.getWriter();
//		List<Object> paras =new ArrayList<Object>();
//		String hql = " from YDVisiterData vs where 1=1  and vs.ydData.price = ? and vs.downloadDate > ? and vs.downloadDate < ? ";
//		String hql_total = "select count(*) from YDVisiterData vs where 1=1  and vs.ydData.price = ? and vs.downloadDate > ? and vs.downloadDate < ?";
//	
//		String start = util.getFirstDayOfMonth(Integer.parseInt(formatter.format(d)), 1) +" "+ "00:00:00";
//		Date d_start = sdf.parse(start);
//		String end = util.getLastDayOfMonth(Integer.parseInt(formatter.format(d)), 12) +" "+ "23:59:59";
//		Date d_end = sdf.parse(end);
//		paras.add(d_start);paras.add(d_end);
//		
//		
//		try {
//			
//			paras.add(price);
//			List<YDVisiterData> ydVisiterDatas = ydVisiterDataInter.findByFenye(hql, paras, page, rows);
//			if (ydVisiterDatas != null && ydVisiterDatas.size() > 0) {
//				flag= true; int id=(page-1)*rows+1;
//				List<vUserData>	json = new ArrayList<vUserData>();
//				
//				for(YDVisiterData ys :ydVisiterDatas ){
//					vUserData v = new vUserData();
//					
//					v.setId(id++);
//					v.setUid(ys.getYdVisiter().getUid());
//			//		v.setUip(ys.getYdVisiter().getLastLoginIp());
//					v.setDownloadDate(sdf.format(ys.getDownloadDate()));
//					json.add(v);
//				}
//				long total = ydVisiterDataInter.getTotal(hql_total, paras);
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("success", flag);
//				map.put("total", total);
//				map.put("rows",json );
//				JSONObject objects = JSONObject.fromObject(map, jsonConfig);
//				util.writeJson(objects, request, response);
//				
//			} else {
//
//				String json = "[]";
//				writer.write(json);
//				writer.flush();
//				writer.close();
//			}
//	
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
