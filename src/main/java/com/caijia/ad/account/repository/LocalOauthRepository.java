package com.caijia.ad.account.repository;

import com.caijia.ad.account.entities.LocalOauthEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LocalOauthRepository extends CrudRepository<LocalOauthEntity, Integer> {

    LocalOauthEntity findByUsernameAndPassword(String username, String password);

    @Query(value = "select o.* from local_oauth o where o.username = ?1 and o.password = ?2", nativeQuery = true)
    LocalOauthEntity findUser(String username, String password);

}
