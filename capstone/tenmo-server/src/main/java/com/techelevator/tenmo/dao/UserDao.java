package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface UserDao {

    List<User> listAllUsersInSystem();

    User getUserById(int id);

    BigDecimal getBalance (int id);

    List<User> showUsersAvailableForTransfer(int userId);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
