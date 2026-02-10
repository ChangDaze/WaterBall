package ProxyPattern.v1;

public class Main {
    public static void main(String[] args){
        ExpenseTrackingCLI cli = new ExpenseTrackingCLI(new SuperExpenseTrackingSystem());
        cli.start();
    }
}
