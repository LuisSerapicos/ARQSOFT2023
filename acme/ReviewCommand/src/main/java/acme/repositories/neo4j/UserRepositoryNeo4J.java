package acme.repositories.neo4j;

import acme.controllers.ResourceNotFoundException;
import acme.model.User;
import acme.persistance.neo4j.UserNeo4J;
import acme.repositories.databases.UserDataBase;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component("neo4J1")
public class UserRepositoryNeo4J implements UserDataBase {
    private final Session session;

    @Autowired
    public UserRepositoryNeo4J(Session session) {
        this.session = session;
    }

    @Override
    public <S extends User> S saveUser(S entity) {
        UserNeo4J save = toUserNeo4J(entity);

        session.save(save);
        return entity;
    }

    @Override
    public Optional<User> findById(Long userId) {
        String cypherQuery = "MATCH (p:UserNeo4J {userId: $userId}) RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("userId", userId);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            UserNeo4J userNeo4J = (UserNeo4J) row.get("p");
            return Optional.ofNullable((userNeo4J.toUser()));
        }

        return Optional.empty();
    }

    @Override
    public User getById(Long userId) {
        final Optional<User> optionalUser = findById(userId);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException(User.class, userId);
        }

        return optionalUser.get();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String cypherQuery = "MATCH (p:UserNeo4J {username: $username}) RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("username", username);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            UserNeo4J userNeo4J = (UserNeo4J) row.get("p");
            return Optional.ofNullable((userNeo4J.toUser()));
        }

        return Optional.empty();
    }

    public UserNeo4J toUserNeo4J(User user) {
        return new UserNeo4J(user.getUserId(), user.getUsername(), user.getPassword(), user.getFullName(), user.getNif(), user.getMorada());
    }
}
