package servlet;

import java.io.IOException;
import java.lang.reflect.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class SignInServletTest extends ServletWebTest {
    private SignInServlet servlet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new SignInServlet();
        Field field = SignInServlet.class.getDeclaredField("accountService");
        field.setAccessible(true);
        field.set(servlet, accountService);
        field.setAccessible(false);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher(SignInServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testNullLogin()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(SignInServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testNullPassword()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(SignInServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testEmptyLogin()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(SignInServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testEmptyPassword()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(SignInServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn("");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostIncorrectLogin() throws ServletException, IOException {
        when(request.getRequestDispatcher(SignInServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn(UNREGISTERED_LOGIN);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(accountService.getUserByLogin(UNREGISTERED_LOGIN)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostIncorrectPassword() throws ServletException, IOException {
        when(request.getRequestDispatcher(SignInServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(UNREGISTERED_PASSWORD);
        when(accountService.getUserByLogin(DEFAULT_LOGIN)).thenReturn(DEFAULT_USER);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostCorrect() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetMainMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(accountService.getUserByLogin(DEFAULT_LOGIN)).thenReturn(DEFAULT_USER);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + GetMainMenuServlet.URL);
    }
}