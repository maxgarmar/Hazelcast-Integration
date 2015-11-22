package com.drive.data.repository.jpa;

import java.util.List;

/**
 * 
 */

public interface HibernateService  {
	
	
	/**
	 * Pagination Controller
	 */
	Long getCountGradeAll(Class<?> c);
	
	List<?> findGradePagination (String clas,int page);
	
	
	
}
