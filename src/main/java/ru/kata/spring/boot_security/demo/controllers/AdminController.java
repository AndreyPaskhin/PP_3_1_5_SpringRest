package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;

import ru.kata.spring.boot_security.demo.services.UserService;


import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping("")
    public String adminPage(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("allUsers", users);
        return "admin";
    }
    @GetMapping("new")
    public String addUser(@ModelAttribute("user") User user, Model model) {
//        User user = new User();
//        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRole());
        return "add-new-user";
    }
    @PostMapping("save")
    public String saveUser(@ModelAttribute ("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles",roleService.getAllRole());
            return "add-new-user";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }
    @GetMapping("/edit")
    public String editUser(@RequestParam ("id") Long id, Model model) {
        model.addAttribute("user", userService.showUser(id));
        model.addAttribute("allRoles", roleService.getAllRole());
        return "edit-user";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute ("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles",roleService.getAllRole());
            return "edit-user";
        }
        userService.update(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
