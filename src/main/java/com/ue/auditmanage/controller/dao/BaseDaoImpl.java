package com.ue.auditmanage.controller.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BaseDaoImpl<T> implements BaseDaoInter<T> {
	/**
	 * 注入SessionFactory
	 */
	private SessionFactory sessionfactory;

	public SessionFactory getSessionfactory() {
		return sessionfactory;
	}

	public void setSessionfactory(SessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	/**
	 * 得到当前session
	 * 
	 * @return
	 */
	private Session getCurrenSession() {
		return sessionfactory.getCurrentSession();
	}

	@Override
	public Serializable save(T o) {
		return this.getCurrenSession().save(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql) {

		return this.getCurrenSession().createQuery(hql).list();
	}

	@Override
	public void delet(T o) {
		this.getCurrenSession().delete(o);

	}

	@Override
	public void update(T o) {
		this.getCurrenSession().update(o);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByFenye(String hql, List<Object> paras, Integer pages,
			Integer rows) {
		if (pages == null || pages < 1)
			pages = 1;
		if (rows == null || rows < 1)
			rows = 1;
		Query query = this.getCurrenSession().createQuery(hql);
		if (paras != null && paras.size() > 0) {
			for (int i = 0; i < paras.size(); i++) {
				query.setParameter(i, paras.get(i));
			}

		}
		
		return query.setFirstResult((pages - 1) * rows).setMaxResults(rows)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject(Class<T> cl, Serializable id) {

		return (T) this.getCurrenSession().get(cl, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findObject(String hql, Object[] paras) {
		List<T> objs = null;
		Query query = this.getCurrenSession().createQuery(hql);
		if (paras != null && paras.length > 0) {
			for (int i = 0; i < paras.length; i++) {
				query.setParameter(i, paras[i]);
			}}
		objs = query.list();
		if ((objs != null && objs.size() > 0)) {
			return objs.get(0);
		} else {
			return null;
		}

	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findObjects(String hql, Object[] paras) {
		List<T> objs = null;
		Query query = this.getCurrenSession().createQuery(hql);
		if (paras != null && paras.length > 0) {
			for (int i = 0; i < paras.length; i++) {
				query.setParameter(i, paras[i]);
			}}
		objs = query.list();
		if ((objs != null && objs.size() > 0)) {
			return objs;
		} else {
			return null;
		}

	}
	/**
	 * 得到查询总的个数
	 */
	public Long getTotal(String hql,List<Object>paras) {
		Query query = this.getCurrenSession().createQuery(hql);
		if (paras != null && paras.size() > 0) {
			for (int i = 0; i < paras.size(); i++) {
				query.setParameter(i, paras.get(i));
			}}
		System.out.println("查询到="+query .uniqueResult());
		Long total =(Long)query .uniqueResult();
		return total;
	}

	@Override
	public Query getQuery(String hql) {
		// TODO Auto-generated method stub
		return this.getCurrenSession().createQuery(hql);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> findBydate(String hql, List paras) {
		List<T> objs = null;
		Query query = this.getCurrenSession().createQuery(hql);
		if (paras != null && paras.size() > 0) {
			for (int i = 0; i < paras.size(); i++) {
				query.setParameter(i, paras.get(i));
			}}
			objs = query.list();
			if ((objs != null && objs.size() > 0)) {
				return objs;
			} else {
				return null;
			}
		
		
		
	}


	
}
