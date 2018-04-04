package com.caijia.ad.account.repository;

import com.caijia.ad.account.entities.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<TestEntity,Integer> {

    TestEntity findByName(String name);
}
