import v0.Database;
import v0.Employee;
import v0.RealDatabase;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Database database = new RealDatabase();
        Employee employee1 = database.getEmployeeById(1);
        System.out.println(employee1);
        Employee employee2 = database.getEmployeeById(2);
        System.out.println(employee2);
    }
}
