package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.splunk.Receiver;
import org.apache.coyote.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private Receiver splunkReceiver;
    @InjectMocks
    private UserController userController;

    @Test
    public void testCreateUser(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("username");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("password");
        Mockito.when(bCryptPasswordEncoder.encode("password")).thenReturn("someEncryptedPassword");
        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void testCreateUserPasswordFailure(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("username");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("");
        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    public void testFindByUserName(){
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPwd");
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);
        ResponseEntity <User> response = userController.findByUserName("testUser");
        Assert.assertEquals(user, response.getBody());
    }
}