package DecoratorPattern.HttpClient;

public abstract class HttpClientDecorator implements HttpClient{
    protected HttpClient next;
    public HttpClientDecorator(HttpClient next){this.next = next;}

    @Override
    public abstract void send(HttpClientStatus status);
}
