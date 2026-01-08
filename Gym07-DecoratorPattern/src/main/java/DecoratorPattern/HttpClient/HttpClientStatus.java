package DecoratorPattern.HttpClient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class HttpClientStatus {
    private String inputRequest;
    private String domain;
    private String useHost;
    private String prefix;
    private String suffix;
    private final Set<String> blacklist = new HashSet<>();
    private final Map<String, LocalDateTime> blocklist = new HashMap<String, LocalDateTime>();
    private final List<String> availableServices = new ArrayList<>();

    public void setRequest(String request){
        //設定原始資料
        inputRequest = request;

        //設定prefix
        int index = request.indexOf("://");
        prefix = request.substring(0, index + 3);
        request = request.substring(index + 3); //更新字串

        //設定domain, suffix
        int slashIndex = request.indexOf("/");
        domain = request.substring(0, slashIndex);
        suffix = request.substring(slashIndex);

        //設定預設值
        useHost = domain;
    }

    public void setAvailableServices(String availableServices){
        String[] list = availableServices.split(":");
        if(Objects.equals(list[0], domain)){
            this.availableServices.clear();
            String[] newAvailableServices = list[1].split(",");
            this.availableServices.addAll(Arrays.asList(newAvailableServices));

            //還沒discover前都預設阻擋
            this.blocklist.clear();
            for (String newAvailableService : newAvailableServices) {
                this.blocklist.put(newAvailableService, LocalDateTime.MIN);
            }
        }
    }

    public void setBlacklist(String blacklist){
        this.blacklist.clear();
        String[] list = blacklist.split(",");
        this.blacklist.addAll(Arrays.asList(list));
    }

    public String getUseRequest(){
        return prefix + useHost + suffix;
    }
}
