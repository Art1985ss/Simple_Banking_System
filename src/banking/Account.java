package banking;

import banking.services.ValidationService;

import java.math.BigDecimal;
import java.util.Random;

public class Account {
    private static final int NUMBER_LENGTH = 9;
    private static final int PIN_LENGTH = 4;
    private int id;
    private String number;
    private String pin;
    private BigDecimal balance = BigDecimal.ZERO;

    public Account() {
    }

    public Account(String number, String pin) {
        this.number = number;
        this.pin = pin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public static Account create() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("400000");
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            int n = random.nextInt(10);
            sb.append(n);
        }
        sb.append(ValidationService.LuhnAlgorithm(sb.toString()));
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < PIN_LENGTH; i++) {
            sb1.append(random.nextInt(10));
        }
        return new Account(sb.toString(), sb1.toString());
    }

    @Override
    public String toString() {
        return "Your card number:\n" + number +
                "\nYour card PIN:\n" + pin;
    }
}
