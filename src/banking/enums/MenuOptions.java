package banking.enums;

import banking.BankingExceptions;

import java.util.Arrays;
import java.util.function.Predicate;

public enum MenuOptions {
    CREATE_ACCOUNT(1, "Create an account"),
    LOG_INTO_ACCOUNT(2, "Log into account"),
    EXIT(0, "Exit");


    private final int id;
    private final String text;

    MenuOptions(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static MenuOptions getById(int id) throws BankingExceptions {
        Predicate<MenuOptions> predicate = i -> i.id == id;
        return Arrays.stream(values()).filter(predicate).findAny().orElseThrow(() -> new BankingExceptions("Invalid option"));
    }

    @Override
    public String toString() {
        return id + ". " + text;
    }
}
