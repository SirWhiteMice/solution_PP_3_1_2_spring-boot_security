package kata.tkachev.controller;

import kata.tkachev.model.User;
import kata.tkachev.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import kata.tkachev.model.Role;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder("user")
    public void bindUser(WebDataBinder binder) {
        binder.setAllowedFields("id", "name", "age", "email", "password");
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String home(@AuthenticationPrincipal User principal, Model model) {
        User user = userService.getUserById(principal.getId());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));
        return "user";
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/admin/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        return "new";
    }

    @PostMapping("/admin/new")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam List<Long> roleIds) {
        userService.saveUser(user, roleIds);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("selectedRoleIds", user.getRoles().stream().map(Role::getId).toList());
        return "edit";
    }

    @PostMapping("/admin/edit")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam List<Long> roleIds) {
        userService.updateUser(user, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
