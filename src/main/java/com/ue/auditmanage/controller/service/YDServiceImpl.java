package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.YDService;

public class YDServiceImpl implements YDServiceInter {
	@Resource
	private BaseDaoInter<YDService> baseDao;

	public BaseDaoInter<YDService> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<YDService> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(YDService ydService) {
		// TODO Auto-generated method stub
		baseDao.save(ydService);
	}

	@Override
	public boolean deletById(int uid, String hql) {

		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return (query.executeUpdate() > 0);
	}

	@Override
	public void update(YDService ydService) {
		baseDao.update(ydService);

	}

	@Override
	public YDService getObject(Class<YDService> cl, Serializable id) {
		return baseDao.getObject(cl, id);
	}

	@Override
	public List<YDService> findAllService(String hql) {

		return baseDao.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YDService> findServiceByFenye(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {
		// TODO Auto-generated method stub
		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YDService> findService(String hql, String[] paras) {
		return (List<YDService>) baseDao.findObject(hql, paras);
	}

	@Override
	public Long getTotal(String hql, List<Object> paras) {
		// TODO Auto-generated method stub
		return baseDao.getTotal(hql, paras);
	}

	@Override
	public YDService findServiceBySid(String sid) {
		String hql = "from YDService s where s.sid =?";
		String[] paras = { sid };
		YDService service = baseDao.findObject(hql, paras);
		return service != null ? service : null;

	}

	@Override
	public YDService findServiceByUrl(String url) {
		String hql = "from YDService s where s.surl =?";
		String[] paras = { url };
		YDService service = baseDao.findObject(hql, paras);
		return service != null ? service : null;

	}

	@Override
	public boolean deletBySid(int id) {
		String hql = "delete from YDService p where p.id = ? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);

		return (query.executeUpdate() > 0);
	}

	@Override
	public List<YDService> findServiceByList(String hql, List paras) {
		return baseDao.findBydate(hql, paras)!=null?baseDao.findBydate(hql, paras):null;
	}

}
