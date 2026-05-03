package v2;

public class ConsoleExporter extends CompositeExporter {
    @Override
    public void export(String message) {
        System.out.println(message);
    }
}
