package banking.enums;

import banking.BankingExceptions;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AccountOptions {
    BALANCE(1, "Balance"),
    ADD_INCOME(2, "Add income"),
    DO_TRANSFER(3, "Do transfer"),
    CLOSE_ACCOUNT(4, "Close account"),
    LOG_OUT(5, "Log out"),
    EXIT(0, "Exit");


    private final int id;
    private final String text;

    AccountOptions(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static AccountOptions getById(int id) throws BankingExceptions {
        Predicate<AccountOptions> predicate = i -> i.id == id;
        return Arrays.stream(values()).filter(predicate).findAny().orElseThrow(() -> new BankingExceptions("Invalid option"));
    }

    @Override
    public String toString() {
        return id + ". " + text;
    }
}
