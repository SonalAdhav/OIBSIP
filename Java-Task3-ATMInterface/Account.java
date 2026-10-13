import java.util.ArrayList;

public class Account {
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<Transaction> history = new ArrayList<>();

    public Account(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public boolean checkPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;   // Insufficient Funds
        }
        balance -= amount;
        history.add(new Transaction("Withdraw", amount));
        return true;
    }

    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("Deposit", amount));
    }

    public boolean transferOut(double amount, String toId) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        history.add(new Transaction("Transfer to " + toId, amount));
        return true;
    }

    public void transferIn(double amount, String fromId) {
        balance += amount;
        history.add(new Transaction("Transfer from " + fromId, amount));
    }

    public ArrayList<Transaction> getHistory() {
        return history;
    }
}