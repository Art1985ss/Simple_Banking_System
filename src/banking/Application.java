package banking;

import banking.enums.MenuOptions;
import banking.services.AccountManagementService;

import java.util.Arrays;
import java.util.Scanner;

public class Application {

    private final Scanner scanner = new Scanner(System.in);
    private final AccountRepository accountRepository = new AccountRepository();

    public void execute() {
        do {
            printMenu();
        } while (userSelection());
        System.out.println("Bye!");
    }

    private void printMenu() {
        Arrays.stream(MenuOptions.values()).forEach(System.out::println);
    }

    private boolean userSelection() {
        int num = Integer.parseInt(scanner.nextLine());
        MenuOptions menuOption = MenuOptions.getById(num);
        switch (menuOption) {
            case CREATE_ACCOUNT:
                createAccount();
                break;
            case LOG_INTO_ACCOUNT:
                try {
                    return logIn();
                } catch (BankingExceptions bankingExceptions) {
                    System.out.println(bankingExceptions.getMessage());
                }
                break;
            case EXIT:
                return false;
            default:
        }
        System.out.println();
        return true;
    }

    private void createAccount() {
        Account account = Account.create();
        System.out.println("Your card has been created");
        System.out.println(account);
        accountRepository.add(account);
    }

    private boolean logIn() throws BankingExceptions {
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        Account account = accountRepository.getByNumberAndPin(number, pin)
                .orElseThrow(() -> new BankingExceptions("Wrong card number or PIN!"));
        AccountManagementService accountManagementService =
                new AccountManagementService(account, scanner, accountRepository);
        int state = accountManagementService.execute();
        return state != 2;
    }

}
