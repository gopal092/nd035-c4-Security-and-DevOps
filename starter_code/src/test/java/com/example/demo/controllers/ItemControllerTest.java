package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.splunk.Receiver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemController itemController;
    @Test
    public void testGetItemsByName(){
        ArrayList<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(BigDecimal.valueOf(200));
        items.add(item);
        Mockito.when(itemRepository.findByName("item1")).thenReturn(items);
        Assert.assertEquals(HttpStatus.OK, itemController.getItemsByName("item1").getStatusCode());
    }
    @Test
    public void testGetItemById(){
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        Assert.assertEquals(HttpStatus.NOT_FOUND, itemController.getItemById(1L).getStatusCode());
    }
}
