package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.YDUser;

public interface YDUserInter {
	public void save(YDUser ydData);
	public boolean deletById(int uid,String hql);
	public void update(YDUser ydData);
	public YDUser getObject(Class<YDUser> cl,Serializable id);
	public boolean login(String userName, String password,String login_ip, String login_time);
	public YDUser findVisitorByUid(String uid);
	public YDUser findVisitorByUname(String name);
	public List<YDUser> findAll(String hql);
	public List<YDUser> findByFenye(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<YDUser> find( String hql,String[] paras);
	public Long getTotal(String hql,List<Object>paras);
	public boolean deletUserByUId(String uid);
	public YDUser findVisitorByIp(String ip);
	public List<YDUser> findUsers( String hql,List paras);
}
