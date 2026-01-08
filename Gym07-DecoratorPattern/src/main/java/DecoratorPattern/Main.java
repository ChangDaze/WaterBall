package DecoratorPattern;

import DecoratorPattern.HttpClient.*;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        HttpClientStatus httpClientStatus = new HttpClientStatus();
        httpClientStatus.setRequest("http://test.tw/waterball");
        httpClientStatus.setAvailableServices("test.tw:127.0.0.1,127.0.0.2,127.0.0.3");
        httpClientStatus.setBlacklist("127.0.0.3");

        System.out.println("====== Send 1 ======");
        new FakeHttpClient().send(httpClientStatus);
        System.out.println("====== Send 2 ======");
        new ServiceDiscovery(new FakeHttpClient()).send(httpClientStatus);
        System.out.println("====== Send 3 ======");
        new LoadBalancing(new ServiceDiscovery(new FakeHttpClient())).send(httpClientStatus);
        Thread.sleep(2000);
        System.out.println("====== Send 4 ======");
        new ServiceDiscovery(new LoadBalancing(new Blacklist(new FakeHttpClient()))).send(httpClientStatus);
    }
}
