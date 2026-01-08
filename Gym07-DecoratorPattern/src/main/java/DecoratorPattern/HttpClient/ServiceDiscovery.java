package DecoratorPattern.HttpClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServiceDiscovery extends HttpClientDecorator{
    public ServiceDiscovery(HttpClient next){super(next);}

    @Override
    public void send(HttpClientStatus status){
        List<String> availableServices = status.getAvailableServices();
        Map<String, LocalDateTime> blockList = status.getBlocklist();
        //檢查服務
        for(String availableService : availableServices){
            if(blockList.containsKey(availableService)){
                LocalDateTime blockTime = blockList.get(availableService);

                if(LocalDateTime.now().isAfter(blockTime.plusSeconds(10))){ //先擋2秒
                    System.out.printf("%s 阻擋到期從阻擋清單移除%n", availableService);
                    blockList.remove(availableService);
                }
            }

            if(Objects.equals(availableService, "127.0.0.1")){
                System.out.printf("%s ServiceDiscovery 失敗，加入阻擋清單%n", availableService);
                blockList.put(availableService, LocalDateTime.now());
            }
        }

        //設定目標
        for(String availableService : availableServices){
            if(blockList.containsKey(availableService)){
                System.out.printf("%s 阻擋%n", availableService);
                continue;
            }
            status.setUseHost(availableService);
            break;
        }

        //繼續下一個
        next.send(status);
    }
}
