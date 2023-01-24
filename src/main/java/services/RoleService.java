package services;

import dao.RolesDAO;
import entities.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleService {

    private static final Logger LOGGER = LogManager.getLogger(RoleService.class.getName());
    private final RolesDAO rolesDAO;

    public RoleService(Connection connection) {
        this.rolesDAO = new RolesDAO(connection);
    }

    public long create(Role entity) {
        try {
            return rolesDAO.create(entity);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public Role get(long id) {
        try {
            return rolesDAO.read(id);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean update(Role entity) {
        try {
            return rolesDAO.update(entity);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean delete(long id) {
        try {
            return rolesDAO.delete(id);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<Role> getAll()  {
        try {
            return rolesDAO.getAll();
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }
}
