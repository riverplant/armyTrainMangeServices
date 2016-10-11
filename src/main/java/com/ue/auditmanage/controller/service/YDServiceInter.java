package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.YDService;

public interface YDServiceInter {
	public void save(YDService ydService);
	public boolean deletById(int uid,String hql);
	public void update(YDService ydService);
	public YDService getObject(Class<YDService> cl,Serializable id);
	public List<YDService> findAllService(String hql);
	public YDService findServiceBySid(String sid);
	public List<YDService> findServiceByFenye(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<YDService> findService( String hql,String[] paras);
	public boolean deletBySid(int sid);
	public Long getTotal(String hql,List<Object>paras);
	public YDService findServiceByUrl(String url);
	public List<YDService> findServiceByList( String hql,List paras);
	
}
