package example.controller;

import example.entity.User;
import example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        logger.info("All users displayed");
        return "users";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("user", new User());
        logger.info("Add user form displayed");
        return "user-edit";
    }

    @PostMapping("/edit")
    public String createOrUpdateUser(@Valid @ModelAttribute User user) {
        userService.saveUser(user);
        logger.info("User created/updated: {}", user);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        Optional<User> userOpt = Optional.ofNullable(userService.getById(id));
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            logger.info("Edit form displayed for user id: {}", id);
            return "user-edit";
        } else {
            logger.warn("User with id {} not found", id);
            return "redirect:/";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        Optional<User> userOpt = Optional.ofNullable(userService.getById(id));
        if (userOpt.isPresent()) {
            userService.delete(id);
            logger.info("User with id {} deleted", id);
        } else {
            logger.warn("User with id {} not found, deletion aborted", id);
        }
        return "redirect:/";
    }
}

