import java.time.Duration;

public class WaterBall  implements ChannelSubscriber {
    private String name;

    public WaterBall(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean update(Video video) {
        if(video.getLength().compareTo(Duration.ofMinutes(3)) >= 0){
            System.out.printf("%s 對影片 \"%s\" 按讚。%n", name, video.getTitle());
        }

        return true;
    }
}
