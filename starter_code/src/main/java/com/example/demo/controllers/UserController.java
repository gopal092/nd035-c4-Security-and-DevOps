package com.example.demo.controllers;


import com.example.demo.model.requests.SplunkArgs;
import com.splunk.Args;
import com.splunk.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private Receiver splunkReceiver;
	private final Args args = new Args();
	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		SplunkArgs.setSourceType(args, "UserController");
		splunkReceiver.log("main", args, "called findByUserName by user: "+username);
		logger.info("called findByUserName by user: "+username);
		User user = userRepository.findByUsername(username);
		splunkReceiver.log("main", args, "Got the user: "+username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		SplunkArgs.setSourceType(args, "UserController");
		splunkReceiver.log("main", args,"called createUser()");
		logger.info("called createUser()");
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length() < 7
		|| !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			splunkReceiver.log("main", args,"Entered password and confirm passwords do not match. Cannot create the User "+createUserRequest.getUsername());
			logger.error("Entered password and confirm passwords do not match. Cannot create the User "+createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		logger.info("User "+createUserRequest.getUsername()+" created successfully.");
		splunkReceiver.log("main", args, "User "+createUserRequest.getUsername()+" created successfully.");
		return ResponseEntity.ok(user);
	}
}
