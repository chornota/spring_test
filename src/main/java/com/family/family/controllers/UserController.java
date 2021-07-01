package com.family.family.controllers;

import com.family.family.model.User;
import com.family.family.container.UserContainer;
import com.family.family.repo.UserRepo;
import com.family.family.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping(value = "/get_all_users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserContainer getAllUsers(){
        List<User> userList = userRepo.findAll();
        UserContainer userListContainer = new UserContainer();
        userListContainer.setList(userList);
        return  userListContainer;
    }

    @PostMapping(value = "/new_user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserResponse addNew(@RequestBody User user){
        UserResponse userRequest = new UserResponse();
        User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getAge());

        if (!newUser.getFirstName().isEmpty()){
            userRequest.setStatus("OK");
            userRepo.save(newUser);
        }else {
            userRequest.setStatus("Fall");
        }
        return userRequest;
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public UserResponse deleteCurrentUser(@PathVariable Long id){
        UserResponse userRequest = new UserResponse();
        userRepo.deleteById(id);

        if (!userRepo.existsById(id)){
            userRequest.setStatus("OK");
        }else {
            userRequest.setStatus("Fall");
        }
        return  userRequest;
    }

    @PutMapping(value = "/update_user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserResponse updateUser(@PathVariable Long id, @RequestBody User user){
        UserResponse userRequest = new UserResponse();
        Optional<User> updateUser = userRepo.findById(id);

        user.setId(id);

        if (!updateUser.isPresent()){
            userRequest.setStatus("Fall");
            return userRequest;
        }

        userRequest.setStatus("OK");
        userRepo.save(user);

        return userRequest;
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserContainer filterByAge(@RequestParam Integer a, @RequestParam Integer b){
        Iterable<User> list = userRepo.findAll();

        Stream<User> stream = StreamSupport.stream(list.spliterator(), false);
        stream.filter(x -> x.getAge() > a && x.getAge() < b);
        List<User> filterList = stream.collect(Collectors.toList());

        UserContainer userListContainer = new UserContainer();
        userListContainer.setList(filterList);

        return  userListContainer;
    }
}
