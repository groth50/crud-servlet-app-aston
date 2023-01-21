package database;

import accounts.UserAccount;
import dao.UserDAO;
import dao.UserDAOI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

/**
 *         Объект, который создает коннект к базам, управляет им и позволяет менять базы. Прослойка между UserDAO и клиентом,
 *         который перенаправляет к DAO запросы от клиента.
 *         По сути нужен для безболезненной смены БД.
 *         При получении SQL Exception отлавливает и пробрасывает DBException дальше - изоляция. Каждая сущность
 *         работает со теми исключениями, которые ему положены.
 */
public class DBService {
    static final Logger LOGGER = LogManager.getLogger(DBService.class.getName());
    private final Connection connection;
    private final UserDAO userDAO;

    public DBService() {
        this.connection = getMysqlConnection();
        this.userDAO = new UserDAOI(connection);
        try {
            userDAO.createTable();
            LOGGER.debug("Create table done");
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public DBService(Connection connection, UserDAO userDAO) {
        this.connection = connection;
        this.userDAO = userDAO;
        try {
            userDAO.createTable();
            LOGGER.debug("Create table done");
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public UserAccount getUserById(long id) throws DBException {
        try {
            return userDAO.getUserById(id);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    public UserAccount getUserByLogin(String login) throws DBException {
        try {
            return userDAO.getUserByLogin(login);
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new DBException(e);
        }
    }

    public Collection<UserAccount> getAllUsers() throws DBException {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    public void addNewUser(String login, String password, UserAccount.Role role) throws DBException {
        int rows = 0;
        try {
            connection.setAutoCommit(false); //todo: concurrent
            rows = userDAO.insertUser(login, password, role.toString());
            if (rows == 0 || rows == -1) {
                connection.setAutoCommit(true);
                throw new DBException(" User not add. ");
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(e);
            try {
                connection.rollback();
            } catch (SQLException ignore) {
                LOGGER.error(ignore);
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    public void deleteUser(String id) throws DBException {
        int rows = 0;
        try {
            connection.setAutoCommit(false);
            rows = userDAO.deleteUser(id);
            if (rows == 0 || rows == -1) {
                connection.setAutoCommit(true);
                throw new DBException(" User not find. ");
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(e);
            try {
                connection.rollback();
            } catch (SQLException ignore) {
                LOGGER.error(ignore);
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    public void updateUser(UserAccount user) throws DBException {
        int rows = 0;
        try {
            connection.setAutoCommit(false);
            rows = userDAO.updateUser(user);
            if (rows == 0 || rows == -1) {
                connection.setAutoCommit(true);
                throw new DBException(" User not find. ");
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(e);
            try {
                connection.rollback();
            } catch (SQLException ignore) {
                LOGGER.error(ignore);
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    public void cleanUp() throws DBException {
        try {
            userDAO.dropTable();
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new DBException(e);
        }
    }

    public void printConnectInfo() {
        try {
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login
                    append("password=1234");       //password

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("Исключение при создании коннекта к базе" + e.toString());
            throw new RuntimeException(e);
        }
    }

}

