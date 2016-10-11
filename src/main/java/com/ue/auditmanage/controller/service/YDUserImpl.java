package com.ue.auditmanage.controller.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.Query;

import util.dom4jdao;
import util.util;

import com.ue.auditmanage.controller.dao.BaseDaoInter;
import com.ue.auditmanage.controller.service.YDUserInter;

import entity.YDUser;

public class YDUserImpl implements YDUserInter {
	@Resource
	private BaseDaoInter<YDUser> baseDao;

	public BaseDaoInter<YDUser> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<YDUser> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(YDUser ydData) {
		baseDao.save(ydData);
		
	}

	@Override
	public boolean deletById(int uid, String hql) {
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return(query.executeUpdate()>0);
	}

	@Override
	
	public void update(YDUser ydData) {

		baseDao.update(ydData);
		System.out.println("更新完成");
	}

	@Override
	public YDUser getObject(Class<YDUser> cl, Serializable id) {
		
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<YDUser> findAll(String hql) {
		
		return baseDao.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YDUser> findByFenye(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<YDUser> find(String hql, String[] paras) {
		return (List<YDUser>) baseDao.findObjects(hql,paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		return baseDao.getTotal(hql,paras);
	}

	@Override
	public YDUser findVisitorByUid(String uid) {
		
		String hql = "from YDUser u where u.uid =?";
		String[] paras = {uid};
		YDUser user = baseDao.findObject(hql, paras);
		return user!=null ? user : null;
	}
	
	@Override
	public YDUser findVisitorByIp(String ip) {
		
		String hql = "from YDUser where 1=1 and UserIp =?";
		String[] paras = {ip};
		YDUser user = baseDao.findObject(hql, paras);
		return user!=null ? user : null;
	}

	@Override
	public boolean login(String uName, String pwd,String login_ip, String login_time) {
		boolean flag = false;
		String root = util.getRootPath();
		File f = new File(root+"admin.xml");
		Document doc = dom4jdao.getParse(f.getAbsolutePath());
		Element rot=doc.getRootElement();
		Element user = rot.element("user");
		String userName = user.elementTextTrim("username");
		String password = user.elementTextTrim("password");
		if(userName.equalsIgnoreCase(uName) && password.equalsIgnoreCase(pwd) ){
			
			flag = true;
			
		}else {
			System.out.println("登录验证失败");
			
		}
		return flag;
		
	}
	@Override
	public boolean deletUserByUId(String uid) {
		String hql = "delete from YDUser u where u.uid=? ";
		Query query = baseDao.getQuery(hql);
		query.setString(0, uid);
		return(query.executeUpdate()>0);
	}

	@Override
	public YDUser findVisitorByUname(String name) {
		String hql = "from YDUser u where u.uname =?";
		String[] paras = {name};
		YDUser user = baseDao.findObject(hql, paras);
		return user!=null ? user : null;
	}

	@Override
	public List<YDUser> findUsers(String hql, List paras) {
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

}
