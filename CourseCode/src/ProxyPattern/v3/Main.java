package ProxyPattern.v3;

public class Main {
    public static void main(String[] args){
        ExpenseTrackingCLI cli = new ExpenseTrackingCLI(new TrialVersionSuperExpenseTrackingProxy()); //改使用proxy
        cli.start();
    }
}
