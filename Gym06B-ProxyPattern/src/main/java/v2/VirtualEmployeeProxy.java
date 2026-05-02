package v2;

import java.util.List;

public class VirtualEmployeeProxy implements Employee {
    private int id;
    private Employee realEmployee;
    private VirtualDatabaseProxy database;

    public VirtualEmployeeProxy(int id, VirtualDatabaseProxy database) {
        this.id = id;
        this.database = database;
    }

    @Override
    public int getId() {
        lazyInitialization();
        return realEmployee.getId();
    }

    @Override
    public String getName() {
        lazyInitialization();
        return realEmployee.getName();
    }

    @Override
    public int getAge() {
        lazyInitialization();
        return realEmployee.getAge();
    }

    @Override
    public List<Integer> getSubordinateIds() {
        lazyInitialization();
        return realEmployee.getSubordinateIds();
    }

    @Override
    public List<Employee> getSubordinates() {
        lazyInitialization();
        return realEmployee.getSubordinates();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "_id=" + getId() +
                ", _name='" + getName() + '\'' +
                ", _age=" + getAge() +
                ", _subordinateIds=" + getSubordinateIds() +
                ", _subordinates=" + getSubordinates() +
                '}';
    }

    private void lazyInitialization() {
        if(realEmployee == null) {
            realEmployee = database.fetchRealData(id);
        }
    }
}
