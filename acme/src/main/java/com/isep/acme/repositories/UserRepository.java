package com.isep.acme.repositories;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.User;
import com.isep.acme.persistance.mongodb.UserMongo;
import com.isep.acme.repositories.databases.UserDataBase;
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
    @Cacheable
    public Optional<User> findById(Long userId){
     return null;
    }

    @Cacheable
    public User getById(final Long userId){
        final Optional<User> optionalUser = findById(userId);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException(User.class, userId);
        }
        if (!optionalUser.get().isEnabled()) {
            throw new ResourceNotFoundException(User.class, userId);
        }
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