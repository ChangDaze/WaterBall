package DecoratorPattern.HttpClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Blacklist extends HttpClientDecorator{
    public Blacklist(HttpClient next){super(next);}

    @Override
    public void send(HttpClientStatus status){
        if(status.getBlacklist().contains(status.getUseHost())){
            throw new RuntimeException(String.format("%s 在黑名單", status.getUseHost()));
        }

        //繼續下一個
        next.send(status);
    }
}
