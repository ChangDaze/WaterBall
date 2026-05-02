

public class Main {
    public static void main(String[] args) {
        /*
        v0.Database database = new v0.RealDatabase();
        v0.Employee employee1 = database.getEmployeeById(1);
        System.out.println(employee1);
        v0.Employee employee2 = database.getEmployeeById(2);
        System.out.println(employee2);

        v1.Database database = new v1.VirtualDatabaseProxy();
        v1.Employee employee1 = database.getEmployeeById(1);
        //用debug模式IDE為了給user看值會自動tostring(導致lazy效果難以察覺)之類的會有提示skip xxx，但搭配console print在非debug模式能看出效果
        System.out.println("lazy proves");
        System.out.println(employee1);
        v1.Employee employee2 = database.getEmployeeById(2);
        System.out.println("lazy proves");
        System.out.println(employee2);
        */

        v2.Database database = new v2.VirtualPasswordProtectionDatabaseProxy();
        v2.Employee employee1 = database.getEmployeeById(1);
        //用debug模式IDE為了給user看值會自動tostring(導致lazy效果難以察覺)之類的會有提示skip xxx，但搭配console print在非debug模式能看出效果
        System.out.println("lazy proves");
        System.out.println(employee1);
        v2.Employee employee2 = database.getEmployeeById(2);
        System.out.println("lazy proves");
        System.out.println(employee2);
    }
}
