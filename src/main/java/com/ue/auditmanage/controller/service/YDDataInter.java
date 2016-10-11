package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.YDData;

public interface YDDataInter {
	public void save(YDData ydData);
	public boolean deletById(int uid,String hql);
	public void update(YDData ydData);
	public YDData getObject(Class<YDData> cl,Serializable id);
	public List<YDData> findAllData(String hql);
	public YDData findServiceByDid(String did);
	public List<YDData> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<YDData> findData( String hql,String[] paras);
	public Long getTotal(String hql,List<Object>paras);
	public boolean deletById(int did);
	public List<YDData> findDataByList( String hql,List paras);
	public YDData findServiceBydUrl(String dUrl);
}
