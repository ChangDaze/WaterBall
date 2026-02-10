package ProxyPattern.v3;

import java.util.List;

public interface ExpenseTrackingSystem {
    List<Expense> getExpenses();
    void addExpense(Expense expense);
    void editExpense(Expense expense);
    void exportCSV(String filename);
}
