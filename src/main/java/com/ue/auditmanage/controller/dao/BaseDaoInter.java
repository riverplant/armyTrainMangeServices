package com.ue.auditmanage.controller.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

public interface BaseDaoInter<T> {
public  Serializable save( T o);
public List<T> find(String hql);
public void delet(T o );
public void update(T o);
public Query getQuery(String hql);
public List<T> findObjects(String hql, Object[] paras);
public T getObject(Class<T> cl,Serializable id);
//分页显示
public List<T> findByFenye(String hql,List<Object>paras,Integer pages,Integer rows);

public T findObject(String hql, Object[] paras);
public Long getTotal(String hql,List<Object>paras);
public List<T> findBydate(String hql, @SuppressWarnings("rawtypes") List paras);
}
