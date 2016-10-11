package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.ServiceType;

public interface ServiceTypeInter {
	public void saveServiceType( ServiceType serviceType);
	public void updateServiceType(ServiceType serviceType);
	public ServiceType getServiceTypeById(Class<ServiceType> cl,Serializable id);
	public List<ServiceType> findAllServiceType(String hql);
	public List<ServiceType> findByFenyeServiceType(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<ServiceType> findServiceType( String hql,String[] paras);
	public Long getServiceTypeTotal(String hql,List<Object>paras);
	public ServiceType  findByName(String name);
	public boolean deletById(int uid);
}
