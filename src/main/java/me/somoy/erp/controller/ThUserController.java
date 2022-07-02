package me.somoy.erp.controller;

import me.somoy.erp.model.user.CurrentUser;
import me.somoy.erp.model.user.User;
import me.somoy.erp.service.UserService;
import me.somoy.erp.utils.exception.AccessLevelLowException;
import me.somoy.erp.utils.exception.EmployeeAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ThUserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model, @CurrentUser User loggedInUser){
        List<User> userList = userService.getRepository().findAll();
        model.addAttribute("userList", userList);
        if(loggedInUser != null){
            model.addAttribute("user", loggedInUser);
        }
        return "index";
    }

    @RequestMapping("/login")
    public String login(@CurrentUser User loggedInUser){
        if(loggedInUser != null){
            return "index";
        }
        return "login";
    }

    @GetMapping("/addUser")
    public String addUser(Model model){
        model.addAttribute(new User());
        return "addUser";
    }
    @PostMapping("/addUser")
    public String addUser( @ModelAttribute("user") User user, @CurrentUser User loggedInUser) throws EmployeeAlreadyExistsException, AccessLevelLowException {
        if(loggedInUser != null){
            if(loggedInUser.getAccessLevel().equalsIgnoreCase("Employee")
                    && user.getAccessLevel().equalsIgnoreCase("Admin")){
                throw new AccessLevelLowException();
            }
        }
        userService.save(user);
        return "index";
    }

    @GetMapping ("/edit/{id}")
    public String getUserToEdit(Model model, @PathVariable("id") String id, @CurrentUser User loggedInUser) throws AccessLevelLowException {
        User user = userService.getUserById(Long.parseLong(id));
        model.addAttribute("user", user);
        if(loggedInUser != null){
            if(loggedInUser.getAccessLevel().equalsIgnoreCase("Employee")
                && user.getAccessLevel().equalsIgnoreCase("Admin")){
            throw new AccessLevelLowException();
            }
        }
        return "edit";
    }


    @PostMapping("/edit")
    public String editUser(Model model, @ModelAttribute("user") User user, @CurrentUser User loggedInUser){
        System.out.println(user.getUsername());
        User user1 = userService.getRepository().findByUsername(user.getUsername());
        if(user1 != null){
            user1.setUsername(user.getUsername());
            user1.setDateOfJoining(user.getDateOfJoining());
            user1.setAddress(user.getAddress());
            user1.setEmail(user.getEmail());
        }
        userService.getRepository().save(user1);
        return "index";
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(Model model, @PathVariable("id") String id, @CurrentUser User loggedInUser) throws AccessLevelLowException {
        User user = userService.getUserById(Long.parseLong(id));
        if(loggedInUser != null){
            if(loggedInUser.getAccessLevel().equalsIgnoreCase("Employee")
                    && user.getAccessLevel().equalsIgnoreCase("Admin")){
                throw new AccessLevelLowException();
            }
        }
        userService.getRepository().delete(user);
        return "index";
    }



}
