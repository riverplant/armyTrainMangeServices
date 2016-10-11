package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.DataType;


public interface DataTypeInter {
	public void save(DataType dataType);
	public boolean deletById(int id,String hql);
	public void update(DataType dataType);
	public DataType getObject(Class<DataType> cl,Serializable id);
	public List<DataType> findAllData(String hql);
	public DataType findServiceByName(String name);
	public List<DataType> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<DataType> findData( String hql,String[] paras);
	public Long getTotal(String hql,List<Object>paras);
	public boolean deletById(int did);
}
