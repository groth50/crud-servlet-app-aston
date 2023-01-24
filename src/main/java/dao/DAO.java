package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    long create(T entity) throws SQLException;

    T read(long id) throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(long id) throws SQLException;

    List<T> getAll() throws SQLException;
}
