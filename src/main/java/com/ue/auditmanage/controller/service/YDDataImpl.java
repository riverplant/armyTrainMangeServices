package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.YDData;


public class YDDataImpl implements YDDataInter {
	@Resource
	private BaseDaoInter<YDData> baseDao;

	public BaseDaoInter<YDData> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<YDData> baseDao) {
		this.baseDao = baseDao;
	}
	@Override
	public void save(YDData ydData) {
		baseDao.save(ydData);

	}

	@Override
	public boolean deletById(int uid , String hql) {
		
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return(query.executeUpdate()>0);
	}

	@Override
	public void update(YDData ydData) {
		
		baseDao.update(ydData);
	}

	@Override
	public YDData getObject(Class<YDData> cl, Serializable id) {
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<YDData> findAllData(String hql) {
		return baseDao.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YDData> findDataByFenye(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YDData> findData(String hql, String[] paras) {
		return (List<YDData>) baseDao.findObject(hql,paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql,paras);
	}

	@Override
	public YDData findServiceByDid(String did) {
		String hql = "from YDData d where d.did =?";
		String[] paras = {did};
		YDData data = baseDao.findObject(hql, paras);
		return data!=null ? data : null;
	}
	
	@Override
	public YDData findServiceBydUrl(String dUrl) {
		String hql = "from YDData d where d.dUrl =?";
		String[] paras = {dUrl};
		YDData data = baseDao.findObject(hql, paras);
		return data!=null ? data : null;
	}

	@Override
	public boolean deletById(int did) {
		String hql = "delete from YDData p where p.id = ? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, did);
		
		return(query.executeUpdate()>0);
	}

	@Override
	public List<YDData> findDataByList(String hql, List paras) {
		
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

}
