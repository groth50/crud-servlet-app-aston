package database;

import accounts.UserAccount;
import dao.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import utils.LongId;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DBServiceTest {
    private static final LongId<UserAccount> DEFAULT_LONG_ID = new LongId<>(-1);
    private static final String DEFAULT_STRING_ID = String.valueOf(DEFAULT_LONG_ID.getId());
    private static final String DEFAULT_LOGIN = "default";
    private static final String DEFAULT_PASSWORD = "123";
    private static final String DEFAULT_ROLE_STRING = "USER";
    private static final UserAccount.Role DEFAULT_ROLE = UserAccount.Role.valueOf(DEFAULT_ROLE_STRING);
    private static final UserAccount DEFAULT_USER = getNewUserAccount(DEFAULT_LONG_ID, DEFAULT_LOGIN, DEFAULT_PASSWORD);

    private Connection connection;
    private UserDAO dao;
    private DBService dbService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        connection = Mockito.mock(Connection.class);
        dao = Mockito.mock(UserDAO.class);
        dbService = new DBService(connection, dao);
    }

    @After
    public void tearDown() throws Exception {
        connection = null;
        dao = null;
        dbService = null;
    }

    @Test
    public void addNewUserCorrect() throws SQLException, DBException {
        when(dao.insertUser(DEFAULT_LOGIN, DEFAULT_PASSWORD, DEFAULT_ROLE_STRING)).thenReturn(1);
        dbService.addNewUser(DEFAULT_LOGIN, DEFAULT_PASSWORD, DEFAULT_ROLE);
        verify(connection).commit();
    }

    @Test
    public void addNewUserIncorrect() throws SQLException, DBException {
        when(dao.insertUser(DEFAULT_LOGIN, DEFAULT_PASSWORD, DEFAULT_ROLE_STRING)).thenReturn(0);
        thrown.expect(DBException.class);
        dbService.addNewUser(DEFAULT_LOGIN, DEFAULT_PASSWORD, DEFAULT_ROLE);
        thrown = ExpectedException.none();

        when(dao.insertUser(DEFAULT_LOGIN, DEFAULT_PASSWORD, DEFAULT_ROLE_STRING)).thenReturn(-1);
        thrown.expect(DBException.class);
        dbService.addNewUser(DEFAULT_LOGIN, DEFAULT_PASSWORD, DEFAULT_ROLE);
        thrown = ExpectedException.none();
    }

    @Test
    public void deleteUserCorrect() throws DBException, SQLException {
        when(dao.deleteUser(DEFAULT_STRING_ID)).thenReturn(1);
        dbService.deleteUser(DEFAULT_STRING_ID);
        verify(connection).commit();
    }

    @Test
    public void deleteUserIncorrect() throws DBException, SQLException {
        when(dao.deleteUser(DEFAULT_STRING_ID)).thenReturn(0);
        thrown.expect(DBException.class);
        dbService.deleteUser(DEFAULT_STRING_ID);
        thrown = ExpectedException.none();

        when(dao.deleteUser(DEFAULT_STRING_ID)).thenReturn(-1);
        thrown.expect(DBException.class);
        dbService.deleteUser(DEFAULT_STRING_ID);
        thrown = ExpectedException.none();
    }

    @Test
    public void updateUserCorrect() throws SQLException, DBException {
        when(dao.updateUser(DEFAULT_USER)).thenReturn(1);
        dbService.updateUser(DEFAULT_USER);
        verify(connection).commit();
    }

    @Test
    public void updateUserIncorrect() throws SQLException, DBException {
        when(dao.updateUser(DEFAULT_USER)).thenReturn(0);
        thrown.expect(DBException.class);
        dbService.updateUser(DEFAULT_USER);
        thrown = ExpectedException.none();

        when(dao.updateUser(DEFAULT_USER)).thenReturn(-1);
        thrown.expect(DBException.class);
        dbService.updateUser(DEFAULT_USER);
        thrown = ExpectedException.none();
    }

    private static UserAccount getNewUserAccount(LongId<UserAccount> id, String login, String password) {
        return new UserAccount(id, login, password);
    }
}