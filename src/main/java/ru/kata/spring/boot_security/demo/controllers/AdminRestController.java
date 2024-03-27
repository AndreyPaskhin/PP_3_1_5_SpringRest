package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.exception.UserValidationException;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping()
    public ResponseEntity<List<User>> showUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User user = userService.showUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid User newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserValidationException(bindingResult);
        }
        userService.addUser(newUser);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PatchMapping("/update{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid User updateUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserValidationException(bindingResult);
        }
        userService.update(updateUser);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/delete{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRole();
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.userById(id);
        return ResponseEntity.ok(role);
    }


}
