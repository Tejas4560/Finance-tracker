import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String id;
    private String type; // INCOME or EXPENSE
    private String category;
    private double amount;
    private String description;
    private LocalDate date;
    
    public Transaction(String id, String type, String category, double amount, String description, LocalDate date) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
    
    // Getters
    public String getId() { return id; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    
    // Setters
    public void setCategory(String category) { this.category = category; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%.2f,%s,%s", 
                id, type, category, amount, description, date.toString());
    }
    
    public String getFormattedString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return String.format("ID: %s | %s | %s | $%.2f | %s | %s", 
                id, type, category, amount, description, date.format(formatter));
    }
}