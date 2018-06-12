package com.example.demo.controller;

import com.example.demo.dao.User;
import com.example.demo.exception.UserException;
import com.example.demo.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.demo.constants.Constants.ID_NOT_FOUND;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @ApiOperation(value = "This method is used to insert single user record in database.")
    @PostMapping("/insert")
    public User insertUser(@RequestBody User user){
        log.info("Inserting single user info");
        return userRepository.save(user);
    }

    @ApiOperation(value = "This method is used to insert multiple user record in database.")
    @PostMapping("/insertAll")
    public Iterable<User> insertAllUser(@RequestBody Iterable<User> users){
        log.info("Insert multiple user info.");
        return userRepository.saveAll(users);
    }

    @ApiOperation(value = "This method is used to update single user record in database.")
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

    @ApiOperation(value = "This method is used to update multiple user record in database.")
    @PostMapping("/updateAll")
    public List<User> updateMultipleUser(@RequestBody Iterable<User> users){
        try {
            log.info("Update user info");
            List<User> updatedUser = new ArrayList<>();
            for (User user: users) {
                User oldUser = userRepository.findById(user.getId()).get();
                oldUser.setEmail(user.getEmail());
                oldUser.setName(user.getName());
                updatedUser.add(oldUser);
            }
            return  userRepository.saveAll(updatedUser);

        }catch (NoSuchElementException exception){
            log.error(ID_NOT_FOUND + getClass(), exception);
            throw new UserException(ID_NOT_FOUND);
        }
    }

    @ApiOperation(value = "This method is used to delete single user record in database.")
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

    @ApiOperation(value = "This method is used to delete multiple user record in database.")
    @PostMapping("/delete")
    public String deleteAllUsers(){
        log.info("Delete all user info");
        userRepository.deleteAll();
        return "All users deleted successfully";
    }

    @ApiOperation(value = "This method is used to retrieve single user record in database.")
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

    @ApiOperation(value = "This method is used to retrieve multiple user record in database.")
    @GetMapping("/getAll")
    public Iterable<User> getAllUsers(){
        log.info("Retrieve all user info");
        return userRepository.findAll();
    }
}
