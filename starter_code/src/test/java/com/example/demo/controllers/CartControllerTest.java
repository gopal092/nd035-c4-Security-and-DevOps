package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.splunk.Receiver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private Receiver splunkReceiver;
    @InjectMocks
    private CartController cartController;
    @Test
    public void testAddToCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);
        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(BigDecimal.valueOf(200));
        cart.addItem(item);
        user.setUsername("testUser");
        user.setPassword("pwd");
        request.setItemId(1);
        request.setUsername("testUser");
        request.setQuantity(5);
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertEquals(HttpStatus.OK, cartController.addTocart(request).getStatusCode());
    }
    @Test
    public void testAddToCartNullUser(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setUsername("testUser");
        request.setQuantity(5);
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(null);
        Assert.assertEquals(HttpStatus.NOT_FOUND, cartController.addTocart(request).getStatusCode());
    }
    @Test
    public void testAddToCartNullItem(){
        ModifyCartRequest request = new ModifyCartRequest();
        User user = new User();
        request.setItemId(1L);
        request.setUsername("testUser");
        request.setQuantity(5);
        user.setUsername("testUser");
        user.setPassword("pwd");
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        Assert.assertEquals(HttpStatus.NOT_FOUND, cartController.addTocart(request).getStatusCode());
    }
    @Test
    public void testRemoveFromCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);
        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(BigDecimal.valueOf(200));
        cart.addItem(item);
        user.setUsername("testUser");
        user.setPassword("pwd");
        request.setItemId(1);
        request.setUsername("testUser");
        request.setQuantity(5);
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Assert.assertEquals(HttpStatus.OK, cartController.removeFromcart(request).getStatusCode());
    }
}
