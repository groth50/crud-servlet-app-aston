package servlet;

import accounts.AccountService;
import accounts.UserAccount;
import utils.LongId;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;

public class ServletWebTest {
    public static final LongId<UserAccount> DEFAULT_ID = new LongId<>(-1);
    public static final String DEFAULT_LOGIN = "Test";
    public static final String UNREGISTERED_LOGIN = "Unregistered";
    public static final String DEFAULT_PASSWORD = "123";
    public static final String UNREGISTERED_PASSWORD = "666";
    public static final String DEFAULT_SESSION_ID = "123";
    public static final String UNREGISTERED_SESSION_ID = "-123";
    public static final UserAccount DEFAULT_USER = getNewUserAccount(DEFAULT_ID, DEFAULT_LOGIN, DEFAULT_PASSWORD);
    public static final UserAccount DEFAULT_ADMIN = getNewAdminAccount(DEFAULT_ID, DEFAULT_LOGIN, DEFAULT_PASSWORD);

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected RequestDispatcher dispatcher;
    protected AccountService accountService;
    protected HttpSession session;
    protected FilterChain chain;



    @Before
    public void setUp() throws Exception {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
        accountService = Mockito.mock(AccountService.class);
        session = Mockito.mock(HttpSession.class);
        chain = Mockito.mock(FilterChain.class);
    }

    @After
    public void tearDown() throws Exception {
        request = null;
        response = null;
        dispatcher = null;
        accountService = null;
        session = null;
        chain = null;
    }

    private static UserAccount getNewUserAccount(LongId<UserAccount> id, String login, String password) {
        return new UserAccount(id, login, password);
    }

    private static UserAccount getNewAdminAccount(LongId<UserAccount> id, String login, String password) {
        return new UserAccount(id, login, password, UserAccount.Role.ADMIN);
    }
}
