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

public class DeleteUserServletTest extends ConfigServletTest {
    private DeleteUserServlet servlet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new DeleteUserServlet();
        Field field = DeleteUserServlet.class.getDeclaredField("accountService");
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
    public void testDoPostNullId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("userId")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostEmptyId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("userId")).thenReturn("");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNegativeId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("userId")).thenReturn("-1");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostZeroId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("userId")).thenReturn("0");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNotNumberId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("userId")).thenReturn("A");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNotIntegerId() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("userId")).thenReturn("0.1");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostExceptionWhenDeleteUser() throws ServletException, IOException, DBException {
        when(request.getRequestDispatcher(GetAdminMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("userId")).thenReturn(DEFAULT_STRING_ID);
        Mockito.doThrow(new DBException("deleteUser")).when(accountService).deleteUser(DEFAULT_STRING_ID);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostCorrectId() throws ServletException, IOException, DBException {
        when(request.getRequestDispatcher(UpdateUserServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("userId")).thenReturn(DEFAULT_STRING_ID);
        when(accountService.getUserById(DEFAULT_STRING_ID)).thenReturn(DEFAULT_USER);
        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).sendRedirect(CONTEXT_PATH + GetAdminMenuServlet.URL);
    }
}