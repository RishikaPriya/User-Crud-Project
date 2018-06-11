package com.example.demo.controller;

import com.example.demo.dao.User;
import com.example.demo.exception.UserException;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

import static com.example.demo.constants.Constants.ID_NOT_FOUND;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/insert")
    public User insertUser(@RequestBody User user){
        log.info("Inserting single user info");
        return userRepository.save(user);
    }

    @PostMapping("/insertAll")
    public Iterable<User> insertAllUser(@RequestBody Iterable<User> users){
        log.info("Insert multiple user info.");
        return userRepository.saveAll(users);
    }

    @PostMapping("/{id}/update")
    public User updateUser(@PathVariable("id") int id, @RequestBody User user){
        try {
            log.info("Update user info");
            User oldUser = userRepository.findById(id).get();
            oldUser.setEmail(user.getEmail());
            oldUser.setName(user.getName());
            return userRepository.save(oldUser);
        }catch (NoSuchElementException exception){
           log.error(ID_NOT_FOUND + getClass(), exception);
           throw new UserException(ID_NOT_FOUND);
        }
    }

   @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") int id){
        try{
        log.info("Delete user info");
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
        return "UserId: " + id + " deleted successfully";
        }catch (NoSuchElementException exception){
            log.error(ID_NOT_FOUND + getClass(), exception);
            throw new UserException(ID_NOT_FOUND);
        }
    }

    @PostMapping("/delete")
    public String deleteAllUsers(){
        log.info("Delete all user info");
        userRepository.deleteAll();
        return "All users deleted successfully";
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int id){
        try{
        log.info("Retrieve user info");
        return userRepository.findById(id).get();
        }catch (NoSuchElementException exception){
            log.error(ID_NOT_FOUND + getClass(), exception);
            throw new UserException(ID_NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public Iterable<User> getAllUsers(){
        log.info("Retrieve all user info");
        return userRepository.findAll();
    }
}
