package servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import database.DBException;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SignUpServlet Tester.
 *
 * @author <Authors name>
 * @since <pre>��� 30, 2018</pre>
 * @version 1.0
 */
public class SignUpServletTest extends ConfigServletTest {
    private SignUpServlet servlet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new SignUpServlet();
        Field field = SignUpServlet.class.getDeclaredField("accountService");
        field.setAccessible(true);
        field.set(servlet, accountService);
        field.setAccessible(false);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        servlet = null;
    }
    
    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNullLogin()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNullPassword()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(null);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostEmptyLogin()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostEmptyPassword()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn("");
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostDBExceptionWhenCheckUser() throws IOException, ServletException, DBException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(UNREGISTERED_LOGIN);
        when(request.getParameter("password")).thenReturn(UNREGISTERED_PASSWORD);
        when(accountService.getUserByLogin(UNREGISTERED_LOGIN)).thenThrow(new DBException("getUserByLogin"));
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostDBExceptionWhenAddUser() throws IOException, ServletException, DBException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(UNREGISTERED_LOGIN);
        when(request.getParameter("password")).thenReturn(UNREGISTERED_PASSWORD);
        when(accountService.getUserByLogin(UNREGISTERED_LOGIN)).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        Mockito.doThrow(new DBException("addNewUser")).when(accountService).addNewUser(UNREGISTERED_LOGIN, UNREGISTERED_PASSWORD);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostIncorrectLogin() throws IOException, ServletException, DBException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(accountService.getUserByLogin(DEFAULT_LOGIN)).thenReturn(DEFAULT_USER);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostCorrect() throws IOException, ServletException, DBException {
        when(request.getParameter("login")).thenReturn(UNREGISTERED_LOGIN);
        when(request.getParameter("password")).thenReturn(UNREGISTERED_PASSWORD);
        when(accountService.getUserByLogin(UNREGISTERED_LOGIN)).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);

        servlet.doPost(request, response);

        verify(response).sendRedirect(CONTEXT_PATH + SignInServlet.URL);
    }
}
