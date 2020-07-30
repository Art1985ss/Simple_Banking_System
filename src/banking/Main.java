package banking;


import banking.services.DBConnectionService;

public class Main {
    public static void main(String[] args) {
        DBConnectionService.setDbName(getDbName(args));
        Application application = new Application();
        application.execute();

    }

    private static String getDbName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-fileName".equals(args[i])) {
                return args[i + 1];
            }
        }
        return "banking.db";
    }
}
