package dao;

import accounts.UserAccount;

import java.sql.SQLException;
import java.util.Collection;

public interface UserDAO {
    UserAccount getUserById(long id) throws SQLException;

    UserAccount getUserByLogin(String name) throws SQLException;

    Collection<UserAccount> getAllUsers() throws SQLException;

    int insertUser(String name, String password, String role) throws SQLException;

    int deleteUser(String id) throws SQLException;

    int updateUser(UserAccount user) throws SQLException;

    int createTable() throws SQLException;

    int dropTable() throws SQLException;
}