package FactoryMethodPattern.v1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        WaterballChatRoomServer waterballChatRoomServer = new WaterballChatRoomServer(8787);
        waterballChatRoomServer.startServer();
    }
}
