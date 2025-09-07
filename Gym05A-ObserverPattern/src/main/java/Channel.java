import java.util.ArrayList;
import java.util.List;

public class Channel {
    private String name;
    private List<ChannelSubscriber> subscribers;

    public String getName() {
        return name;
    }

    public List<ChannelSubscriber> getSubscribers() {
        return subscribers;
    }

    public Channel(String name) {
        this.name = name;
        subscribers = new ArrayList<>();
    }

    public void Subscribe(ChannelSubscriber subscriber){
        subscribers.add(subscriber);
        System.out.printf("%s 訂閱了 %s。%n", subscriber.getName(), name);
    }

    public void Upload(Video video){
        System.out.printf("頻道 %s 上架了一則新影片 \"%s\"。%n", name, video.getTitle());
        notify(video);
    }

    private void notify(Video video){
        for (int i = 0; i < subscribers.size(); i++){
            var subscriber = subscribers.get(i);
            if(!subscriber.update(video)){
                subscribers.remove(i);
                i--;
                System.out.printf("%s 解除訂閱了 %s。%n", subscriber.getName(), name);
            }
        }
    }
}
