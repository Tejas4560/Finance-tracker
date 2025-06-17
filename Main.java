import java.util.Scanner;

public class Main {
    private static FinanceTracker tracker = new FinanceTracker();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to Personal Finance Tracker ===");
        
        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    tracker.viewAllTransactions();
                    break;
                case 3:
                    viewTransactionsByCategory();
                    break;
                case 4:
                    generateMonthlyReport();
                    break;
                case 5:
                    generateYearlyReport();
                    break;
                case 6:
                    tracker.getCurrentBalance();
                    break;
                case 7:
                    deleteTransaction();
                    break;
                case 8:
                    System.out.println("Thank you for using Personal Finance Tracker!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n=== PERSONAL FINANCE TRACKER ===");
        System.out.println("1. Add Transaction");
        System.out.println("2. View All Transactions");
        System.out.println("3. View Transactions by Category");
        System.out.println("4. Generate Monthly Report");
        System.out.println("5. Generate Yearly Report");
        System.out.println("6. Current Balance");
        System.out.println("7. Delete Transaction");
        System.out.println("8. Exit");
    }
    
    private static void addTransaction() {
        System.out.println("\n=== ADD TRANSACTION ===");
        
        // Get transaction type
        System.out.println("1. Income");
        System.out.println("2. Expense");
        int typeChoice = getIntInput("Select type (1-2): ");
        
        String type = (typeChoice == 1) ? "INCOME" : "EXPENSE";
        
        // Display and get category
        tracker.displayCategories(type);
        int categoryChoice = getIntInput("Select category: ");
        
        String category;
        if (categoryChoice >= 1 && categoryChoice <= tracker.getCategories(type).size()) {
            category = tracker.getCategories(type).get(categoryChoice - 1);
        } else {
            System.out.println("Invalid category selection.");
            return;
        }
        
        // Get amount
        double amount = getDoubleInput("Enter amount: $");
        
        // Get description
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        
        tracker.addTransaction(type, category, amount, description);
    }
    
    private static void viewTransactionsByCategory() {
        System.out.print("Enter category name: ");
        String category = scanner.nextLine();
        tracker.viewTransactionsByCategory(category);
    }
    
    private static void generateMonthlyReport() {
        int month = getIntInput("Enter month (1-12): ");
        int year = getIntInput("Enter year: ");
        
        if (month < 1 || month > 12) {
            System.out.println("Invalid month. Please enter a value between 1-12.");
            return;
        }
        
        tracker.generateMonthlyReport(month, year);
    }
    
    private static void generateYearlyReport() {
        int year = getIntInput("Enter year: ");
        tracker.generateYearlyReport(year);
    }
    
    private static void deleteTransaction() {
        System.out.print("Enter transaction ID to delete: ");
        String id = scanner.nextLine();
        tracker.deleteTransaction(id);
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.out.println("Amount cannot be negative.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
            }
        }
    }
}