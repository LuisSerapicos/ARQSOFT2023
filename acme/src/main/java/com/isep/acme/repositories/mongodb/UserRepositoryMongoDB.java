package com.isep.acme.repositories.mongodb;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.User;
import com.isep.acme.persistance.mongodb.UserMongo;
import com.isep.acme.repositories.databases.UserDataBase;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@CacheConfig(cacheNames = "users")
@Component("mongoDB1")
public class UserRepositoryMongoDB implements UserDataBase {

    private final MongoTemplate mongoTemplate;

    public UserRepositoryMongoDB(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.userId", condition = "#p0.userId != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
    public <S extends User> S saveUser(S entity) {
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        /*if(findByUsername(entity.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }*/
        /*entity.addAuthority(entity.getAuthorities().)*/
        mongoTemplate.save(entity);
        return null;
    }

    @Override
    @Cacheable
    public Optional<User> findById(Long userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        UserMongo user = mongoTemplate.findOne(query, UserMongo.class);
        if (user == null) {
            System.out.println("User don't exist");
            return Optional.empty();
        }
        return Optional.ofNullable(user.toUser());
    }

    @Override
    @Cacheable
    public User getById(Long userId) {
        final Optional<User> optionalUser = findById(userId);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException(User.class, userId);
        }
        if (!optionalUser.get().isEnabled()) {
            throw new ResourceNotFoundException(User.class, userId);
        }
        return optionalUser.get();
    }

    @Override
    @Cacheable
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        UserMongo user = mongoTemplate.findOne(query, UserMongo.class);
        if (user == null) {
            System.out.println("User don't exist");
            return Optional.empty();
        }
        return Optional.ofNullable(user.toUser());
    }

    @Override
    public UserMongo toUserMongo(User user) {
        return new UserMongo(user.getUserId(), user.getUsername(), user.getPassword(),user.getFullName(),user.getAuthorities(),user.getNif(),user.getMorada());
    }
}
