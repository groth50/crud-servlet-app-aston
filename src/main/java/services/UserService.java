package services;

import dao.UsersDAO;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

public class UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class.getName());
    private final UsersDAO usersDAO;

    public UserService(Connection connection) {
        this.usersDAO = new UsersDAO(connection);
    }

    public long create(User entity) {
        try {
            return usersDAO.create(entity);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public User get(long id) {
        try {
            return usersDAO.read(id);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean update(User entity) {
        try {
            return usersDAO.update(entity);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean delete(long id) {
        try {
            return usersDAO.delete(id);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<User> getAll()  {
        try {
            return usersDAO.getAll();
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }
}
