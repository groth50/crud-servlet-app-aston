package servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddUserServletTest extends ServletWebTest {
    private AddUserServlet servlet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new AddUserServlet();
        Field field = AddUserServlet.class.getDeclaredField("accountService");
        field.setAccessible(true);
        field.set(servlet, accountService);
        field.setAccessible(false);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testAddUserDoGet() throws IOException, ServletException {
        when(request.getRequestDispatcher(AddUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testAddUserNullLogin()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(AddUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testAddUserNullPassword()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(AddUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(null);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testAddUserEmptyLogin()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(AddUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testAddUserEmptyPassword()
            throws ServletException, IOException {
        when(request.getRequestDispatcher(AddUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn("");
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void doAddUserCorrect() throws IOException, ServletException {
        when(request.getParameter("login")).thenReturn(UNREGISTERED_LOGIN);
        when(request.getParameter("password")).thenReturn(UNREGISTERED_PASSWORD);
        when(accountService.getUserByLogin(DEFAULT_LOGIN)).thenReturn(null);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + GetAdminMenuServlet.URL);
    }
}