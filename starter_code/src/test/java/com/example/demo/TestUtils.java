package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

public class TestUtils {
    public static void injectObject(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if (!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);

            if (wasPrivate) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Item createTestItem(String name, String description, BigDecimal price) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        return item;
    }

    public static User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }

    public static Cart createCart(User user, List<Item> items) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(items);
        return cart;
    }
}
