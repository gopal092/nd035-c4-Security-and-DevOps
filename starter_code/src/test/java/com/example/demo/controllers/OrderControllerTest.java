package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import javax.persistence.criteria.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderController orderController;

    @Test
    public void testSubmit(){
        User user = new User();
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(BigDecimal.valueOf(200));
        cart.addItem(item);
        user.setUsername("testUser");
        user.setPassword("pwd");
        user.setCart(cart);
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);
        Assert.assertEquals(HttpStatus.OK, orderController.submit("testUser").getStatusCode());
    }
    @Test
    public void testGetOrdersForUser(){
        User user = new User();
        List<UserOrder> userOrders = new ArrayList<>();
        UserOrder userOrder = new UserOrder();
        userOrders.add(userOrder);
        user.setUsername("testUser");
        user.setPassword("pwd");
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);
        Mockito.when(orderRepository.findByUser(user)).thenReturn(userOrders);
        Assert.assertEquals(HttpStatus.OK, orderController.getOrdersForUser("testUser").getStatusCode());
    }
}
