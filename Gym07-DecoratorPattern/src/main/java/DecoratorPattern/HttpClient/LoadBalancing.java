package DecoratorPattern.HttpClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoadBalancing extends HttpClientDecorator{
    public LoadBalancing(HttpClient next){super(next);}

    @Override
    public void send(HttpClientStatus status){
        List<String> availableServices = status.getAvailableServices();
        Map<String, LocalDateTime> blockList = status.getBlocklist();

        //設定目標
        if(!availableServices.isEmpty()) //沒值會用原本的UseHost
        {
            int index = 0;
            while (index < availableServices.size()){
                String useHost = availableServices.getFirst();
                //輪巡
                availableServices.removeFirst();
                availableServices.add(useHost);
                if(blockList.containsKey(useHost))
                {
                    System.out.printf("%s 阻擋%n", useHost);
                }
                else
                {
                    status.setUseHost(useHost);
                    break;
                }
            }

            if(index == availableServices.size()){
                throw new RuntimeException("超出 availableServices index");
            }
        }

        //繼續下一個
        next.send(status);
    }
}
