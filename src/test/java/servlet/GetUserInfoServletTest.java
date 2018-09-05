package servlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetUserInfoServletTest extends ServletWebTest {
    private GetUserInfoServlet servlet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new GetUserInfoServlet();
        Field field = GetUserInfoServlet.class.getDeclaredField("accountService");
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
        when(request.getRequestDispatcher(GetUserInfoServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);
        when(accountService.getUserBySessionId(DEFAULT_SESSION_ID)).thenReturn(DEFAULT_USER);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(request, response);
    }
}