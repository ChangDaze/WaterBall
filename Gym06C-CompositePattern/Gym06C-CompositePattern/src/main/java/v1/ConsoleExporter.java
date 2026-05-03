package v1;

public class ConsoleExporter extends CompositeExporter{
    @Override
    public void export(String message) {
        System.out.println(message);
    }
}
