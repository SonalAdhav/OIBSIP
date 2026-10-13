import java.util.ArrayList;

public class Bank {
    private ArrayList<Account> accounts = new ArrayList<>();

    public Bank() {
        // Sample accounts for testing
        accounts.add(new Account("1001", "1234", 5000));
        accounts.add(new Account("1002", "4321", 3000));
        accounts.add(new Account("1003", "1111", 10000));
    }

    public Account findAccount(String userId) {
        for (Account acc : accounts) {
            if (acc.getUserId().equals(userId)) {
                return acc;
            }
        }
        return null;   // account not found
    }
}