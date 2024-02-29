package ru.kata.spring.boot_security.demo.DBInit;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Set;

@Component
public class DBInit {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public DBInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostConstruct
    public void initmethod() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(adminRole);
        }
        if (roleRepository.findByName("ROLE_USER") == null) {
            roleRepository.save(userRole);
        }


        User admin = new User("admin", passwordEncoder.encode("admin"), "admin@mail.ru", 30, roleRepository.findAll());
        User user = new User("user", passwordEncoder.encode("user"), "user@mail.ru", 25, Set.of(userRole));

        if (userRepository.findByUsername("admin") == null) {
            userRepository.save(admin);
        }
        if (userRepository.findByUsername("user") == null) {
            userRepository.save(user);
        }

    }
}
