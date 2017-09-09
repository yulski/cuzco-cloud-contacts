package com.yulski.cuzco.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void userWithNegativeIdIsNotValidTest() {
        User user = new User(-1, "email", "password");
        assertFalse(user.isValid());
    }

    @Test
    public void userWithZeroIdIsNotValidTest() {
        User user = new User(0, "email", "password");
        assertFalse(user.isValid());
    }

    @Test
    public void userWithNullEmailIsNotValidTest() {
        User user = new User(1, null, "password");
        assertFalse(user.isValid());
    }

    @Test
    public void userWithEmptyEmailIsNotValidTest() {
        User user = new User(1, "", "password");
        assertFalse(user.isValid());
    }

    @Test
    public void userWithNullPasswordIsNotValidTest() {
        User user = new User(1, "email", null);
        assertFalse(user.isValid());
    }

    @Test
    public void userWithEmptyPasswordIsNotValidTest() {
        User user = new User(1, "email", "");
        assertFalse(user.isValid());
    }

    @Test
    public void userWithAllInvalidDataIsInvalidTest() {
        User user = new User(0, "", null);
        assertFalse(user.isValid());
    }

    @Test
    public void userWithAllValidDataIsValidTest() {
        User user = new User(1, "email", "password");
        assertTrue(user.isValid());
    }

}
