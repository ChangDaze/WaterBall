package ProxyPattern.v2;

public class Main {
    public static void main(String[] args){
        ExpenseTrackingCLI cli = new ExpenseTrackingCLI(new VirtualSuperExpenseTrackingSystemProxy()); //改使用proxy
        cli.start();
    }
}
