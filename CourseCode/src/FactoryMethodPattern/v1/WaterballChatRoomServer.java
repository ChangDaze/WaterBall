package FactoryMethodPattern.v1;

import FactoryMethodPattern.Tunnel.Tunnel;
import FactoryMethodPattern.Tunnel.WaterballTunnel;

import java.net.Socket;

public class WaterballChatRoomServer extends ChatRoomServer{
    public WaterballChatRoomServer(int port) {
        super(port);
    }

    //只需實作變動之處Step
    @Override
    protected Tunnel createTunnel(Socket client) {
        return new WaterballTunnel(client);
    }
}
