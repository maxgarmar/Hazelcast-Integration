package com.drive.data.repository.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.drive.bean.jpa.UserEntity;

/**
 * Repository : User.
 */
public interface UserJpaRepository extends PagingAndSortingRepository<UserEntity, Integer> {

}
