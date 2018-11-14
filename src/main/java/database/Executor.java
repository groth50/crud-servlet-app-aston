package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *         Прослойка между DAO и самой базой. Управляет циклом жизни Statement из JDBC.
 *         Получает готовый запрос и перенаправляет его в Statement. А так же принимает
 *         функцию для обработки результата.
 */
public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public int execUpdate(String update) throws SQLException {
        int rows = 0;
        Statement stmt = connection.createStatement();
        stmt.execute(update);
        rows = stmt.getUpdateCount();
        stmt.close();
        return rows;
    }

    public <T> T execQuery(String query,
                           ResultHandler<T> handler)
            throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();
        return value;
    }

}
