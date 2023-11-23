package com.isep.acme.Model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import lombok.Value;

@Value
public class Role implements GrantedAuthority {

    public static final String Admin = "Admin";

    public static final String Mod = "Mod";

    public static final String RegisteredUser = "RegisteredUser";

    private String authority;

    public Role(String authority) {
        System.out.println("authority"+authority);
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
