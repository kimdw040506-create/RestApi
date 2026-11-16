package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(long id);

    User create(UserRequest request);

    User update(long id,UserRequest request);

    void delete(long id);
}