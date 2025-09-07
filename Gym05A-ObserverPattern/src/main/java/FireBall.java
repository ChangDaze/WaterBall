import java.time.Duration;

public class FireBall  implements ChannelSubscriber  {
    private String name;

    public FireBall(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /*
    int result = d1.compareTo(d2);
    if (result < 0) {
        System.out.println("d1 < d2");
    } else if (result > 0) {
        System.out.println("d1 > d2");
    } else {
        System.out.println("d1 == d2");
    }
     */
    @Override
    public boolean update(Video video) {
        if (video.getLength().compareTo(Duration.ofSeconds(60)) <= 0){
            return false;
        }

        return true;
    }
}
