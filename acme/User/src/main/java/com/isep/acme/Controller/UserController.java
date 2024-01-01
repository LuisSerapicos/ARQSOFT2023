package com.isep.acme.Controller;


import com.isep.acme.Model.User;
import com.isep.acme.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userName}")
    public Boolean getUserRolePM(@PathVariable final String userName) {
        System.out.println(userName);
        User user = userService.getUser(userName);
        if(user.getRoleU()!=2) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"The user is not a Product Manager");
        return true;
    }

    @GetMapping("/user/{username}")
    public User getUserIDRolePM(@PathVariable final String username) {
        System.out.println(username);
        User user = userService.getUser(username);
        if(user.getUsername() == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"The user is not a Product Manager");
        return user;
    }

    /*@GetMapping("/username/{username}")
    public ResponseEntity<UserDetails> create(@PathVariable final String username) {
        UserDetails userDetails = userService.loadUserByUsername(username);

        return ResponseEntity.ok().body(userDetails);
    }*/

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        System.out.println("Post USER"+ user);
        final User userReturn = userService.saveUser(user);
        return new ResponseEntity<User>(userReturn, HttpStatus.CREATED);
    }
}
