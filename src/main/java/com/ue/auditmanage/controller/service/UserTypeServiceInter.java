package com.ue.auditmanage.controller.service;

import java.io.Serializable;
import java.util.List;

import entity.UserType;

public interface UserTypeServiceInter {
	public void saveUserType( UserType userType);
	public boolean deletUserTypeById(int id,String hql);
	public void updateUserType(UserType userType);
	public UserType getUserTypeById(Class<UserType> cl,Serializable id);
	public List<UserType> findAllUserType(String hql);
	public List<UserType> findByFenyeUserType(String hql, @SuppressWarnings("rawtypes") List paras,int page,int rows);
	public List<UserType> findUserType( String hql,String[] paras);
	public Long getUserTypeTotal(String hql,List<Object>paras);
	public UserType  findByName(String name);
	public boolean deletById(int uid);
}
