package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.DataType;

public class DataTypeImpl implements DataTypeInter {
	@Resource
	private BaseDaoInter<DataType> baseDao;
	
	public BaseDaoInter<DataType> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<DataType> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(DataType dataType) {
		
		baseDao.save(dataType);
	}

	@Override
	public boolean deletById(int id, String hql) {
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		return(query.executeUpdate()>0);
	}

	@Override
	public void update(DataType dataType) {
		baseDao.update(dataType);

	}

	@Override
	public DataType getObject(Class<DataType> cl, Serializable id) {
		// TODO Auto-generated method stub
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<DataType> findAllData(String hql) {
		// TODO Auto-generated method stub
		return baseDao.find(hql);
	}

	@Override
	public DataType findServiceByName(String name) {
		String hql = "from DataType dt where dt.name =?";
		String[] paras = {name};
		DataType data = baseDao.findObject(hql, paras);
		return data!=null ? data : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataType> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataType> findData(String hql, String[] paras) {
		return (List<DataType>) baseDao.findObject(hql,paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras) ;
	}

	@Override
	public boolean deletById(int did) {
		String hql = "delete from DataType p where p.id = ? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, did);
		
		return(query.executeUpdate()>0);
	}

}
