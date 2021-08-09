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
// Виділяєм сразу юзеріів в мапінгу классу @RequestMapping("/api/users")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    
    //Тобі вже цей бін тут не потрібен.
    @Autowired
    UserRepo userRepo;

    @GetMapping(value = "/users")
    public List<UserDTO> users(){
        //назва методу users - ні про що не говорить.
        // методи завжди мають бути  дієсловами.
        List<UserDTO> users = userService.users();
        return users;
    }
    //використовуй множину коли пишеш маппінги (не user, а users)
    @PostMapping(value = "/user")
    @ResponseStatus(HttpStatus.OK)
    public void add(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
    }
        //тут теж множину

    @DeleteMapping(value = "/user/{id}")
    public void delete (@PathVariable Long id){
        userService.delete(id);
    }
    //update - redunant, якщо ми використовуємо PUT http method - то це і так зрозуміло, що ми будемо оновлювати entity.
    @PutMapping(value = "/user/update/{id}")
    public void update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userService.update(id, userDTO);
    }
    //don't fetch all data from db if you want to filter it after.
    // Ти можеш дістати вже відфільтровані данні з бд, так буде використано менше пам'яті і менше данних передаватиметься через сетку.
    @GetMapping(value = "/filter")
    public UserContainer filterByAge(@RequestParam Integer a, @RequestParam Integer b){
        List<User> list = userRepo.findAll();

        list.stream().filter(x -> x.getAge() > a && x.getAge() < b);

        UserContainer userListContainer = new UserContainer();
        userListContainer.setList(list);

        return  userListContainer;
    }
}
