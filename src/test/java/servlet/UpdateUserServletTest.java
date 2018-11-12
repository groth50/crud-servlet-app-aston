package servlet;

import database.DBException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateUserServletTest extends ConfigServletTest {
    private UpdateUserServlet servlet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new UpdateUserServlet();
        Field field = UpdateUserServlet.class.getDeclaredField("accountService");
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
    public void testDoGetNullId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetEmptyId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("");

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNegativeId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("-1");

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetZeroId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("0");

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNotNumberId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("A");

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNotIntegerId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("0.1");

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetIncorrectId() throws ServletException, IOException, DBException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn(DEFAULT_STRING_ID);
        when(accountService.getUserById(DEFAULT_STRING_ID)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetExceptionWhenCheckUser() throws ServletException, IOException, DBException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn(DEFAULT_STRING_ID);
        when(accountService.getUserById(DEFAULT_STRING_ID)).thenThrow(new DBException("getUserById"));

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetCorrectId() throws ServletException, IOException, DBException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn(DEFAULT_STRING_ID);
        when(accountService.getUserById(DEFAULT_STRING_ID)).thenReturn(DEFAULT_USER);
        when(request.getServletContext()).thenReturn(servletContext);

        servlet.doGet(request, response);

        verify(servletContext).setAttribute("user", DEFAULT_USER);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNullUser() throws ServletException, IOException {
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(null);

        servlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).sendRedirect(CONTEXT_PATH + GetAdminMenuServlet.URL);
    }

    @Test
    public void testDoPostNullLogin() throws ServletException, IOException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(DEFAULT_USER);
        when(request.getParameter("login")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getParameter("role")).thenReturn(DEFAULT_STRING_ROlE);

        servlet.doPost(request, response);

        verify(request).setAttribute("user", DEFAULT_USER);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNullPassword() throws ServletException, IOException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(DEFAULT_USER);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("role")).thenReturn(DEFAULT_STRING_ROlE);

        servlet.doPost(request, response);

        verify(request).setAttribute("user", DEFAULT_USER);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNullRole() throws ServletException, IOException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(DEFAULT_USER);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getParameter("role")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("user", DEFAULT_USER);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostEmptyLogin() throws ServletException, IOException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(DEFAULT_USER);
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getParameter("role")).thenReturn(DEFAULT_STRING_ROlE);

        servlet.doPost(request, response);

        verify(request).setAttribute("user", DEFAULT_USER);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostEmptyPassword() throws ServletException, IOException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(DEFAULT_USER);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("role")).thenReturn(DEFAULT_STRING_ROlE);

        servlet.doPost(request, response);

        verify(request).setAttribute("user", DEFAULT_USER);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostEmptyRole() throws ServletException, IOException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(DEFAULT_USER);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getParameter("role")).thenReturn("");

        servlet.doPost(request, response);

        verify(request).setAttribute("user", DEFAULT_USER);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostDBExceptionWhenUpdateUser() throws ServletException, IOException, DBException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(DEFAULT_USER);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getParameter("role")).thenReturn(DEFAULT_STRING_ROlE);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        Mockito.doThrow(new DBException("updateUser")).when(accountService).updateUser(DEFAULT_USER);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        verify(response).sendRedirect(CONTEXT_PATH + GetAdminMenuServlet.URL);
    }

    @Test
    public void testDoPostCorrect() throws ServletException, IOException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("user")).thenReturn(DEFAULT_USER);
        when(request.getParameter("login")).thenReturn(DEFAULT_LOGIN);
        when(request.getParameter("password")).thenReturn(DEFAULT_PASSWORD);
        when(request.getParameter("role")).thenReturn(DEFAULT_STRING_ROlE);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).sendRedirect(CONTEXT_PATH + GetAdminMenuServlet.URL);
    }

}