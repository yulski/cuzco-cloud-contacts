package com.yulski.cuzco.util;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PathsTest {

    private final String landingPage = "LANDING_PAGE";
    private final String dashboard = "DASHBOARD";
    private final String profile = "PROFILE";
    private final String login = "LOGIN";
    private final String logout = "LOGOUT";
    private final String editProfile = "EDIT_PROFILE";
    private final String deleteProfile = "DELETE_PROFILE";
    private final String registration = "REGISTRATION";
    private final String contact = "CONTACT";
    private final String userContacts = "USER_CONTACTS";
    private final String editContact = "EDIT_CONTACT";
    private final String createContact = "CREATE_CONTACT";
    private final String deleteContact = "DELETE_CONTACT";

    @Test
    public void pathMapHasAllExpectedPathsTest() {
        Map<String, Object> paths = Paths.getMap();

        assertTrue(paths.containsKey(landingPage));
        assertEquals(Paths.LANDING_PAGE, paths.get(landingPage));

        assertTrue(paths.containsKey(dashboard));
        assertEquals(Paths.DASHBOARD, paths.get(dashboard));

        assertTrue(paths.containsKey(profile));
        assertEquals(Paths.PROFILE, paths.get(profile));

        assertTrue(paths.containsKey(login));
        assertEquals(Paths.LOGIN, paths.get(login));

        assertTrue(paths.containsKey(logout));
        assertEquals(Paths.LOGOUT, paths.get(logout));

        assertTrue(paths.containsKey(editProfile));
        assertEquals(Paths.EDIT_PROFILE, paths.get(editProfile));

        assertTrue(paths.containsKey(deleteProfile));
        assertEquals(Paths.DELETE_PROFILE, paths.get(deleteProfile));

        assertTrue(paths.containsKey(registration));
        assertEquals(Paths.REGISTRATION, paths.get(registration));

        assertTrue(paths.containsKey(contact));
        assertEquals(Paths.CONTACT, paths.get(contact));

        assertTrue(paths.containsKey(userContacts));
        assertEquals(Paths.USER_CONTACTS, paths.get(userContacts));

        assertTrue(paths.containsKey(editContact));
        assertEquals(Paths.EDIT_CONTACT, paths.get(editContact));

        assertTrue(paths.containsKey(createContact));
        assertEquals(Paths.CREATE_CONTACT, paths.get(createContact));

        assertTrue(paths.containsKey(deleteContact));
        assertEquals(Paths.DELETE_CONTACT, paths.get(deleteContact));
    }

    @Test
    public void pathMapOnlyContainsExpectedValuesTest() {
        Map<String, Object> paths = Paths.getMap();

        paths.remove(landingPage);
        paths.remove(dashboard);
        paths.remove(profile);
        paths.remove(login);
        paths.remove(logout);
        paths.remove(editProfile);
        paths.remove(deleteProfile);
        paths.remove(registration);
        paths.remove(contact);
        paths.remove(userContacts);
        paths.remove(editContact);
        paths.remove(createContact);
        paths.remove(deleteContact);

        assertTrue(paths.isEmpty());
    }

}
