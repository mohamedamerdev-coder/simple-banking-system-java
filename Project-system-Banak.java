import javax.swing.JOptionPane;

public class Main {

    static final int MAX = 100;                   // max accounts
    static String[] accountNumbers = new String[MAX];
    static String[] accountNames   = new String[MAX];
    static double[] balances       = new double[MAX];
    static int accountCount = 0;                 // number of created accounts

    public static void main(String[] args) {
        while (true) {
            String choiceStr = JOptionPane.showInputDialog(
                    null,
                    "Simple Banking Menu\n\n" +
                            "1 - Create account\n" +
                            "2 - Deposit\n" +
                            "3 - Withdraw\n" +
                            "4 - Check balance\n" +
                            "0 - Exit\n\n" +
                            "Enter choice (0-4):",
                    "Menu",
                    JOptionPane.QUESTION_MESSAGE);
            if (choiceStr == null) break; // user closed/cancelled -> exit
            int choice = parseIntSafe(choiceStr, -1);
            switch (choice) {
                case 1: createAccount(); break;
                case 2: deposit(); break;
                case 3: withdraw(); break;
                case 4: checkBalance(); break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Enter 0-4.");
            }
        }
    }

    // Create account and store data in parallel arrays (same index = same account)
    public static void createAccount() {
        if (accountCount >= MAX) {
            JOptionPane.showMessageDialog(null, "System full. Cannot create more accounts.");
            return;
        }
        String name = JOptionPane.showInputDialog(null, "Enter account holder name:");
        if (name == null) return;
        name = name.trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty.");
            return;
        }
        String depositStr = JOptionPane.showInputDialog(null, "Enter initial deposit (0 or more):");
        if (depositStr == null) return;
        double dep = parseDoubleSafe(depositStr, -1);
        if (dep < 0) {
            JOptionPane.showMessageDialog(null, "Invalid deposit amount.");
            return;
        }

        String accNum = String.format("ACC%03d", accountCount + 1); // ACC001 ...
        accountNumbers[accountCount] = accNum;
        accountNames[accountCount] = name;
        balances[accountCount] = dep;
        accountCount++;

        JOptionPane.showMessageDialog(null,
                "Account created!\nAccount Number: " + accNum +
                        "\nName: " + name + "\nBalance: $" + String.format("%.2f", dep));
    }

    // Deposit money to an account (lookup is flexible)
    public static void deposit() {
        if (accountCount == 0) {
            JOptionPane.showMessageDialog(null, "No accounts yet. Create one first.");
            return;
        }
        String key = JOptionPane.showInputDialog(null,
                "Enter account number (ACC001 / a1 / 1) or account holder name:");
        if (key == null) return;
        int idx = findAccountIndexFlexible(key.trim());
        if (idx == -1) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }
        String amtStr = JOptionPane.showInputDialog(null,
                "Account: " + accountNumbers[idx] + "\nName: " + accountNames[idx] +
                        "\nCurrent Balance: $" + String.format("%.2f", balances[idx]) +
                        "\nEnter deposit amount:");
        if (amtStr == null) return;
        double amt = parseDoubleSafe(amtStr, -1);
        if (amt <= 0) {
            JOptionPane.showMessageDialog(null, "Deposit must be > 0.");
            return;
        }
        balances[idx] += amt;
        JOptionPane.showMessageDialog(null,
                "Deposit successful.\nNew Balance: $" + String.format("%.2f", balances[idx]));
    }

    // Withdraw money with sufficient funds check
    public static void withdraw() {
        if (accountCount == 0) {
            JOptionPane.showMessageDialog(null, "No accounts yet. Create one first.");
            return;
        }
        String key = JOptionPane.showInputDialog(null,
                "Enter account number (ACC001 / a1 / 1) or account holder name:");
        if (key == null) return;
        int idx = findAccountIndexFlexible(key.trim());
        if (idx == -1) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }
        String amtStr = JOptionPane.showInputDialog(null,
                "Account: " + accountNumbers[idx] + "\nName: " + accountNames[idx] +
                        "\nAvailable: $" + String.format("%.2f", balances[idx]) +
                        "\nEnter withdrawal amount:");
        if (amtStr == null) return;
        double amt = parseDoubleSafe(amtStr, -1);
        if (amt <= 0) {
            JOptionPane.showMessageDialog(null, "Amount must be > 0.");
            return;
        }
        if (amt > balances[idx]) {
            JOptionPane.showMessageDialog(null, "Insufficient funds.");
            return;
        }
        balances[idx] -= amt;
        JOptionPane.showMessageDialog(null,
                "Withdrawal successful.\nNew Balance: $" + String.format("%.2f", balances[idx]));
    }

    // Check balance/details
    public static void checkBalance() {
        if (accountCount == 0) {
            JOptionPane.showMessageDialog(null, "No accounts yet.");
            return;
        }
        String key = JOptionPane.showInputDialog(null,
                "Enter account number (ACC001 / a1 / 1) or account holder name:");
        if (key == null) return;
        int idx = findAccountIndexFlexible(key.trim());
        if (idx == -1) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }
        JOptionPane.showMessageDialog(null,
                "Account: " + accountNumbers[idx] +
                        "\nName: " + accountNames[idx] +
                        "\nBalance: $" + String.format("%.2f", balances[idx]));
    }

    // Flexible account lookup:
    // - If input like "ACC005" -> match accountNumbers
    // - If input like "a5" or "A5" or "5" -> treat as account number index 5 (ACC005)
    // - Else try exact name match (case-insensitive), then substring match
    public static int findAccountIndexFlexible(String key) {
        if (key.isEmpty()) return -1;
        String k = key.trim();

        // 1) direct account number match
        for (int i = 0; i < accountCount; i++) {
            if (accountNumbers[i].equalsIgnoreCase(k)) return i;
        }

        // 2) patterns like "A5", "a5" -> number after letter
        if (k.length() >= 2 && Character.toUpperCase(k.charAt(0)) == 'A') {
            String rest = k.substring(1).trim();
            int n = parseIntSafe(rest, -1);
            if (n > 0) {
                int idx = n - 1;
                if (idx >= 0 && idx < accountCount) return idx;
            }
        }

        // 3) pure number like "5" -> treat as index
        int n = parseIntSafe(k, -1);
        if (n > 0) {
            int idx = n - 1;
            if (idx >= 0 && idx < accountCount) return idx;
        }

        // 4) search by exact name (case-insensitive)
        for (int i = 0; i < accountCount; i++) {
            if (accountNames[i].equalsIgnoreCase(k)) return i;
        }

        // 5) search by name contains (case-insensitive)
        String kl = k.toLowerCase();
        for (int i = 0; i < accountCount; i++) {
            if (accountNames[i].toLowerCase().contains(kl)) return i;
        }

        return -1; // not found
    }

    // Helper: parse int safely, return defaultVal on error
    public static int parseIntSafe(String s, int defaultVal) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return defaultVal; }
    }

    // Helper: parse double safely, return defaultVal on error
    public static double parseDoubleSafe(String s, double defaultVal) {
        try { return Double.parseDouble(s.trim()); }
        catch (Exception e) { return defaultVal; }
    }
}