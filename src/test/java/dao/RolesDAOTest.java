package dao;

import entities.Role;
import entities.User;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class RolesDAOTest {
    private static final long DEFAULT_ROLE_ID = 666;
    private static final String DEFAULT_ROLE_NAME = "TEST_ROLE";
    private static final Role DEFAULT_ROLE = getNewRole(DEFAULT_ROLE_ID, DEFAULT_ROLE_NAME);


    private static Connection connection;
    private static RolesDAO dao;

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

        dao = new RolesDAO(connection);

    }

    @AfterClass
    public static void afterClass() throws Exception {
        connection.close();
    }

    @Test
    public void create() throws SQLException {
        long roleId = dao.create(DEFAULT_ROLE);
        assertTrue(roleId > 0);
    }

    @Test
    public void read() throws SQLException {
        Role role = dao.read(1);
        assertNotNull(role);
    }

    @Test
    public void update() throws SQLException {
        String updated = "Updated";
        long roleId = dao.create(DEFAULT_ROLE);
        assertTrue(roleId > 0);
        DEFAULT_ROLE.setRoleName(updated);
        boolean isUpdated = dao.update(DEFAULT_ROLE);
        assertTrue(isUpdated);
    }

    @Test
    public void delete() throws SQLException {
        long roleId = dao.create(DEFAULT_ROLE);
        assertTrue(roleId > 0);
        boolean isDeleted = dao.delete(roleId);
        assertTrue(isDeleted);
    }

    @Test
    public void getAll() throws SQLException {
        List<Role> all = dao.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    private static Role getNewRole(long id, String login) {
        return new Role(DEFAULT_ROLE_ID, DEFAULT_ROLE_NAME);
    }
}