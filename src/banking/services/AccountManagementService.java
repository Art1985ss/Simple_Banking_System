package banking.services;

import banking.Account;
import banking.AccountRepository;
import banking.BankingExceptions;
import banking.enums.AccountOptions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;

public class AccountManagementService {
    private final Account account;
    private final Scanner scanner;
    private final AccountRepository accountRepository;


    public AccountManagementService(Account account, Scanner scanner, AccountRepository accountRepository) {
        this.account = account;
        this.scanner = scanner;
        this.accountRepository = accountRepository;
    }

    private void printMenu() {
        Arrays.stream(AccountOptions.values()).forEach(System.out::println);
    }

    public int execute() throws RuntimeException {
        System.out.println("You have successfully logged in!");
        int state = 0;
        do {
            printMenu();
            try {
                state = userSelection();
            } catch (BankingExceptions e) {
                System.out.println(e.getMessage());
            }
        } while (state == 0);
        return state;
    }

    private int userSelection() throws BankingExceptions {
        int num = Integer.parseInt(scanner.nextLine());
        AccountOptions accountOption = AccountOptions.getById(num);
        switch (accountOption) {
            case BALANCE:
                System.out.println("Balance : " + account.getBalance());
                break;
            case ADD_INCOME:
                addIncome();
                break;
            case DO_TRANSFER:
                transfer();
                break;
            case CLOSE_ACCOUNT:
                deleteAccount();
                System.out.println("Account has been closed!");
                return 1;
            case LOG_OUT:
                System.out.println("You have successfully logged out!");
                return 1;
            case EXIT:
            default:
                return 2;
        }
        System.out.println();
        return 0;
    }

    private void addIncome() {
        System.out.println("Enter income:");
        account.setBalance(account.getBalance().add(new BigDecimal(scanner.nextLine())));
        System.out.println("Income was added!");
    }

    private void transfer() throws BankingExceptions {
        System.out.println("Enter card number:");
        String number = scanner.nextLine();
        if (!String.valueOf(number.charAt(number.length() - 1))
                .equals(String.valueOf(ValidationService.LuhnAlgorithm(number.substring(0, number.length() - 1))))) {
            throw new BankingExceptions("Probably you made mistake in the card number. Please try again!");
        }
        Account accountRecipient = accountRepository.getByNumber(number)
                .orElseThrow(() -> new BankingExceptions("Such a card does not exist."));
        System.out.println("Enter how much money you want to transfer:");
        BigDecimal bigDecimal = new BigDecimal(scanner.nextLine());
        if (bigDecimal.compareTo(account.getBalance()) > 0) {
            throw new BankingExceptions("Not enough money!");
        }
        account.setBalance(account.getBalance().subtract(bigDecimal));
        accountRecipient.setBalance(accountRecipient.getBalance().add(bigDecimal));
        System.out.println("Success!");
    }

    private void deleteAccount() throws BankingExceptions {
        accountRepository.deleteAccount(account);
    }


}
