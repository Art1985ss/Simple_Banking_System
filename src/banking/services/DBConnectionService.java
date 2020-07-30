package banking.services;

import banking.BankingExceptions;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class DBConnectionService {
    private static final String TABLE_CREATION =
            "CREATE TABLE IF NOT EXISTS card ( " +
                    "id INTEGER, " +
                    "number TEXT, " +
                    "pin TEXT, " +
                    "balance INTEGER DEFAULT 0 " +
                    ")";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "1234";
    private static final String URL = "jdbc:sqlite:";
    private static String dbName;

    public static Optional<Connection> getConnection() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(URL + dbName);
        Connection con = null;
        try {
            con = dataSource.getConnection(USERNAME, PASSWORD);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return Optional.ofNullable(con);
    }

    public static void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void setDbName(String dbName) {
        DBConnectionService.dbName = dbName;
        createTable();
    }

    private static void createTable() throws BankingExceptions {
        Connection connection = getConnection()
                .orElseThrow(() -> new BankingExceptions("Could not establish connection with db."));
        try {
            connection.createStatement().execute(TABLE_CREATION);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
}
