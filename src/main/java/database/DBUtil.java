package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    static final Logger LOGGER = LogManager.getLogger(DBUtil.class.getName());
    private final Connection connection;

    public DBUtil() {
        this.connection = getMysqlConnection();
        printConnectInfo();
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
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

//            url.
//                    append("jdbc:mysql://").        //db type
//                    append("localhost:").           //host name
//                    append("3306/").                //port
//                    append("db_example?").          //db name
//                    append("user=root&").          //login
//                    append("password=root");       //password
//
//            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_example", "root", "root");;
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("Исключение при создании коннекта к базе" + e.toString());
            throw new RuntimeException(e);
        }
    }
}

