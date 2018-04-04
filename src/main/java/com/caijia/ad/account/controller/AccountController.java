package com.caijia.ad.account.controller;

import com.caijia.ad.account.entities.LocalOauthEntity;
import com.caijia.ad.account.entities.UserEntity;
import com.caijia.ad.account.repository.LocalOauthRepository;
import com.caijia.ad.account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AccountController {

    private final LocalOauthRepository localOauthRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountController(LocalOauthRepository localOauthRepository, UserRepository userRepository) {
        this.localOauthRepository = localOauthRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping("/login")
    public @ResponseBody
    UserEntity login(@RequestParam(value = "username") String username,
                     @RequestParam(value = "password") String password) {
        LocalOauthEntity localOauthEntity = localOauthRepository.findUser(username, password);
        if (localOauthEntity != null) {
            Optional<UserEntity> userEntity = userRepository.findById(localOauthEntity.getUserId());
            if (userEntity.isPresent()) {
                return userEntity.get();
            }
        }
        return null;
    }
}
