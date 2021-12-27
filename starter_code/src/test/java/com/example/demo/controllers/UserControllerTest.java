package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0, u.getId());
        Assert.assertEquals("test", u.getUsername());
        Assert.assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void create_user_wrong_password_format() {
        // create user request
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("nik");
        createUserRequest.setPassword("short");

        // map response
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        HttpStatus responseStatusCode = response.getStatusCode();

        // Assertions
        Assert.assertEquals("Verify HTTP status code is bad request", HttpStatus.BAD_REQUEST, responseStatusCode);
    }

    @Test
    public void create_user_password_not_equal_confirmpassword() {
        // create user request
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("nik");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("confirmPassword");

        // map response
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        HttpStatus responseStatusCode = response.getStatusCode();

        // Assertions
        Assert.assertEquals("Verify HTTP status code is bad request", HttpStatus.BAD_REQUEST, responseStatusCode);
    }

    @Test
    public void find_user_by_id_happy_path() {
        // user
        User mockedUser = new User();
        Optional<User> mockedOptionalUser = Optional.of(mockedUser);

        // mock methods
        when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(mockedOptionalUser);

        // map response
        ResponseEntity<User> response = userController.findById(ArgumentMatchers.anyLong());
        HttpStatus responseStatusCode = response.getStatusCode();
        User responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status is ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify user is returned", mockedOptionalUser.get(), responseBody);
    }

    @Test
    public void find_user_by_username_happy_path() {
        // user
        User mockedUser = new User();
        mockedUser.setUsername("nik");

        // mock methods
        when(userRepository.findByUsername("nik")).thenReturn(mockedUser);

        // map response
        ResponseEntity<User> response = userController.findByUserName("nik");
        HttpStatus responseStatusCode = response.getStatusCode();
        User responseBody = response.getBody();

        // Assertions
        Assert.assertEquals("Verify HTTP status is ok", HttpStatus.OK, responseStatusCode);
        Assert.assertEquals("Verify user is returned", mockedUser, responseBody);
    }
}
