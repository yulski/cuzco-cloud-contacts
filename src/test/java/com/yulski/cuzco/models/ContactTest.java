package com.yulski.cuzco.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactTest {

    @Test
    public void contactWithNegativeIdIsNotValidTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(-1, "name", "1234567890", mockUser);

        when(mockUser.isValid()).thenReturn(true);

        assertFalse(contact.isValid());
    }

    @Test
    public void contactWithZeroIdIsNotValidTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(0, "name", "1234567890", mockUser);

        when(mockUser.isValid()).thenReturn(true);

        assertFalse(contact.isValid());
    }

    @Test
    public void contactWithNullNameIsNotValidTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(1, null, "1234567890", mockUser);

        when(mockUser.isValid()).thenReturn(true);

        assertFalse(contact.isValid());
    }

    @Test
    public void contactWithEmptyNameIsNotValidTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(1, "", "1234567890", mockUser);

        when(mockUser.isValid()).thenReturn(true);

        assertFalse(contact.isValid());
    }

    @Test
    public void contactWithNullPhoneNumberIsValidTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(1, "name", null, mockUser);

        when(mockUser.isValid()).thenReturn(true);

        assertTrue(contact.isValid());
    }

    @Test
    public void contactWithEmptyPhoneNumberIsNullTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(1, "name", "", mockUser);

        when(mockUser.isValid()).thenReturn(true);

        assertTrue(contact.isValid());
    }

    @Test
    public void contactWithNullUserIsNotValidTest() {
        Contact contact = new Contact(1, "name", "", null);
        assertFalse(contact.isValid());
    }

    @Test
    public void contactWithInvalidUserIsNotValidTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(1, "name", "", mockUser);

        when(mockUser.isValid()).thenReturn(false);

        assertFalse(contact.isValid());
    }

    @Test
    public void contactWithAllInvalidDataIsNotValidTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(0, null, "", mockUser);

        when(mockUser.isValid()).thenReturn(false);

        assertFalse(contact.isValid());
    }

    @Test
    public void contactWithAllValidDataIsValidTest() {
        User mockUser = mock(User.class);
        Contact contact = new Contact(1, "name", "", mockUser);

        when(mockUser.isValid()).thenReturn(true);

        assertTrue(contact.isValid());
    }

}
