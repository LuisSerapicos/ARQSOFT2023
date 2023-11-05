package com.isep.acme.repositories.databases;

import com.isep.acme.model.User;
import com.isep.acme.persistance.mongodb.UserMongo;

import java.util.Optional;

public interface UserDataBase {

    <S extends User> S saveUser(S entity);

    Optional<User> findById(Long userId);

    User getById(final Long userId);

    Optional<User> findByUsername(String username);

    UserMongo toUserMongo(User user);
}
