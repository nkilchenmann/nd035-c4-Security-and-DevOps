package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private User user = mock(User.class);


    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_order_happy_path() {

        // items
        Item mockedItem1 = TestUtils.createTestItem("Earplugs", "Bluetooth earplugs tailored for sport activites.", new BigDecimal(120));
        Item mockedItem2 = TestUtils.createTestItem("Hairdryer", "Hairdryer with revolutionary ION technology.", new BigDecimal(250));
        List<Item> mockedItems = Arrays.asList(mockedItem1, mockedItem2);

        // user
        User mockedUser = TestUtils.createUser("nik");

        // cart
        Cart mockedCart = TestUtils.createCart(mockedUser, mockedItems);

        // user order
        UserOrder mockedOrder = new UserOrder();
        mockedOrder.setUser(mockedUser);
        mockedOrder.setItems(mockedItems);
        mockedUser.setCart(mockedCart);

        // mock methods
        when(userRepository.findByUsername("nik")).thenReturn(mockedUser);
        when(user.getCart()).thenReturn(mockedCart);

        // map response
        ResponseEntity<UserOrder> response = orderController.submit("nik");
        HttpStatus responseStatusCode = response.getStatusCode();
        UserOrder responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status is ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify order contains the correct user", mockedUser, responseBody.getUser());
        Assert.assertEquals("Verify order contains the correct items", mockedItems, responseBody.getItems());
        Assert.assertEquals("Verify order contains the correct total price", mockedCart.getTotal(), responseBody.getTotal());
    }

    @Test
    public void submit_order_user_not_found() {
        // mock methods
        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        // map response
        ResponseEntity<UserOrder> response = orderController.submit(ArgumentMatchers.anyString());
        HttpStatus responseStatusCode = response.getStatusCode();
        UserOrder responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status is not found", HttpStatus.NOT_FOUND, responseStatusCode);
        Assert.assertNull("Verify response is null", responseBody);
    }

    @Test
    public void review_order_history_happy_path() {
        // user
        User mockedUser = TestUtils.createUser("nik");

        // user order
        UserOrder mockedUserOrder1 = new UserOrder();
        UserOrder mockedUserOrder2 = new UserOrder();
        List<UserOrder> mockedUserOrders = Arrays.asList(mockedUserOrder1, mockedUserOrder2);

        // mock methods
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(mockedUser);
        when(orderRepository.findByUser(ArgumentMatchers.any())).thenReturn(mockedUserOrders);

        // map response
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(ArgumentMatchers.anyString());
        HttpStatus responseStatusCode = response.getStatusCode();
        List<UserOrder> responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status is ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify order contains the correct user order list", mockedUserOrders, responseBody);
    }

    @Test
    public void review_order_history_user_not_found() {
        // mock methods
        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        // map response
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(ArgumentMatchers.anyString());
        HttpStatus responseStatusCode = response.getStatusCode();
        List<UserOrder> responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status is not found", HttpStatus.NOT_FOUND, responseStatusCode);
        Assert.assertNull("Verify response is null", responseBody);
    }
}
