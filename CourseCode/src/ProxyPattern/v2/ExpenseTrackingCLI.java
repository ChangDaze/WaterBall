package ProxyPattern.v2;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ProxyPattern.utils.ScannerUtils.*;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.Collections.reverse;

public class ExpenseTrackingCLI {
    private final ExpenseTrackingSystem system;
    public static final int EXPENSES_PAGE_SIZE = 7;

    public ExpenseTrackingCLI(ExpenseTrackingSystem system) {
        this.system = system;
    }

    public void start(){
        while (true){
            try{
                System.out.println("選擇功能:(1)新增一筆帳目 (2)編輯一筆帳目 (3)匯出CSV:");
                int choice = inputIntegerBetween(1, 3, "請輸入數字1~3!");
                switch (choice){
                    case 1:
                        addExpense();
                        break;
                    case 2:
                        editExpense();
                        break;
                    case 3:
                        exportCSV();
                        break;
                }
            }catch (Exception err) {
                System.err.println(err.getMessage());
            }
        }
    }

    private void addExpense(){
        Expense expense = inputExpense();
        system.addExpense(expense);
        System.out.printf("成功新增一筆帳目 -- <%s> %.3f\n", expense.getDescription(), expense.getCost().doubleValue());
    }

    private Expense inputExpense() {
        System.out.println("請輸入這筆帳目的描述:");
        String description = inputLine();
        System.out.println("請輸入這筆帳目的支出金額:");
        BigDecimal cost = BigDecimal.valueOf(inputDoubleBetween(0, Double.MAX_VALUE, "請輸入此筆帳目的支出!"));
        return new Expense(description, cost);
    }

    private void editExpense(){
        List<Expense> expenses = system.getExpenses();
        reverse(expenses); //會特地從後面的資料顯示來提供選擇 因為資料比較多，這邊的expenses都是new出來一次性使用的 edit時是用id不是用index編輯的
        int editedExpenseIndex = selectEditExpenseIndex(expenses);
        Expense edited = inputEdiedExpense(expenses, editedExpenseIndex);
        system.editExpense(edited);
    }

    //製作一個互動CLI給使用者選要編輯哪個INDEX
    private int selectEditExpenseIndex(List<Expense> expenses){
        int page = 0;
        int editedExpenseIndex = -1;
        while (editedExpenseIndex == -1) {
            printExpenses(expenses, page);
            System.out.println("請輸入編輯的帳目編號 (上一頁 'P'|下一頁 'N'):");
            String choice = inputLine();
            if("P".equalsIgnoreCase(choice)) {
                page = Math.max(0, page -1);
            } else if("N".equalsIgnoreCase(choice)) {
                page++;
            }else {
                try {
                    editedExpenseIndex = parseInt(choice);//顯示的數字就是在list中的index
                }catch (NumberFormatException ignored){
                    System.err.println("請輸入數字!");
                }
            }
        }
        return editedExpenseIndex;
    }

    private Expense inputEdiedExpense(List<Expense> expenses, int editedExpenseIndex){
        Expense expense = expenses.get(editedExpenseIndex);
        System.out.println("請編輯這筆帳目的描述:");
        String description = inputLine();
        System.out.println("請編輯這筆帳目的支出金額:");
        BigDecimal cost = BigDecimal.valueOf(inputDoubleBetween(0, Double.MAX_VALUE, "請輸入此筆帳目的支出!"));
        return expense.edit(description, cost);
    }

    private void printExpenses(List<Expense> expenses, int page) {
        int end = min(expenses.size(), (page+1) * EXPENSES_PAGE_SIZE); //計算最後印到的index
        for(int i = page*EXPENSES_PAGE_SIZE; i < end; i++){ //起始page ~ 算出的index
            Expense expense = expenses.get(i);
            System.out.printf("[%d] %s\n", i, format("%s %.3f %s",
                    expense.getDescription(),
                    expense.getCost(),
                    expense.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))));
        }
    }

    private void exportCSV(){
        System.out.println("請提供輸出 CSV 檔案名稱:");
        String filename = input();
        system.exportCSV(filename);
    }
}
