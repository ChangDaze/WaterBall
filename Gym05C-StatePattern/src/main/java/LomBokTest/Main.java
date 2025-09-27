package LomBokTest;

public class Main {
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("Tom");
        user.setPassword("Tom");
        System.out.println(user.getUsername());
        System.out.println(user);
    }
}
