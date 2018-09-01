package servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SignUpServlet Tester.
 *
 * @author <Authors name>
 * @since <pre>��� 30, 2018</pre>
 * @version 1.0
 */
public class SignUpServletTest extends ServletWebTest {
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
    }
    
    @Test
    public void testSignUpDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher(SignUpServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testSignUpNullLogin()
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
    public void testSignUpNullPassword()
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
    public void testSignUpEmptyLogin()
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
    public void testSignUpEmptyPassword()
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
    public void testSignUpCorrect() throws IOException, ServletException {
        when(request.getParameter("login")).thenReturn(UNREGISTERED_LOGIN);
        when(request.getParameter("password")).thenReturn(UNREGISTERED_PASSWORD);
        when(accountService.getUserByLogin(DEFAULT_LOGIN)).thenReturn(null);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + SignInServlet.URL);
    }
}
