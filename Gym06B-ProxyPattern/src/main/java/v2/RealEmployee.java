package v2;

import java.util.List;

public class RealEmployee implements Employee {
    private final int _id;
    private final String _name;
    private final int _age;
    private final List<Integer> _subordinateIds;
    private final Database _database;
    private final List<Employee> _subordinates;

    public RealEmployee(int id, String name, int age, List<Integer> subordinateIds, Database database) {
        this._id = id;
        this._name = name;
        this._age = age;
        this._subordinateIds = subordinateIds;
        this._database = database;
        if(subordinateIds != null) {
            _subordinates = subordinateIds.stream()
                    .map(database::getEmployeeById)
                    .toList();
        } else {
            _subordinates = null;
        }
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
    public List<Employee> getSubordinates() {
        return _subordinates;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "_id=" + _id +
                ", _name='" + _name + '\'' +
                ", _age=" + _age +
                ", _subordinateIds=" + _subordinateIds +
                ", _subordinates=" + _subordinates +
                '}';
    }
}
