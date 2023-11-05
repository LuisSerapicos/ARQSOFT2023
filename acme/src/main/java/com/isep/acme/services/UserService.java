package com.isep.acme.services;

import com.isep.acme.model.User;
import com.isep.acme.model.UserView;
import com.isep.acme.model.UserViewMapper;
import com.isep.acme.repositories.databases.UserDataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserDataBase userDataBase;

    private final UserViewMapper userViewMapper;

    @Autowired
    public UserService(@Value("${user.interface.default}") String beanName2 , ApplicationContext context, UserViewMapper userViewMapper) {
        this.userViewMapper = userViewMapper;
        this.userDataBase = context.getBean(beanName2, UserDataBase.class);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userDataBase.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }

    public UserView getUser(final Long userId){
        return userViewMapper.toUserView(userDataBase.getById(userId));
    }

    public Optional<User> getUserId(Long user) {
        return userDataBase.findById(user);
    }
}
