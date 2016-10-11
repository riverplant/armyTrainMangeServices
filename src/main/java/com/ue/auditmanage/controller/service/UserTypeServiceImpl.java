package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;

import com.ue.auditmanage.controller.dao.BaseDaoInter;

import entity.UserType;

public class UserTypeServiceImpl implements UserTypeServiceInter {
	@Resource
	private BaseDaoInter<UserType> baseDao;

	public BaseDaoInter<UserType> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDaoInter<UserType> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void saveUserType(UserType userType) {
		
		baseDao.save(userType);
	}
	
	@Override
	public boolean deletById(int uid) {
		String hql = "delete from UserType u where u.id=? ";
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, uid);
		return(query.executeUpdate()>0);
		
	}

	@Override
	public boolean deletUserTypeById(int id, String hql) {
		Query query = baseDao.getQuery(hql);
		query.setInteger(0, id);
		return(query.executeUpdate()>0);
	}

	@Override
	public void updateUserType(UserType userType) {
		baseDao.update(userType);

	}

	@Override
	public UserType getUserTypeById(Class<UserType> cl, Serializable id) {
		return baseDao.getObject(UserType.class, id);
	}

	@Override
	public List<UserType> findAllUserType(String hql) {
		return baseDao.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserType> findByFenyeUserType(String hql, @SuppressWarnings("rawtypes") List paras, int page,
			int rows) {

		return baseDao.findByFenye(hql, paras, page, rows);
	}

	@Override
	public List<UserType> findUserType(String hql, String[] paras) {
		// TODO Auto-generated method stub
		return baseDao.findObjects(hql, paras);
	}

	@Override
	public Long getUserTypeTotal(String hql, List<Object> paras) {
		return baseDao.getTotal(hql,paras);
	}

	@Override
	public UserType findByName(String name) {
		String hql = " from UserType where name =?";
		String[] para = {name};
		UserType userType = new UserType();
		userType =baseDao.findObject(hql, para);
		return userType;
	}

}
