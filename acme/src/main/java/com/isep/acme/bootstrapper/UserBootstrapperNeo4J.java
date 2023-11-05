package com.isep.acme.bootstrapper;

import com.isep.acme.model.Role;
import com.isep.acme.model.User;
import com.isep.acme.repositories.neo4j.UserRepositoryNeo4J;
import com.isep.acme.utils.RandomLongGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@EnableNeo4jRepositories("com.isep.acme.repositories.neo4j")
public class UserBootstrapperNeo4J implements CommandLineRunner{
    @Autowired
    private UserRepositoryNeo4J userRepositoryNeo4J;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepositoryNeo4J.findByUsername("admin1@mail.com").isEmpty()) {
            User admin1 = new User("admin1@mail.com", encoder.encode("AdminPW1"),
                    "Jose Antonio", "355489123", "Rua Um");
            admin1.addAuthority(new Role(Role.Admin));

            admin1.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(admin1);
        }

        if (userRepositoryNeo4J.findByUsername("admin2@mail.com").isEmpty()) {
            User mod1 = new User("admin2@mail.com", encoder.encode("AdminPW2"),
                    "Antonio Jose", "321984553", "Rua dois");
            mod1.addAuthority(new Role(Role.Mod));

            mod1.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(mod1);
        }
        if (userRepositoryNeo4J.findByUsername("user1@mail.com").isEmpty()) {
            User user1 = new User("user1@mail.com", encoder.encode("userPW1"),
                    "Nuno Miguel", "253647883", "Rua tres");
            user1.addAuthority(new Role(Role.RegisteredUser));

            user1.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user1);
        }
        if (userRepositoryNeo4J.findByUsername("user2@mail.com").isEmpty()) {
            User user2 = new User("user2@mail.com", encoder.encode("userPW2"),
                    "Miguel Nuno", "253698854", "Rua quatro");
            user2.addAuthority(new Role(Role.RegisteredUser));

            user2.setUserId(RandomLongGenerator.generateRandomLong());


            userRepositoryNeo4J.saveUser(user2);
        }
        if (userRepositoryNeo4J.findByUsername("user3@mail.com").isEmpty()) {
            User user3 = new User("user3@mail.com", encoder.encode("userPW3"),
                    "Antonio Pedro", "254148863", "Rua vinte");
            user3.addAuthority(new Role(Role.RegisteredUser));

            user3.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user3);
        }

        if (userRepositoryNeo4J.findByUsername("user4@mail.com").isEmpty()) {
            User user4 = new User("user4@mail.com", encoder.encode("userPW4"),
                    "Pedro Antonio", "452369871", "Rua cinco");
            user4.addAuthority(new Role(Role.RegisteredUser));

            user4.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user4);
        }
        if (userRepositoryNeo4J.findByUsername("user5@mail.com").isEmpty()) {
            User user5 = new User("user5@mail.com", encoder.encode("userPW5"),
                    "Ricardo Joao", "452858596", "Rua seis");
            user5.addAuthority(new Role(Role.RegisteredUser));

            user5.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user5);
        }
        if (userRepositoryNeo4J.findByUsername("user6@mail.com").isEmpty()) {
            User user6 = new User("user6@mail.com", encoder.encode("userPW6"),
                    "Joao Ricardo", "425364781", "Rua sete");
            user6.addAuthority(new Role(Role.RegisteredUser));

            user6.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user6);
        }
        if (userRepositoryNeo4J.findByUsername("user7@mail.com").isEmpty()) {
            User user7 = new User("user7@mail.com", encoder.encode("userPW7"),
                    "Luis Pedro", "526397747", "Rua oito");
            user7.addAuthority(new Role(Role.RegisteredUser));

            user7.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user7);
        }
        if (userRepositoryNeo4J.findByUsername("user8@mail.com").isEmpty()) {
            User user8 = new User("user8@mail.com", encoder.encode("userPW8"),
                    "Pedro Luis", "523689471", "Rua nove ");
            user8.addAuthority(new Role(Role.RegisteredUser));

            user8.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user8);
        }
        if (userRepositoryNeo4J.findByUsername("user9@mail.com").isEmpty()) {
            User user9 = new User("user9@mail.com", encoder.encode("userPW9"),
                    "Marco Antonio", "253148965", "Rua dez");
            user9.addAuthority(new Role(Role.RegisteredUser));

            user9.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user9);
        }
        if (userRepositoryNeo4J.findByUsername("user10@mail.com").isEmpty()) {
            User user10 = new User("user10@mail.com", encoder.encode("userPW10"),
                    "Antonio Marco", "201023056", "Rua onze");
            user10.addAuthority(new Role(Role.RegisteredUser));

            user10.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user10);
        }
        if (userRepositoryNeo4J.findByUsername("user11@mail.com").isEmpty()) {
            User user11 = new User("user11@mail.com", encoder.encode("userPW11"),
                    "Rui Ricardo", "748526326", "Rua doze");
            user11.addAuthority(new Role(Role.RegisteredUser));

            user11.setUserId(RandomLongGenerator.generateRandomLong());

            userRepositoryNeo4J.saveUser(user11);
        }
    }
}
