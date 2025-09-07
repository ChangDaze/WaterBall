import java.time.Duration;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Main {
    public static void main(String[] args) {
        //先只用push
        WaterBall waterBall = new WaterBall("水球");
        FireBall fireBall = new FireBall("火球");
        Channel pewDiePie = new Channel("PewDiePie");
        Channel waterballAcademy = new Channel("水球軟體學院");

        waterballAcademy.Subscribe(waterBall);
        pewDiePie.Subscribe(waterBall);
        waterballAcademy.Subscribe(fireBall);
        pewDiePie.Subscribe(fireBall);

        waterballAcademy.Upload(new Video("C1M1S2", "這個世界正是物件導向的呢！", Duration.ofMinutes(4)));
        pewDiePie.Upload(new Video("Hello guys", "Clickbait", Duration.ofSeconds(30)));
        waterballAcademy.Upload(new Video("C1M1S3", "物件", Duration.ofMinutes(1)));
        pewDiePie.Upload(new Video("Minecraft", "Let’s play Minecraft", Duration.ofMinutes(30)));
    }
}
