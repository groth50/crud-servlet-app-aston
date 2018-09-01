package accounts;

import utils.LongId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

public class AccountServiceTest {
    private static final LongId<UserAccount> DEFAULT_ID = new LongId<>(-1);
    private static final LongId<UserAccount> UNREGISTERED_ID = new LongId<>(-2);
    private static final String DEFAULT_LOGIN = "default";
    private static final String UNREGISTERED_LOGIN = "unregistered";
    private static final String DEFAULT_PASSWORD = "123";
    private static final String UNREGISTERED_PASSWORD = "666";
    private static final String DEFAULT_SESSION_ID = "-1";
    private static final String UNREGISTERED_SESSION_ID = "-2";
    private static final UserAccount DEFAULT_USER = getNewUserAccount(DEFAULT_ID, DEFAULT_LOGIN, DEFAULT_PASSWORD);
    private static final UserAccount UNREGISTERED_USER = getNewUserAccount(UNREGISTERED_ID, UNREGISTERED_LOGIN, UNREGISTERED_PASSWORD);

    private final AccountService accountService = FactoryAccountService.getAccountService();

    @Before
    public void setUp() throws Exception {
        accountService.addNewUser(DEFAULT_LOGIN, DEFAULT_PASSWORD);
        accountService.addSession(DEFAULT_SESSION_ID, DEFAULT_USER);
    }

    @After
    public void tearDown() throws Exception {
        accountService.deleteUser(DEFAULT_LOGIN);
        accountService.deleteSession(DEFAULT_SESSION_ID);
    }

    @Test
    public void testGetUserByLogin() {
        UserAccount user = accountService.getUserByLogin(DEFAULT_LOGIN);
        assertNotNull(user);
        assertEquals(DEFAULT_LOGIN, user.getLogin());
    }

    @Test
    public void testAddNewUser() {
        accountService.addNewUser(UNREGISTERED_LOGIN, UNREGISTERED_PASSWORD);
        UserAccount addedUser = accountService.getUserByLogin(UNREGISTERED_LOGIN);
        assertNotNull(addedUser);
        assertEquals(UNREGISTERED_USER.getLogin(), addedUser.getLogin());
        accountService.deleteUser(addedUser.getLogin());
    }

    @Test
    public void testDeleteUser() {
        UserAccount user = accountService.getUserByLogin(DEFAULT_LOGIN);
        assertNotNull(user);
        accountService.deleteUser(DEFAULT_LOGIN);
        user = accountService.getUserByLogin(DEFAULT_LOGIN);
        assertNull(user);
    }

    @Test
    public void testGetUserBySessionId() {
        UserAccount user = accountService.getUserBySessionId(DEFAULT_SESSION_ID);
        assertNotNull(user);
        assertEquals(user.getLogin(), DEFAULT_LOGIN);
    }

    @Test
    public void testAddSession() {
        accountService.addSession(UNREGISTERED_SESSION_ID, UNREGISTERED_USER);
        UserAccount addedUser = accountService.getUserBySessionId(UNREGISTERED_SESSION_ID);
        assertNotNull(addedUser);
        assertEquals(UNREGISTERED_USER.getLogin(), addedUser.getLogin());
    }

    @Test
    public void testDeleteSession() {
        UserAccount user = accountService.getUserBySessionId(DEFAULT_SESSION_ID);
        assertNotNull(user);
        accountService.deleteSession(DEFAULT_SESSION_ID);
        user = accountService.getUserBySessionId(DEFAULT_SESSION_ID);
        assertNull(user);
    }

    private static UserAccount getNewUserAccount(LongId<UserAccount> id, String login, String password) {
        return new UserAccount(id, login, password);
    }
}