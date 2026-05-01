package v0;

import java.util.List;

public class RealEmployee implements Employee {
    private final int _id;
    private final String _name;
    private final int _age;
    private final List<Integer> _subordinateIds;

    public RealEmployee(int id, String name, int age, List<Integer> subordinateIds) {
        this._id = id;
        this._name = name;
        this._age = age;
        this._subordinateIds = subordinateIds;
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public int getAge() {
        return _age;
    }

    @Override
    public List<Integer> getSubordinateIds() {
        return _subordinateIds;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "_id=" + _id +
                ", _name='" + _name + '\'' +
                ", _age=" + _age +
                ", _subordinateIds=" + _subordinateIds +
                '}';
    }
}
