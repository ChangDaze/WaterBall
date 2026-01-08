package DecoratorPattern.HttpClient;

public class FakeHttpClient implements HttpClient{
    @Override
    public void send(HttpClientStatus status){
        System.out.printf("[SUCCESS] %s%n", status.getUseRequest());
    }
}
