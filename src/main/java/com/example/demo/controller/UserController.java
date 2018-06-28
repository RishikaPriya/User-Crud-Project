package com.example.demo.controller;

import com.example.demo.dao.Response;
import com.example.demo.dao.Status;
import com.example.demo.dao.User;
import com.example.demo.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.demo.constants.Constants.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @ApiOperation(value = "This method is used to insert single user record in database.")
    @PostMapping("/insert")
    public Response insertUser(@RequestBody User user){
        Response response = new Response();
        try{
            log.info("Inserting single user info");
            response.setUsers(userRepository.save(user));
            response.setStatus(new Status(SUCCESSFUL, INSERT_SUCCESS, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);
        }
        return response;
    }

    @ApiOperation(value = "This method is used to insert multiple user record in database.")
    @PostMapping("/insertAll")
    public Response insertAllUser(@RequestBody Iterable<User> users){
        Response response = new Response();
        try{
            log.info("Insert multiple user info.");
            response.setUsers(userRepository.saveAll(users));
            response.setStatus(new Status(SUCCESSFUL, INSERT_SUCCESS, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);

        }
        return response;
    }

    @ApiOperation(value = "This method is used to update single user record in database.")
    @PostMapping("/{id}/update")
    public Response updateUser(@PathVariable("id") int id, @RequestBody User user){
        Response response = new Response();
        try {
            log.info("Update user info");
            User oldUser = userRepository.findById(id).get();
            oldUser.setEmail(user.getEmail());
            oldUser.setName(user.getName());

            response.setUsers(userRepository.save(oldUser));
            response.setStatus(new Status(SUCCESSFUL, UPDATE_SUCCESS, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);

        }catch (NoSuchElementException exception){
            return getUserIdNotFoundResponse(id, response, exception);

        }
        return response;
    }

    @ApiOperation(value = "This method is used to update multiple user record in database.")
    @PostMapping("/updateAll")
    public Response updateMultipleUser(@RequestBody Iterable<User> users){
        Response response = new Response();
        int id = -1;
        try {
            log.info("Update user info");
            List<User> updatedUser = new ArrayList<>();
            for (User user: users) {
                id = user.getId();
                User oldUser = userRepository.findById(id).get();
                oldUser.setEmail(user.getEmail());
                oldUser.setName(user.getName());
                updatedUser.add(oldUser);
            }

            response.setUsers(userRepository.saveAll(updatedUser));
            response.setStatus(new Status(SUCCESSFUL, UPDATE_SUCCESS, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);

        }catch (NoSuchElementException exception){
            return getUserIdNotFoundResponse(id, response, exception);

        }
        return response;
    }

    @ApiOperation(value = "This method is used to delete single user record in database.")
    @PostMapping("/{id}/delete")
    public Response deleteUser(@PathVariable("id") int id){
        Response response = new Response();
        try{
            log.info("Delete user info");
            User user = userRepository.findById(id).get();
            userRepository.delete(user);

            response.setUsers("UserId: " + id + " deleted successfully");
            response.setStatus(new Status(SUCCESSFUL, DELETE_SUCCESS, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);

        }catch (NoSuchElementException exception){
            return getUserIdNotFoundResponse(id, response, exception);

        }
        return response;
    }

    @ApiOperation(value = "This method is used to delete multiple user record in database.")
    @PostMapping("/delete")
    public Response deleteAllUsers(){
        Response response = new Response();

        try{
        log.info("Delete all user info");
        userRepository.deleteAll();
        response.setUsers("User deleted successfully");
        response.setStatus(new Status(SUCCESSFUL, DELETE_SUCCESS, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);

        }
        return response;
    }

    @ApiOperation(value = "This method is used to retrieve single user record in database.")
    @GetMapping("/{id}")
    public Response getUser(@PathVariable("id") int id){
        Response response = new Response();

        try{
            log.info("Retrieve user info");
            response.setUsers(userRepository.findById(id).get());
            response.setStatus(new Status(SUCCESSFUL, DATA_FOUND_SUCCESSFULLY, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);

        }catch (NoSuchElementException exception){
            return getUserIdNotFoundResponse(id, response, exception);

        }
        return response;
    }

    @ApiOperation(value = "This method is used to retrieve multiple user record in database.")
    @GetMapping("/getAll")
    public Response getAllUsers(){
        Response response = new Response();

        try {
        log.info("Retrieve all user info");
        response.setUsers(userRepository.findAll());
        response.setStatus(new Status(SUCCESSFUL, DATA_FOUND_SUCCESSFULLY, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);

        }
        return response;
    }

    @ApiOperation(value = "This method is used to retrieve multiple (limit provided in query parameter) user record " +
            "in database bases on name in query parameter.")
    @GetMapping("/getUser")
    public Response getUsers(@RequestParam("limit") int limit, @RequestParam("name") String name){
        Response response = new Response();

        try {
            log.info("Retrieve all user info");
            response.setUsers(userRepository.findUser(name, limit));
            response.setStatus(new Status(SUCCESSFUL, DATA_FOUND_SUCCESSFULLY, null));

        }catch(JDBCConnectionException ex){
            return getJDBCConnectionResponse(response, ex);

        }
        return response;
    }

    private Response getUserIdNotFoundResponse(int id, Response response, NoSuchElementException exception) {
        log.error(ID_NOT_FOUND + getClass(), exception);
        response.setStatus(new Status(UNSUCCESSFUL, ID_NOT_FOUND + ": " + id, FALSE));
        return response;
    }

    private Response getJDBCConnectionResponse(Response response, JDBCConnectionException ex) {
        log.error(getClass() + CONNECTION_REFUSED, ex);
        response.setStatus(new Status(UNSUCCESSFUL, CONNECTION_REFUSED, TRUE));
        return response;
    }
}
