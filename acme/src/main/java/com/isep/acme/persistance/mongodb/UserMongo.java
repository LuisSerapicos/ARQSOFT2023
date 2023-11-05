package com.isep.acme.persistance.mongodb;


import com.isep.acme.model.Role;
import com.isep.acme.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

import static com.isep.acme.utils.Generator.generateLongID;


@Document(collection = "user")
@Data
@NoArgsConstructor
public class UserMongo{
    @Field(value = "userId")
    private Long userId;
    @Field(value = "username")
    private String username;
    @Field(value = "password")
    private String password;
    @Field(value = "fullName")
    private String fullName;

    @Field(value = "authorities")
    private Set<Role> authorities = new HashSet<>();

    @Field(write = Field.Write.ALWAYS)
    private String nif;

    @Field(write = Field.Write.ALWAYS)
    private String morada;

    public UserMongo(final String username, final String password){
        this.userId = generateLongID();
        this.username = username;
        this.password = password;
    }

    public UserMongo(final String username, final String password, final String fullName, final String nif, final String morada) {
        this.userId = generateLongID();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        setNif(nif);
        this.morada = morada;
    }

    public UserMongo(Long userId, String username, String password, String fullName, Set<Role> authorities, String nif, String morada) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.authorities = authorities;
        this.nif = nif;
        this.morada = morada;
    }

    public void setNif(String nif) {
        if(nif.length() != 9) {
            throw new IllegalArgumentException("NIF must be 9 characters.");
        }
        this.nif = nif;
    }

    public User toUser(){
        return new User(this.userId, this.username, this.password, this.fullName, this.nif,this.morada,this.authorities);
    }

    public void addAuthority(Role r) {
        authorities.add(r);
    }

    public Set<Role> getAuthorities() {


        return authorities;
    }
}
