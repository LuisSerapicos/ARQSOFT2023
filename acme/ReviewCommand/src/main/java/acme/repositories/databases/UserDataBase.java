package acme.repositories.databases;

import acme.model.User;
import acme.persistance.neo4j.UserNeo4J;

import java.util.Optional;

public interface UserDataBase {

    <S extends User> S saveUser(S entity);

    Optional<User> findById(Long userId);

    User getById(final Long userId);

    Optional<User> findByUsername(String username);

    UserNeo4J toUserNeo4J(User user);
}
