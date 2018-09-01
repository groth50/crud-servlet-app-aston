package servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.ServletException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogOutServletTest extends ServletWebTest {
    private LogOutServlet servlet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new LogOutServlet();
        Field field = LogOutServlet.class.getDeclaredField("accountService");
        field.setAccessible(true);
        field.set(servlet, accountService);
        field.setAccessible(false);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    @Test
    public void testDoPostLogOut() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);

        servlet.doPost(request, response);

        verify(accountService).deleteSession(DEFAULT_SESSION_ID);
        verify(response).sendRedirect(request.getContextPath() + SignInServlet.URL);
    }
}