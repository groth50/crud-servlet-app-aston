package dao;

import entities.Role;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolesDAO implements DAO<Role> {

    private static final Logger LOGGER = LogManager.getLogger(UsersDAO.class.getName());

    private static final String READ_BY_ID = "SELECT * FROM roles WHERE role_id=(?);";
    private static final String READ_ALL = "SELECT * FROM roles;";
    public static final String CREATE = "INSERT INTO roles (role_name) "
                                                            + "VALUES (?);";

    private static final String DELETE = "DELETE FROM roles WHERE role_id = (?);";
    private static final String DELETE_USERS_WITH_ROLE_ID = "DELETE FROM users WHERE role_id = (?);";
    private static final String UPDATE = "UPDATE roles SET role_name = (?) " +
                                                            "WHERE role_id = (?);";

    private final Connection connection;

    public RolesDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long create(Role entity) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(RolesDAO.CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getRoleName());
            int i = preparedStatement.executeUpdate();

            if (i < 1) {
                throw new SQLException("Creating role failed.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long roleId = generatedKeys.getLong(1);
                    entity.setRoleId(roleId);
                    connection.commit();
                    return roleId;
                }
                else {
                    throw new SQLException("Creating role failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public Role read(long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(RolesDAO.READ_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long roleId = resultSet.getLong(1);
                String roleName = resultSet.getString(2);
                return new Role(roleId, roleName);
            } else {
                throw new SQLException("role not found");
            }
        }
    }

    @Override
    public boolean update(Role entity) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(RolesDAO.UPDATE)) {
            preparedStatement.setString(1, entity.getRoleName());
            preparedStatement.setLong(2, entity.getRoleId());
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(RolesDAO.DELETE_USERS_WITH_ROLE_ID)) {
            preparedStatement.setLong(1, id);
            int i = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(RolesDAO.DELETE)) {
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
    public List<Role> getAll() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(RolesDAO.READ_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {
                long roleId = resultSet.getLong(1);
                String name = resultSet.getString(2);
                roles.add(new Role(roleId, name));
            }
            return roles;
        }
    }
}
