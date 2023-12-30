package com.isep.acme.Model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

import static com.isep.acme.utils.Generator.generateLongID;

@Entity
@Getter
@Setter
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long userId;

    @Column(unique = true)
    @Email
    private String username;

    private String password;

    private String fullName;

    private int roleU;

    @Column(nullable = false, unique = true)
    private String nif;

    @Column(nullable = false)
    private String morada;

/*    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<Review>(); */

    protected User() {
    }

    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long userId, final String username, final String password, final String fullName, final String nif, final String morada, Set<Role> role, int roleU) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        setNif(nif);
        this.morada = morada;
        this.roleU = roleU;
    }

    public User(final String username, final String password, final String fullName, final String nif, final String morada) {
        this.userId = generateLongID();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        setNif(nif);
        this.morada = morada;
    }

    public User(final Long userId, final String username, final String password, final String fullName, final String nif, final String morada) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        setNif(nif);
        this.morada = morada;
    }

    public void setNif(String nif) {
        if (nif.length() != 9) {
            throw new IllegalArgumentException("NIF must be 9 characters.");
        }
        this.nif = nif;
    }


    public Long getUserId() {
        return userId;
    }


    public String getFullName() {
        return fullName;
    }


    public String getNif() {
        return nif;
    }

    public String getMorada() {
        return morada;
    }
}

