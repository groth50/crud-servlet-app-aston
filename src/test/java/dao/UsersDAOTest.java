package dao;

import entities.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;

public class UsersDAOTest {
    private static final long DEFAULT_LONG_ID = 123;
    private static final String DEFAULT_LOGIN = "default";
    private static final User DEFAULT_USER = getNewUserAccount(DEFAULT_LONG_ID, DEFAULT_LOGIN);

    private static Connection connection;
    private static UsersDAO dao;

    @BeforeClass
    public static void beforeClass() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:h2:mem:test;"
                        +
                        "INIT=CREATE SCHEMA IF NOT EXISTS TESTSCHEMA\\;"
                        +
                        "runscript from 'src/test/resources/create_test_db.sql'\\;"
                        +
                        "runscript from 'src/test/resources/init_test_db.sql'"
                ,
                "sa",
                "sa");

        dao = new UsersDAO(connection);

    }

    @AfterClass
    public static void afterClass() throws Exception {
        connection.close();
    }

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void create() throws SQLException {
        long userId = dao.create(DEFAULT_USER);
        assertTrue(userId > 0);
    }

    @org.junit.Test
    public void read() throws SQLException {
        User user = dao.read(1);
        assertNotNull(user);
    }

    @org.junit.Test
    public void update() throws SQLException {
        String updatedLogin = "Updated";
        long userId = dao.create(DEFAULT_USER);
        assertTrue(userId > 0);
        DEFAULT_USER.setUserName(updatedLogin);
        boolean isUpdated = dao.update(DEFAULT_USER);
        assertTrue(isUpdated);
    }

    @org.junit.Test
    public void delete() throws SQLException {
        long userId = dao.create(DEFAULT_USER);
        assertTrue(userId > 0);
        boolean isDeleted = dao.delete(userId);
        assertTrue(isDeleted);
    }

    @org.junit.Test
    public void getAll() throws SQLException {
        List<User> all = dao.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }
    private static User getNewUserAccount(long id, String login) {
        return new User(id, login);
    }
}