package acme.persistance.neo4j;


import acme.model.Role;
import acme.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Node("UserNeo4J")
public class UserNeo4J {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private String username;

    private String password;

    private String fullName;

    @Relationship(type = "HAS_ROLE")
    private Set<Role> authorities = new HashSet<>();

    private String nif;

    private int roleU;

    private String morada;

    public UserNeo4J() {

    }

    public UserNeo4J(final Long userId, final String username, final String password, final String fullName, final String nif, final String morada) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        setNif(nif);
        this.morada = morada;
    }

    public User toUser() {
        return new User(this.userId, this.username, this.password, this.fullName, this.nif, this.morada, this.authorities, this.roleU);
    }
}
