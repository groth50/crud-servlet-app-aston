package dao;

import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO implements DAO<User> {
    private static final Logger LOGGER = LogManager.getLogger(UsersDAO.class.getName());

    private static final String READ_BY_NAME = "SELECT * FROM users WHERE user_name=(?);";
    private static final String READ_BY_ID = "SELECT * FROM users WHERE user_id=(?);";
    private static final String READ_ALL = "SELECT * FROM users;";
    public static final String CREATE = "INSERT INTO users (user_name, role_id) "
            + "VALUES ((?), (?));";

    private static final String DELETE = "DELETE FROM users WHERE user_id = (?);";
    private static final String UPDATE = "UPDATE users SET user_name = (?), role_id = (?) " +
                                            "WHERE user_id = (?);";

    private final Connection connection;

    public UsersDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long create(User entity) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UsersDAO.CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getUserName());
            preparedStatement.setLong(2, entity.getRoleId());
            int i = preparedStatement.executeUpdate();

            if (i < 1) {
                throw new SQLException("Creating user failed.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long userId = generatedKeys.getLong(1);
                    entity.setUserId(userId);
                    connection.commit();
                    return userId;
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            connection.rollback();
            throw e;
        }
    }

    @Override
    public User read(long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UsersDAO.READ_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long userId = resultSet.getLong(1);
                String name = resultSet.getString(2);
                long roleId = resultSet.getLong(3);
                return new User(userId, name, roleId);
            } else {
                throw new SQLException("user not found");
            }
        }
    }

    @Override
    public boolean update(User entity) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UsersDAO.UPDATE)) {
            preparedStatement.setString(1, entity.getUserName());
            preparedStatement.setLong(2, entity.getRoleId());
            preparedStatement.setLong(3, entity.getUserId());
            int i = preparedStatement.executeUpdate();
            connection.commit();
            return i > 0;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UsersDAO.DELETE)) {
            preparedStatement.setLong(1, id);
            int i = preparedStatement.executeUpdate();
            connection.commit();
            return i > 0;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UsersDAO.READ_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> allUsers = new ArrayList<>();

           while (resultSet.next()) {
               long userId = resultSet.getLong(1);
               String name = resultSet.getString(2);
               long roleId = resultSet.getLong(3);
               allUsers.add(new User(userId, name, roleId));
           }
           return allUsers;
        }
    }
}
