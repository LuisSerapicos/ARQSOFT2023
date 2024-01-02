package com.isep.acme.Repositories;


import com.isep.acme.Controller.ResourceNotFoundException;
import com.isep.acme.Model.User;
import com.isep.acme.Model.UserMongo;
import com.isep.acme.Repositories.databases.UserDataBase;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;


@CacheConfig(cacheNames = "users")
@Component("sql1")
public class UserRepository implements UserDataBase {


    @Override
    public <S extends User> S saveUser(S entity) {
        return null;
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.empty();
    }

    @Cacheable
    public User getById(final String userId){
        final Optional<User> optionalUser = findById(userId);

        return optionalUser.get();
    }

    @Cacheable
    public Optional<User> findByUsername(String username){
        return null;
    }

    @Override
    public UserMongo toUserMongo(User user) {
        return null;
    }
}