package com.family.family.services;

import com.family.family.dto.UserDTO;
import com.family.family.model.User;
import com.family.family.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public List<UserDTO> users(){
        return convertToDTO(userRepo.findAllUsers());
    }

    public void addUser(UserDTO userDTO) {
        User newUser = new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getAge());
        userRepo.save(newUser);
    }

    public void delete(Long id){
        userRepo.deleteById(id);
    }

    public void update(Long id, UserDTO userDTO){
        userRepo.save(convertToUser(userDTO, id));
    }

    public List<UserDTO> convertToDTO(List<User> list){
        List<UserDTO> userDTO = new ArrayList<>();
        list.stream().forEach(x -> {
            userDTO.add(new UserDTO(x.getId(), x.getFirstName(), x.getLastName(), x.getEmail(), x.getAge()));
        });

        return userDTO;
    }

    public User convertToUser(UserDTO userDTO, Long id){
        Optional<User> findUserById =  userRepo.findById(id);
        User user = new User();
        
        if (findUserById.isPresent()){
            user.setId(id);
            user.setEmail(userDTO.getEmail());
            user.setLastName(userDTO.getLastName());
            user.setFirstName(userDTO.getFirstName());
            user.setAge(userDTO.getAge());
        }
        return user;
    }
}
