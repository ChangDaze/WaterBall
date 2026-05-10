package FactoryMethodPattern.v2;

import FactoryMethodPattern.Tunnel.Tunnel;

import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ChatRoomServer chatRoomServer = new ChatRoomServer(8787, new WaterballTunnelFactory());
        chatRoomServer.startServer();

        //像下面兩個class實作就能輕鬆擴充Tunnel實作
        ChatRoomServer chatRoomServer2 = new ChatRoomServer(8788, new SuperTunnelFactory());
        chatRoomServer2.startServer();
    }
}

class SuperTunnel implements Tunnel {
    @Override
    public String message() throws IOException {
        return "";
    }

    @Override
    public void disconnect() throws IOException {

    }
}

class SuperTunnelFactory implements TunnelFactory {
    @Override
    public Tunnel createTunnel(Socket client) {
        return new SuperTunnel();
    }
}
