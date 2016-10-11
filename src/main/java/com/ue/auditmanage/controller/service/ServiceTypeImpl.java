package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.ServiceType;

public class ServiceTypeImpl implements ServiceTypeInter {
	@Resource
	private BaseDaoInter<ServiceType> baseDao;

	public BaseDaoInter<ServiceType> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<ServiceType> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void saveServiceType(ServiceType serviceType) {
		
		baseDao.save(serviceType);
	}


	@Override
	public void updateServiceType(ServiceType serviceType) {
		
		baseDao.update(serviceType);
	}

	@Override
	public ServiceType getServiceTypeById(Class<ServiceType> cl, Serializable id) {
		// TODO Auto-generated method stub
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<ServiceType> findAllServiceType(String hql) {
		return baseDao.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceType> findByFenyeServiceType(String hql, @SuppressWarnings("rawtypes") List paras,
			int page, int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<ServiceType> findServiceType(String hql, String[] paras) {
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getServiceTypeTotal(String hql, List<Object> paras) {
		return baseDao.getTotal(hql,paras);
	}

	@Override
	public ServiceType findByName(String name) {
		String hql = " from ServiceType where name =?";
		String[] para = {name};
		ServiceType serviceType = new ServiceType();
		serviceType =baseDao.findObject(hql, para);
		return serviceType;
	}

	@Override
	public boolean deletById(int id) {
		String hql = "delete from ServiceType p where p.id = ? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		
		return(query.executeUpdate()>0);
	}

}
