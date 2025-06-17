import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class FinanceTracker {
    private List<Transaction> transactions;
    private final String DATA_FILE = "transactions.txt";
    private int nextId;
    
    // Predefined categories
    private final List<String> INCOME_CATEGORIES = Arrays.asList(
        "Salary", "Freelance", "Investment", "Gift", "Other Income"
    );
    
    private final List<String> EXPENSE_CATEGORIES = Arrays.asList(
        "Food", "Transportation", "Housing", "Utilities", "Entertainment", 
        "Healthcare", "Shopping", "Education", "Other Expense"
    );
    
    public FinanceTracker() {
        transactions = new ArrayList<>();
        loadTransactions();
        nextId = transactions.size() + 1;
    }
    
    // Add new transaction
    public void addTransaction(String type, String category, double amount, String description) {
        LocalDate date = LocalDate.now();
        String id = String.valueOf(nextId++);
        Transaction transaction = new Transaction(id, type, category, amount, description, date);
        transactions.add(transaction);
        saveTransactions();
        System.out.println("Transaction added successfully!");
    }
    
    // View all transactions
    public void viewAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        System.out.println("\n=== ALL TRANSACTIONS ===");
        for (Transaction t : transactions) {
            System.out.println(t.getFormattedString());
        }
    }
    
    // View transactions by category
    public void viewTransactionsByCategory(String category) {
        List<Transaction> filtered = transactions.stream()
            .filter(t -> t.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
            
        if (filtered.isEmpty()) {
            System.out.println("No transactions found for category: " + category);
            return;
        }
        
        System.out.println("\n=== TRANSACTIONS FOR " + category.toUpperCase() + " ===");
        for (Transaction t : filtered) {
            System.out.println(t.getFormattedString());
        }
    }
    
    // Generate monthly report
    public void generateMonthlyReport(int month, int year) {
        List<Transaction> monthlyTransactions = transactions.stream()
            .filter(t -> t.getDate().getMonthValue() == month && t.getDate().getYear() == year)
            .collect(Collectors.toList());
            
        if (monthlyTransactions.isEmpty()) {
            System.out.println("No transactions found for " + month + "/" + year);
            return;
        }
        
        double totalIncome = monthlyTransactions.stream()
            .filter(t -> t.getType().equals("INCOME"))
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double totalExpense = monthlyTransactions.stream()
            .filter(t -> t.getType().equals("EXPENSE"))
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        System.out.println("\n=== MONTHLY REPORT FOR " + month + "/" + year + " ===");
        System.out.println("Total Income: $" + String.format("%.2f", totalIncome));
        System.out.println("Total Expenses: $" + String.format("%.2f", totalExpense));
        System.out.println("Net Balance: $" + String.format("%.2f", (totalIncome - totalExpense)));
        
        // Category-wise breakdown
        System.out.println("\n--- INCOME BY CATEGORY ---");
        generateCategoryReport(monthlyTransactions, "INCOME");
        
        System.out.println("\n--- EXPENSES BY CATEGORY ---");
        generateCategoryReport(monthlyTransactions, "EXPENSE");
    }
    
    // Generate yearly report
    public void generateYearlyReport(int year) {
        List<Transaction> yearlyTransactions = transactions.stream()
            .filter(t -> t.getDate().getYear() == year)
            .collect(Collectors.toList());
            
        if (yearlyTransactions.isEmpty()) {
            System.out.println("No transactions found for year " + year);
            return;
        }
        
        double totalIncome = yearlyTransactions.stream()
            .filter(t -> t.getType().equals("INCOME"))
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double totalExpense = yearlyTransactions.stream()
            .filter(t -> t.getType().equals("EXPENSE"))
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        System.out.println("\n=== YEARLY REPORT FOR " + year + " ===");
        System.out.println("Total Income: $" + String.format("%.2f", totalIncome));
        System.out.println("Total Expenses: $" + String.format("%.2f", totalExpense));
        System.out.println("Net Balance: $" + String.format("%.2f", (totalIncome - totalExpense)));
        
        // Monthly breakdown
        System.out.println("\n--- MONTHLY BREAKDOWN ---");
        for (int month = 1; month <= 12; month++) {
            List<Transaction> monthData = yearlyTransactions.stream()
                .filter(t -> t.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
                
            if (!monthData.isEmpty()) {
                double monthIncome = monthData.stream()
                    .filter(t -> t.getType().equals("INCOME"))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
                double monthExpense = monthData.stream()
                    .filter(t -> t.getType().equals("EXPENSE"))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
                    
                System.out.printf("Month %d: Income $%.2f, Expenses $%.2f, Net $%.2f%n", 
                    month, monthIncome, monthExpense, (monthIncome - monthExpense));
            }
        }
    }
    
    // Helper method for category-wise report
    private void generateCategoryReport(List<Transaction> transactions, String type) {
        Map<String, Double> categoryTotals = transactions.stream()
            .filter(t -> t.getType().equals(type))
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingDouble(Transaction::getAmount)
            ));
            
        categoryTotals.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.printf("%s: $%.2f%n", entry.getKey(), entry.getValue())
            );
    }
    
    // Delete transaction
    public void deleteTransaction(String id) {
        boolean removed = transactions.removeIf(t -> t.getId().equals(id));
        if (removed) {
            saveTransactions();
            System.out.println("Transaction deleted successfully!");
        } else {
            System.out.println("Transaction not found with ID: " + id);
        }
    }
    
    // Get current balance
    public void getCurrentBalance() {
        double totalIncome = transactions.stream()
            .filter(t -> t.getType().equals("INCOME"))
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double totalExpense = transactions.stream()
            .filter(t -> t.getType().equals("EXPENSE"))
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        System.out.println("\n=== CURRENT BALANCE ===");
        System.out.println("Total Income: $" + String.format("%.2f", totalIncome));
        System.out.println("Total Expenses: $" + String.format("%.2f", totalExpense));
        System.out.println("Current Balance: $" + String.format("%.2f", (totalIncome - totalExpense)));
    }
    
    // Save transactions to file
    private void saveTransactions() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Transaction t : transactions) {
                writer.println(t.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }
    
    // Load transactions from file
    private void loadTransactions() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Transaction t = new Transaction(
                        parts[0], parts[1], parts[2], 
                        Double.parseDouble(parts[3]), 
                        parts[4], LocalDate.parse(parts[5])
                    );
                    transactions.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }
    
    // Display categories
    public void displayCategories(String type) {
        System.out.println("\nAvailable " + type + " categories:");
        List<String> categories = type.equals("INCOME") ? INCOME_CATEGORIES : EXPENSE_CATEGORIES;
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
    }
    
    public List<String> getCategories(String type) {
        return type.equals("INCOME") ? INCOME_CATEGORIES : EXPENSE_CATEGORIES;
    }
}
