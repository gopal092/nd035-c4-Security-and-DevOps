package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import com.example.demo.model.requests.SplunkArgs;
import com.splunk.Args;
import com.splunk.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ItemRepository itemRepository;
	private final Logger logger = LoggerFactory.getLogger(CartController.class);
	@Autowired
	private Receiver splunkReceiver;
	private final Args args = new Args();

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		SplunkArgs.setSourceType(args, "CartController");
		splunkReceiver.log("main", args, "Adding to cart for user: "+request.getUsername());
		logger.info("Adding to cart for user: "+request.getUsername());
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			splunkReceiver.log("main", args, "User: "+request.getUsername()+" not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			splunkReceiver.log("main", args, "Item "+request.getItemId()+" is not present in the inventory");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);
		splunkReceiver.log("main", args, "Added.");
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		SplunkArgs.setSourceType(args, "CartController");
		splunkReceiver.log("main", args, "Removing items from cart for "+request.getUsername());
		logger.info("Removing items from cart for "+request.getUsername());
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			splunkReceiver.log("main", args, "User: "+request.getUsername()+" not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			splunkReceiver.log("main", args, "Item "+request.getItemId()+" is not present in the inventory");
			logger.info("Item not available");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		splunkReceiver.log("main", args, "Done.");
		return ResponseEntity.ok(cart);
	}
		
}
