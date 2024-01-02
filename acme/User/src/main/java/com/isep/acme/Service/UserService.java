package com.isep.acme.Service;


import com.isep.acme.Model.User;
import com.isep.acme.Model.UserView;
import com.isep.acme.Model.UserViewMapper;
import com.isep.acme.Repositories.databases.UserDataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {


    private final UserDataBase userDataBase;

    private final UserViewMapper userViewMapper;

    @Autowired
    public UserService(@Value("${user.interface.default}") String beanName2 , ApplicationContext context, UserViewMapper userViewMapper) {
        this.userViewMapper = userViewMapper;
        this.userDataBase = context.getBean(beanName2, UserDataBase.class);
    }

    public <S extends User> S saveUser(S entity) {
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userDataBase.saveUser(entity);
        return null;
    }

    public User getUser(final String userId){
        return userDataBase.findByUsername(userId).get();
    }

    public Optional<User> getUserId(Long user) {
        return userDataBase.findById(user);
    }
}
