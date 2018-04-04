package com.caijia.ad.account.repository;

import com.caijia.ad.account.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,Integer> {

}
