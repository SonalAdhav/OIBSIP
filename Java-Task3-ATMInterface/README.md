# ATM Interface (Java)

**Intern:** Sonal Bhagwan Adhav
**Domain:** Java Development
**Internship:** AICTE Oasis Infobyte Internship Program (OIBSIP)

## About the Project
A console-based ATM simulation built in Java using Object-Oriented Programming. A user logs in with a User ID and PIN and can perform common banking operations.

## Features
- Login with User ID and PIN (access denied after 3 wrong attempts)
- Transaction History (all transactions stored in an ArrayList)
- Withdraw (with balance check)
- Deposit
- Transfer to another account (updates both accounts)
- "Insufficient Funds" message when balance is low
- Input validation for invalid numbers and menu choices
- Quit option

## Project Structure
| Class | Purpose |
|---|---|
| `Main` | Starts the program |
| `ATM` | Login, menu and user interaction |
| `Bank` | Stores accounts and finds an account by ID |
| `Account` | Holds User ID, PIN, balance and transaction history |
| `Transaction` | Represents a single transaction |

## Technologies Used
- Java (JDK 21)
- OOP concepts: classes, encapsulation, ArrayList
- VS Code

## Sample Accounts for Testing
| User ID | PIN |
|---|---|
| 1001 | 1234 |
| 1002 | 4321 |
| 1003 | 1111 |

## How to Run
1. Install JDK 17 or higher.
2. Clone this repository or download the folder.
3. Open a terminal in the project folder.
4. Compile:
```
   javac *.java
```
5. Run:
```
   java Main
```

## Screenshots
(Screenshots will be added here.)

## Author
Sonal Bhagwan Adhav