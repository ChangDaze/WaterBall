package v2;

public class VirtualDatabaseProxy implements Database {
    private RealDatabase realDatabase;

    @Override
    public Employee getEmployeeById(int id) {
        //對介面的方法Lazy
        return new VirtualEmployeeProxy(id, this);
    }

    public Employee fetchRealData(int id) {
        lazyInitialization();
        return realDatabase.getEmployeeById(id);
    }

    private void lazyInitialization() {
        if(realDatabase == null) {
            realDatabase = new RealDatabase();
        }
    }
}
