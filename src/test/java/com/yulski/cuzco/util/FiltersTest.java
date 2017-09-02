package com.yulski.cuzco.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import com.yulski.cuzco.services.ContactService;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import spark.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FiltersTest {

    @Test
    public void loggedInFilterUserLoggedInShouldNotHaltTest() throws Exception {
        Request mockRequest  = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        User mockUser = mock(User.class);

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.headers("Accept")).thenReturn("application/json");
        when(mockSessionManager.getUser(mockSession)).thenReturn(mockUser);

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter loggedInFilter = filters.loggedIn(redirectPath);

        expectFilterToPass(mockRequest, mockResponse, loggedInFilter);
    }

    @Test
    public void loggedInFilterNoUserJsonRequestShouldHaltWithStatusCode400AndEmptyJsonStringTest() {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.headers("Accept")).thenReturn("application/json");

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter loggedInFilter = filters.loggedIn(redirectPath);

        expectFilterToTriggerForJsonRequest(mockRequest, mockResponse, loggedInFilter);
    }

    @Test
    public void loggedInFilterNoUserNonJsonRequestShouldRedirectAndHaltWithStatusCode400Test() {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.headers("Accept")).thenReturn("text/html");

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter loggedInFilter = filters.loggedIn(redirectPath);

        expectFilterToTriggerForNonJsonRequest(mockRequest, mockResponse, loggedInFilter, redirectPath);
    }

    @Test
    public void notLoggedInFilterNoUserShouldNotHaltTest() throws Exception {
        Request mockRequest  = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.headers("Accept")).thenReturn("application/json");

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter notLoggedInFilter = filters.notLoggedIn(redirectPath);

        expectFilterToPass(mockRequest, mockResponse, notLoggedInFilter);
    }

    @Test
    public void notLoggedInFilterUserLoggedInJsonRequestShouldHaltWithStatusCode400AndEmptyJsonStringTest() {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        User mockUser = mock(User.class);

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.headers("Accept")).thenReturn("application/json");
        when(mockSessionManager.getUser(mockSession)).thenReturn(mockUser);

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter notLoggedInFilter = filters.notLoggedIn(redirectPath);

        expectFilterToTriggerForJsonRequest(mockRequest, mockResponse, notLoggedInFilter);
    }

    @Test
    public void notLoggedInFilterUserLoggedInNonJsonRequestShouldRedirectAndHaltWithStatusCode400Test() {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        User mockUser = mock(User.class);

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.headers("Accept")).thenReturn("text/html");
        when(mockSessionManager.getUser(mockSession)).thenReturn(mockUser);

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter notLoggedInFilter = filters.notLoggedIn(redirectPath);

        expectFilterToTriggerForNonJsonRequest(mockRequest, mockResponse, notLoggedInFilter, redirectPath);
    }

    @Test
    public void contactAccessFilterValidReqiestShouldNotHaltTest() throws Exception {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        User mockUser = mock(User.class);
        Contact mockContact = mock(Contact.class);
        ContactService mockContactService = mock(ContactService.class);
        final int contactId = 1;
        final int userId = 100;

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.params("id")).thenReturn(Integer.toString(contactId));
        when(mockRequest.headers("Accept")).thenReturn("text/html");
        when(mockSessionManager.getUser(mockSession)).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(userId);
        when(mockContact.getUser()).thenReturn(mockUser);
        when(mockContactService.getOne(contactId)).thenReturn(mockContact);

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter contactAccessFilter = filters.contactAccess(redirectPath, mockContactService);

        expectFilterToPass(mockRequest, mockResponse, contactAccessFilter);
    }

    @Test
    public void contactAccessFilterContactDoesntExistJsonRequestShouldHaltWithStatusCode400AndEmptyJsonStringTest() {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        User mockUser = mock(User.class);
        ContactService mockContactService = mock(ContactService.class);
        final int contactId = 1;
        final int userId = 100;

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.params("id")).thenReturn(Integer.toString(contactId));
        when(mockRequest.headers("Accept")).thenReturn("application/json");
        when(mockSessionManager.getUser(mockSession)).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(userId);
        when(mockContactService.getOne(contactId)).thenReturn(null);

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter contactAccessFilter = filters.contactAccess(redirectPath, mockContactService);

        expectFilterToTriggerForJsonRequest(mockRequest, mockResponse, contactAccessFilter);
    }


    @Test
    public void contactAccessFilterContactDoesntExistNonJsonRequestShouldRedirectAndHaltWithStatusCode400Test() {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        User mockUser = mock(User.class);
        ContactService mockContactService = mock(ContactService.class);
        final int contactId = 1;
        final int userId = 100;

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.params("id")).thenReturn(Integer.toString(contactId));
        when(mockRequest.headers("Accept")).thenReturn("text/html");
        when(mockSessionManager.getUser(mockSession)).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(userId);
        when(mockContactService.getOne(contactId)).thenReturn(null);

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter contactAccessFilter = filters.contactAccess(redirectPath, mockContactService);

        expectFilterToTriggerForNonJsonRequest(mockRequest, mockResponse, contactAccessFilter, redirectPath);
    }

    @Test
    public void contactAccessFilterUserIsNotOwnerJsonRequestShouldHaltWithStatusCode400AndEmptyJsonStringTest() {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        Contact mockContact = mock(Contact.class);
        User mockUser = mock(User.class);
        User mockContactOwner = mock(User.class);
        ContactService mockContactService = mock(ContactService.class);
        final int contactId = 1;
        final int userId = 100;
        final int contactOwnerId = 999;

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.params("id")).thenReturn(Integer.toString(contactId));
        when(mockRequest.headers("Accept")).thenReturn("application/json");
        when(mockSessionManager.getUser(mockSession)).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(userId);
        when(mockContactOwner.getId()).thenReturn(contactOwnerId);
        when(mockContactService.getOne(contactId)).thenReturn(mockContact);
        when(mockContact.getUser()).thenReturn(mockContactOwner);

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter contactAccessFilter = filters.contactAccess(redirectPath, mockContactService);

        expectFilterToTriggerForJsonRequest(mockRequest, mockResponse, contactAccessFilter);
    }

    @Test
    public void contactAccessFilterUserIsNotOwnerNonJsonRequestShouldRedirectAndHaltWithStatusCode400Test() {
        Request mockRequest = mock(Request.class);
        Response mockResponse = mock(Response.class);
        Session mockSession = mock(Session.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        Contact mockContact = mock(Contact.class);
        User mockUser = mock(User.class);
        User mockContactOwner = mock(User.class);
        ContactService mockContactService = mock(ContactService.class);
        final int contactId = 1;
        final int userId = 100;
        final int contactOwnerId = 999;

        when(mockRequest.session()).thenReturn(mockSession);
        when(mockRequest.params("id")).thenReturn(Integer.toString(contactId));
        when(mockRequest.headers("Accept")).thenReturn("text/html");
        when(mockSessionManager.getUser(mockSession)).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(userId);
        when(mockContactOwner.getId()).thenReturn(contactOwnerId);
        when(mockContactService.getOne(contactId)).thenReturn(mockContact);
        when(mockContact.getUser()).thenReturn(mockContactOwner);

        final String redirectPath = "/test/path";
        Filters filters = new Filters(mockSessionManager);
        Filter contactAccessFilter = filters.contactAccess(redirectPath, mockContactService);

        expectFilterToTriggerForNonJsonRequest(mockRequest, mockResponse, contactAccessFilter, redirectPath);
    }

    private void expectFilterToPass(Request mockRequest, Response mockResponse, Filter filter) throws Exception {
        try {
            filter.handle(mockRequest, mockResponse);
        } catch(Exception e) {
            if(e instanceof HaltException) {
                fail("Halt exception thrown when not expected");
            } else {
                throw e;
            }
        }
    }

    private void expectFilterToTriggerForJsonRequest(Request mockRequest, Response mockResponse, Filter filter) {
        try {
            filter.handle(mockRequest, mockResponse);
            fail("No halt exception thrown");
        } catch(Exception exception) {
            assertTrue(exception instanceof HaltException);
            HaltException haltException = (HaltException) exception;
            assertEquals(400, haltException.statusCode());
            assertEquals(new Gson().toJson(new JsonObject()), haltException.body());
        }
    }

    private void expectFilterToTriggerForNonJsonRequest(Request mockRequest, Response mockResponse, Filter filter,
                                                        final String redirectPath) {
        final boolean[] redirected = {false};
        doAnswer((InvocationOnMock invocation) -> {
            redirected[0] = true;
            return null;
        }).when(mockResponse).redirect(redirectPath);
        try {
            filter.handle(mockRequest, mockResponse);
            fail("No halt exception thrown");
        } catch(Exception exception) {
            assertTrue(exception instanceof HaltException);
            HaltException haltException = (HaltException) exception;
            assertTrue(redirected[0]);
            assertEquals(400, haltException.statusCode());
        }
    }

}
