package ProxyPattern.v3;

import java.util.List;

public class VirtualSuperExpenseTrackingSystemProxy implements ExpenseTrackingSystem {
    private SuperExpenseTrackingSystem superSystem;

    @Override
    public List<Expense> getExpenses() {
        lazyInitialization();
        return superSystem.getExpenses();
    }

    @Override
    public void addExpense(Expense expense) {
        lazyInitialization();
        superSystem.addExpense(expense);
    }

    @Override
    public void editExpense(Expense expense) {
        lazyInitialization();
        superSystem.editExpense(expense);
    }

    @Override
    public void exportCSV(String filename) {
        lazyInitialization();
        superSystem.exportCSV(filename);
    }

    private void lazyInitialization() {
        if(superSystem == null) {
            superSystem = new SuperExpenseTrackingSystem();
        }
    }
}
