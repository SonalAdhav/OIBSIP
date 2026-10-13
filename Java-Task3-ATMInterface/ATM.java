import java.util.Scanner;

public class ATM {
    private Bank bank;
    private Scanner sc = new Scanner(System.in);

    public ATM(Bank bank) {
        this.bank = bank;
    }

    public void start() {
        System.out.println("===== Welcome to the ATM =====");
        Account user = login();

        if (user == null) {
            System.out.println("Too many wrong attempts. Access denied.");
            return;
        }

        System.out.println("Login successful!");
        showMenu(user);
    }

    private Account login() {
        int attempts = 0;

        while (attempts < 3) {
            System.out.print("Enter User ID: ");
            String id = sc.next();
            System.out.print("Enter PIN: ");
            String pin = sc.next();

            Account acc = bank.findAccount(id);
            if (acc != null && acc.checkPin(pin)) {
                return acc;
            }

            attempts++;
            System.out.println("Wrong User ID or PIN. Attempts left: " + (3 - attempts));
        }
        return null;
    }

    private double readAmount() {
        System.out.print("Enter amount: ");
        while (!sc.hasNextDouble()) {
            System.out.println("Please enter a valid number!");
            sc.next();
            System.out.print("Enter amount: ");
        }
        return sc.nextDouble();
    }

    private void showMenu(Account user) {
        int choice = 0;

        while (choice != 5) {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number from 1 to 5.");
                sc.next();
                continue;
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    showHistory(user);
                    break;
                case 2:
                    double w = readAmount();
                    if (user.withdraw(w)) {
                        System.out.println("Withdraw successful. Balance: Rs. " + user.getBalance());
                    } else {
                        System.out.println("Insufficient Funds (or invalid amount).");
                    }
                    break;
                case 3:
                    double d = readAmount();
                    if (d > 0) {
                        user.deposit(d);
                        System.out.println("Deposit successful. Balance: Rs. " + user.getBalance());
                    } else {
                        System.out.println("Amount must be more than 0.");
                    }
                    break;
                case 4:
                    doTransfer(user);
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Choose 1 to 5.");
            }
        }
    }

    private void showHistory(Account user) {
        System.out.println("\n--- Transaction History ---");
        if (user.getHistory().isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : user.getHistory()) {
                System.out.println(t);
            }
        }
        System.out.println("Current balance: Rs. " + user.getBalance());
    }

    private void doTransfer(Account user) {
        System.out.print("Enter recipient Account ID: ");
        String toId = sc.next();
        Account to = bank.findAccount(toId);

        if (to == null) {
            System.out.println("Recipient account not found.");
            return;
        }
        if (to == user) {
            System.out.println("You cannot transfer to your own account.");
            return;
        }

        double amount = readAmount();
        if (user.transferOut(amount, toId)) {
            to.transferIn(amount, user.getUserId());
            System.out.println("Transfer successful. Balance: Rs. " + user.getBalance());
        } else {
            System.out.println("Insufficient Funds (or invalid amount).");
        }
    }
}