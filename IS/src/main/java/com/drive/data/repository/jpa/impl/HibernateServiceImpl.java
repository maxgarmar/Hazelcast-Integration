package com.drive.data.repository.jpa.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.hibernate.Query;
import org.hibernate.Session;

import org.hibernate.criterion.Projections;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.drive.data.repository.jpa.HibernateService;
import com.drive.data.repository.jpa.UserJpaRepository;

@Component
@Transactional
@Repository
public class HibernateServiceImpl implements HibernateService {

	private EntityManager entityManager;

	@Resource
	private UserJpaRepository userJpaRepository;
	

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getCountGradeAll(Class clas) {
		// TODO Auto-generated method stub
		Session session = (Session) getEntityManager().getDelegate();
		Number n = (Number) session.createCriteria(clas).setProjection(Projections.rowCount()).uniqueResult();

		Long result = n.longValue();

		return result;
	}

	@Override
	public List<?> findGradePagination(String clas, int page) {
		// TODO Auto-generated method stub
		Session session = (Session) getEntityManager().getDelegate();
		Query q = session.createQuery("from " + clas);
		q.setFirstResult((page - 1) * 10);
		q.setMaxResults(10);

		return q.list();

	}

}
