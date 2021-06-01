package com.example.demo.controllers;

import java.util.List;

import com.example.demo.model.requests.SplunkArgs;
import com.splunk.Args;
import com.splunk.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private Receiver splunkReceiver;
	private final Args args = new Args();
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		SplunkArgs.setSourceType(args, "OrderController");
		splunkReceiver.log("main", args, "Creating Orders for User: "+username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			splunkReceiver.log("main", args, "user "+username+" not found");
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		splunkReceiver.log("main", args, "Done ");
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		SplunkArgs.setSourceType(args, "OrderController");
		splunkReceiver.log("main", args, "Gettomg orders for User: "+username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			splunkReceiver.log("main", args, "user "+username+" not found");
			return ResponseEntity.notFound().build();
		}
		splunkReceiver.log("main", args, "Done ");
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
