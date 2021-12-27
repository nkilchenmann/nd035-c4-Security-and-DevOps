package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
    }

    @Test
    public void add_item_to_cart_happy_path() {
        // user
        User mockedUser = TestUtils.createUser("nik");

        // item
        Item mockedItem = TestUtils.createTestItem("Earplugs", "Bluetooth earplugs tailored for sport activites.", new BigDecimal(120));
        mockedItem.setId(1L);
        Optional<Item> mockedOptionalItem = Optional.of(mockedItem);

        // modify cart request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(mockedUser.getUsername());
        modifyCartRequest.setItemId(mockedItem.getId());
        modifyCartRequest.setQuantity(3);

        // cart
        Cart mockedCart = TestUtils.createCart(mockedUser, new ArrayList<Item>(Arrays.asList(mockedItem)));
        mockedCart.setId(1L);
        mockedCart.setTotal(mockedItem.getPrice());
        mockedUser.setCart(mockedCart);

        // mock methods
        when(userRepository.findByUsername("nik")).thenReturn(mockedUser);
        when(itemRepository.findById(1L)).thenReturn(mockedOptionalItem);

        // map response
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        HttpStatus responseStatusCode = response.getStatusCode();
        Cart responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status code ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify response contains cart", mockedCart, responseBody);
    }

    @Test
    public void add_item_to_cart_user_null() {
        // mock methods
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(null);

        // modify cart request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        // map response
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        HttpStatus responseStatusCode = response.getStatusCode();

        // Assertions
        Assert.assertEquals("Verify HTTP status not found", HttpStatus.NOT_FOUND, responseStatusCode);
    }

    @Test
    public void add_item_to_cart_item_not_present() {
        // user
        User mockedUser = TestUtils.createUser("nik");

        // empty optional
        Optional<Item> mockedEmptyOptional = Optional.empty();

        // mock methods
        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockedUser);
        when(itemRepository.findById(ArgumentMatchers.anyLong())).thenReturn(mockedEmptyOptional);

        // modify cart request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        // map response
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        HttpStatus responseStatusCode = response.getStatusCode();

        // Assertions
        Assert.assertEquals("Verify HTTP status not found", HttpStatus.NOT_FOUND, responseStatusCode);
    }

    @Test
    public void remove_item_from_cart_happy_path() {
        // user
        User mockedUser = TestUtils.createUser("nik");

        // item
        Item mockedItem = TestUtils.createTestItem("Earplugs", "Bluetooth earplugs tailored for sport activites.", new BigDecimal(120));
        mockedItem.setId(1L);
        Optional<Item> mockedOptionalItem = Optional.of(mockedItem);

        // modify cart request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(mockedUser.getUsername());
        modifyCartRequest.setItemId(mockedItem.getId());
        modifyCartRequest.setQuantity(3);

        // cart
        Cart mockedCart = TestUtils.createCart(mockedUser, new ArrayList<Item>(Arrays.asList(mockedItem)));
        mockedCart.setId(1L);
        mockedCart.setTotal(mockedItem.getPrice());
        mockedUser.setCart(mockedCart);

        // mock methods
        when(userRepository.findByUsername("nik")).thenReturn(mockedUser);
        when(itemRepository.findById(1L)).thenReturn(mockedOptionalItem);

        // map response
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        HttpStatus responseStatusCode = response.getStatusCode();
        Cart responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status code ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify response contains cart", mockedCart, responseBody);
    }

    @Test
    public void remove_item_from_cart_user_null() {
        // mock methods
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(null);

        // modify cart request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        // map response
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        HttpStatus responseStatusCode = response.getStatusCode();

        // Assertions
        Assert.assertEquals("Verify HTTP status not found", HttpStatus.NOT_FOUND, responseStatusCode);

    }

    @Test
    public void remove_item_from_cart_item_not_present() {
        // user
        User mockedUser = new User();

        // empty optional
        Optional<Item> mockedEmptyOptional = Optional.empty();

        // mock methods
        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockedUser);
        when(itemRepository.findById(ArgumentMatchers.anyLong())).thenReturn(mockedEmptyOptional);

        // modify cart request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        // map response
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        HttpStatus responseStatusCode = response.getStatusCode();

        // Assertions
        Assert.assertEquals("Verify HTTP status not found", HttpStatus.NOT_FOUND, responseStatusCode);

    }
}
