package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemRepository itemRepository = mock(ItemRepository.class);
    private ItemController itemController;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_all_items_happy_path() {
        // items
        Item mockedItem1 = TestUtils.createTestItem("Earplugs", "Bluetooth earplugs tailored for sport activites.", new BigDecimal(120));
        Item mockedItem2 = TestUtils.createTestItem("Hairdryer", "Hairdryer with revolutionary ION technology.", new BigDecimal(250));
        List<Item> mockedItems = Arrays.asList(mockedItem1, mockedItem2);

        // mock methods
        when(itemRepository.findAll()).thenReturn(mockedItems);

        // map response
        ResponseEntity<List<Item>> response = itemController.getItems();
        HttpStatus responseStatusCode = response.getStatusCode();
        List<Item> responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status code ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify response contains items", mockedItems, responseBody);
    }

    @Test
    public void get_item_by_id_happy_path() {
        // item
        Item mockedItem = TestUtils.createTestItem("Earplugs", "Bluetooth earplugs tailored for sport activites.", new BigDecimal(120));
        mockedItem.setId(1L);
        Optional<Item> mockedOptionalItem = Optional.of(mockedItem);

        // mock methods
        when(itemRepository.findById(1L)).thenReturn(mockedOptionalItem);

        // map response
        ResponseEntity<Item> response = itemController.getItemById(1L);
        HttpStatus responseStatusCode = response.getStatusCode();
        Item responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status code ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify response contains item", mockedItem, responseBody);
    }

    @Test
    public void get_items_by_name_happy_path() {
        // items
        Item mockedItem1 = TestUtils.createTestItem("Earplugs", "Bluetooth earplugs tailored for sport activites.", new BigDecimal(120));
        Item mockedItem2 = TestUtils.createTestItem("Earplugs", "Bluetooth earplugs with noise suppression for music fans", new BigDecimal(200));
        List<Item> mockedItems = Arrays.asList(mockedItem1, mockedItem2);

        // mock methods
        when(itemRepository.findByName("Earplugs")).thenReturn(mockedItems);

        // map response
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Earplugs");
        HttpStatus responseStatusCode = response.getStatusCode();
        List<Item> responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status code ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify response contains item", mockedItems, responseBody);
    }
}
