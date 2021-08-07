package com.family.family.controllers;

import com.family.family.dto.UserDTO;
import com.family.family.model.User;
import com.family.family.container.UserContainer;
import com.family.family.repo.UserRepo;
import com.family.family.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepo userRepo;

    @GetMapping(value = "/users")
    public List<UserDTO> users(){
        List<UserDTO> users = userService.users();
        return users;
    }

    @PostMapping(value = "/user")
    @ResponseStatus(HttpStatus.OK)
    public void add(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
    }

    @DeleteMapping(value = "/user/{id}")
    public void delete (@PathVariable Long id){
        userService.delete(id);
    }

    @PutMapping(value = "/user/update/{id}")
    public void update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userService.update(id, userDTO);
    }

    @GetMapping(value = "/filter")
    public UserContainer filterByAge(@RequestParam Integer a, @RequestParam Integer b){
        List<User> list = userRepo.findAll();

        list.stream().filter(x -> x.getAge() > a && x.getAge() < b);

        UserContainer userListContainer = new UserContainer();
        userListContainer.setList(list);

        return  userListContainer;
    }
}
