package com.yulski.cuzco.util;

import com.yulski.cuzco.models.User;
import org.junit.Test;
import spark.Session;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SessionManagerTest {

    @Test
    public void setFlashMessageSetsFlashMessageInSessionTest() {
        Session mockSession = mock(Session.class);
        SessionManager sessionManager = new SessionManager();

        final String flashMessage = "test message";
        final String flashMessageType = "info";

        sessionManager.setFlashMessage(flashMessage, flashMessageType, mockSession);

        verify(mockSession, times(1)).attribute(SessionManager.FLASH_MESSAGE, flashMessage);
        verify(mockSession, times(1)).attribute(SessionManager.FLASH_MESSAGE_TYPE, flashMessageType);
    }

    @Test
    public void getFlashMessageReturnsAndRemovesFlashMessageFromSessionTest() {
        Session mockSession = mock(Session.class);
        SessionManager sessionManager = new SessionManager();

        final String flashMessage = "test message";

        when(mockSession.attribute(SessionManager.FLASH_MESSAGE)).thenReturn(flashMessage);

        String result = sessionManager.getFlashMessage(mockSession);

        assertEquals(flashMessage, result);
        verify(mockSession, times(1)).attribute(SessionManager.FLASH_MESSAGE);
        verify(mockSession, times(1)).removeAttribute(SessionManager.FLASH_MESSAGE);
    }

    @Test
    public void getFlashMessageTypeReturnsAndRemovesFlashMessageFromSessionTest() {
        Session mockSession = mock(Session.class);
        SessionManager sessionManager = new SessionManager();

        final String flashMessageType = "info";

        when(mockSession.attribute(SessionManager.FLASH_MESSAGE_TYPE)).thenReturn(flashMessageType);

        String result = sessionManager.getFlashMessageType(mockSession);

        assertEquals(flashMessageType, result);
        verify(mockSession, times(1)).attribute(SessionManager.FLASH_MESSAGE_TYPE);
        verify(mockSession, times(1)).removeAttribute(SessionManager.FLASH_MESSAGE_TYPE);
    }

    @Test
    public void setUserSetsUserInSessionTest() {
        Session mockSession = mock(Session.class);
        SessionManager sessionManager = new SessionManager();
        User mockUser = mock(User.class);

        sessionManager.setUser(mockUser, mockSession);

        verify(mockSession, times(1)).attribute(SessionManager.USER, mockUser);
    }

    @Test
    public void getUserReturnsUserStoredInSessionTest() {
        Session mockSession = mock(Session.class);
        SessionManager sessionManager = new SessionManager();
        User mockUser = mock(User.class);

        final int userId = 100;

        when(mockUser.getId()).thenReturn(userId);
        when(mockSession.attribute(SessionManager.USER)).thenReturn(mockUser);

        User user = sessionManager.getUser(mockSession);
        assertEquals(userId, user.getId());
    }

    @Test
    public void removeUserRemovesUserFromSessionTest() {
        Session mockSession = mock(Session.class);
        SessionManager sessionManager = new SessionManager();

        sessionManager.removeUser(mockSession);

        verify(mockSession, times(1)).removeAttribute(SessionManager.USER);
    }

}
