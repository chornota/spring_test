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
// @Controller використовується в SpringMVC і там за замовчуванням треба повертати назву view (файла, на який перекидати після обробки запиту)
// якщо ти пишеш Rest - заміни анотацію @Controller на @RestController, так не треба буде над кожним методом писати анотацію @ResponseBody.
// Додай анотацію @RequestMapping("BaseURL") - де baseURL - базовий урл до всіх запитів твого контролера наприклад: @RequestMapping("/api/users")



/*
    Архітектура
    
    Додай додатковий слой UserService і UserService вже буде звертатися до UserRepository (не треба напряму з контролера викликати репозиторій, прибери його звідси)
    
    Нехай твої методи повертатимуть ResponseEntity, там будеш просто передавати body відповіді
*/

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;
    // не пиши дієслова в мапінгах get запит і так значить, що ти щось отримаєш
    // я переписав би його просто без мапінга
    //Анотація контроллер використовується в springMVC 

    @GetMapping(value = "/get_all_users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserContainer getAllUsers(){
        List<User> userList = userRepo.findAll();
        UserContainer userListContainer = new UserContainer();
        userListContainer.setList(userList);
        return  userListContainer;
    }
    
    
    
    
    //Я не бачив, щоб використовували нижнє підкреслювання в  _ в path naming
    
    //
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
    //норм практика це додати поле
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

    //Фільтрувати треба відразу в бд почитай про анотацію @Query  і hql за І
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
