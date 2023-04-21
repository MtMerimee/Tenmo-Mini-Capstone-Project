package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/tenmo/users")
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path = "/{user_id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceById(@PathVariable("user_id") int userId) {
        BigDecimal balance = userDao.getBalance(userId);
        if (balance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        } else {
            return balance;
        }
    }

    @RequestMapping(path = "/allUsers", method = RequestMethod.GET)
    public List<User> showUsers() {
        Principal principal = null;
        return userDao.listAllUsersInSystem();
    }


    @RequestMapping(path = "/{user_id}/transfer", method = RequestMethod.GET)
    public List<User> showUsersAvailableForTransfer(@PathVariable("user_id") int userId) {
        Principal principal = null;
        return userDao.showUsersAvailableForTransfer(userId);
    }



    @RequestMapping(path = "/id/{userId}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int userId) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        } else {
            return user;
        }
    }

    @RequestMapping(path = "/username/{username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        } else {
            return user;
        }
    }

    //Could put get
}