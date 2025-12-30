# Simple Banking System (Java)

A simple desktop-based banking system built using **Java** and **Swing (JOptionPane)**.  
This project demonstrates core programming concepts such as arrays, methods, input validation, and basic banking operations.

---

##  Features

- Create a new bank account
- Auto-generate account numbers (ACC001, ACC002, ...)
- Deposit money into an account
- Withdraw money with balance validation
- Check account details and balance
- Flexible account search:
  - Account number (ACC001)
  - Short format (A1 / a1)
  - Numeric input (1)
  - Account holder name (full or partial)

---

##  Technologies Used

- Java
- Swing (`JOptionPane`)
- Core Java (Arrays, Methods, Control Flow)

---

##  How It Works

The system stores account data using **parallel arrays**:
- `accountNumbers[]`
- `accountNames[]`
- `balances[]`

Each account shares the same index across these arrays.

The program runs in a loop displaying a menu where the user can choose different banking operations.

---
