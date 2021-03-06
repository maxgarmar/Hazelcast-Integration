/*
 * Created on 20 nov 2015 ( Time 20:44:55 )
 * Generated by Máximo
 */
package com.drive.business.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.drive.bean.User;
import com.drive.bean.jpa.UserEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.drive.business.service.UserService;
import com.drive.business.service.mapping.UserServiceMapper;
import com.drive.data.repository.jpa.UserJpaRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapStore;
import com.hazelcast.query.SqlPredicate;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserService
 */

@Component
@Transactional
public class UserServiceImpl implements UserService {

	@Resource
	private UserJpaRepository userJpaRepository;

	@Resource
	private UserServiceMapper userServiceMapper;

	private HazelcastInstance instance;

	@Override
	public User findById(Integer id) {
		UserEntity userEntity = userJpaRepository.findOne(id);
		return userServiceMapper.mapUserEntityToUser(userEntity);
	}

	@Override
	public List<User> findAll() {

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);

		instance = getHazelCastInstance();

		System.out.println("HAZELCAST STARTED IN FINDALL METHOD: " + strDate);

		List<User> result = new LinkedList<>();
		IMap<Integer,User> cachedList = instance.getMap("users");

		if (cachedList != null && !cachedList.isEmpty()) {

			Date now1 = new Date();
			String strDate1 = sdfDate.format(now1);
			System.out.println("DATA IS ALREADY CASHED AND RETRIEVED FROM HAZELCAST::CONVERTING TO LIST FOR JSP... "+ strDate1);
			
			result.addAll(cachedList.values());
			Date now2 = new Date();
			String strDate2 = sdfDate.format(now2);
			
			System.out.println("DATA CONVERTED TO LIST::TIME TO GET BACK THE CASHED DATA " + strDate2);
			System.out.println("JSP STARTS RENDERING: ");
			
			return result;

		} else {

			System.out.println("DATA IS NOT CASHED: ");

			Date now3 = new Date();
			String strDate3 = sdfDate.format(now3);
			System.out.println("QUERY TO DATABASE STARTED: " + strDate3);
			Iterable<UserEntity> entities = userJpaRepository.findAll();

			Date now4 = new Date();
			String strDate4 = sdfDate.format(now4);
			System.out.println("QUERY TO DATABASE FINISHED: " + strDate4);

			Date now5 = new Date();
			String strDate5 = sdfDate.format(now5);
			System.out.println("LIST::HAZELCAST STARTS LOADING THE DATA IN MEMORY: " + strDate5);

			for (UserEntity userEntity : entities) {
				cachedList.set(userServiceMapper.mapUserEntityToUser(userEntity).getId(),userServiceMapper.mapUserEntityToUser(userEntity));
			}
			

			Date now6 = new Date();
			String strDate6 = sdfDate.format(now6);
			System.out.println("LIST::HAZELCAST ENDS LOADING THE DATA IN MEMORY: " + strDate6);

		}

		result.addAll(cachedList.values());
		System.out.println("JSP STARTS RENDERING: ");
		
		
		return result;
	}

	//method to make the filter to the cache
	@Override
	public List<?> queryFilter(Class<?> c, String field, String join, Object value) {


		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy

		Date now = new Date();
		String strDate = sdfDate.format(now);
		System.out.println("START FILTERING WITH HAZELCAST: " + strDate);
		
		IMap<String,User> cachedList = instance.getMap("users");

		Collection<User> filteredList = (Collection<User>) cachedList.values(new SqlPredicate (field + " LIKE '" + value + "%'"));
		List<User> result = new LinkedList<>();
		
		
		result.addAll(filteredList);
		
		
		Date now2 = new Date();
		String strDate2 = sdfDate.format(now2);
		System.out.println("END FILTERING WITH HAZELCAST: " + strDate2);
		return result;

	}

	private HazelcastInstance getHazelCastInstance() {

		if (instance == null) {
			instance = Hazelcast.newHazelcastInstance();
		}
		return instance;

	}

	@Override
	public User save(User user) {
		return update(user);
	}

	@Override
	public User create(User user) {
		UserEntity userEntity = userJpaRepository.findOne(user.getId());
		if (userEntity != null) {
			throw new IllegalStateException("already.exists");
		}
		userEntity = new UserEntity();
		userServiceMapper.mapUserToUserEntity(user, userEntity);
		UserEntity userEntitySaved = userJpaRepository.save(userEntity);
		return userServiceMapper.mapUserEntityToUser(userEntitySaved);
	}

	@Override
	public User update(User user) {
		UserEntity userEntity = userJpaRepository.findOne(user.getId());
		userServiceMapper.mapUserToUserEntity(user, userEntity);
		UserEntity userEntitySaved = userJpaRepository.save(userEntity);
		return userServiceMapper.mapUserEntityToUser(userEntitySaved);
	}

	@Override
	public void delete(Integer id) {
		userJpaRepository.delete(id);
	}

	public UserJpaRepository getUserJpaRepository() {
		return userJpaRepository;
	}

	public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	public UserServiceMapper getUserServiceMapper() {
		return userServiceMapper;
	}

	public void setUserServiceMapper(UserServiceMapper userServiceMapper) {
		this.userServiceMapper = userServiceMapper;
	}

}
