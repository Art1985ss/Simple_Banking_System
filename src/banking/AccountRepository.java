package banking;

import banking.services.DBConnectionService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository {
    private static int id = 0;
    private static final String INSERT = "INSERT INTO card VALUES (?, ?, ? , ?)";
    private static final String SELECT = "SELECT * FROM card";
    private static final String SELECT_WHERE_NUMBER_AND_PIN = "SELECT * FROM card" +
            " WHERE number = ? AND pin = ?";
    private static final String SELECT_WHERE_NUMBER = "SELECT * FROM card" +
            " WHERE number = ?";
    private static final String DELETE = "DELETE FROM card WHERE number = ?";
    List<Account> list = new ArrayList<>();


    public Account get(int index) {
        return list.get(index);
    }

    public void add(Account account) throws BankingExceptions {
        if (account == null) {
            throw new BankingExceptions("Item is null!");
        }
        list.add(account);
        Connection con = DBConnectionService.getConnection()
                .orElseThrow(() -> new BankingExceptions("Could not get connection to database!"));
        try (PreparedStatement preparedStatement = con.prepareStatement(INSERT)) {
            updateId(con);
            account.setId(id);
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setString(2, account.getNumber());
            preparedStatement.setString(3, account.getPin());
            preparedStatement.setBigDecimal(4, account.getBalance());
            preparedStatement.execute();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
            throw new BankingExceptions("Error with adding item to database!");
        } finally {
            DBConnectionService.closeConnection(con);
        }
    }

    private static void updateId(Connection connection) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery("SELECT max(id) as max_id FROM card");
            id = resultSet.getInt("max_id");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        id += 1;
    }

    public Optional<Account> getByNumberAndPin(String number, String pin) throws BankingExceptions {
        Connection con = DBConnectionService.getConnection()
                .orElseThrow(() -> new BankingExceptions("Could not get connection to database!"));
        Account account = null;
        try (PreparedStatement preparedStatement = con.prepareStatement(SELECT_WHERE_NUMBER_AND_PIN)) {
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, pin);
            ResultSet rs = preparedStatement.executeQuery();
            account = new Account(rs.getString("number"), rs.getString("pin"));
            account.setId(rs.getInt("id"));
            account.setBalance(rs.getBigDecimal("balance"));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            throw new BankingExceptions("Error with finding item in database!");
        } finally {
            DBConnectionService.closeConnection(con);
        }
        return Optional.of(account);
    }

    public Optional<Account> getByNumber(String number) throws BankingExceptions {
        Connection con = DBConnectionService.getConnection()
                .orElseThrow(() -> new BankingExceptions("Could not get connection to database!"));
        Account account = null;
        try (PreparedStatement preparedStatement = con.prepareStatement(SELECT_WHERE_NUMBER)) {
            preparedStatement.setString(1, number);
            ResultSet rs = preparedStatement.executeQuery();
            account = new Account(rs.getString("number"), rs.getString("pin"));
            account.setId(rs.getInt("id"));
            account.setBalance(rs.getBigDecimal("balance"));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            throw new BankingExceptions("Error with finding item in database!");
        } finally {
            DBConnectionService.closeConnection(con);
        }
        return Optional.of(account);
    }

    public void deleteAccount(Account account) {
        Connection con = DBConnectionService.getConnection()
                .orElseThrow(() -> new BankingExceptions("Could not get connection to database!"));
        try (PreparedStatement preparedStatement = con.prepareStatement(DELETE)) {
            preparedStatement.setString(1, account.getNumber());
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            throw new BankingExceptions("Could not delete account");
        } finally {
            DBConnectionService.closeConnection(con);
        }
    }

}
