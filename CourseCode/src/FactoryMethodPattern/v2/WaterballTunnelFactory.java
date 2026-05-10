package FactoryMethodPattern.v2;

import FactoryMethodPattern.Tunnel.Tunnel;
import FactoryMethodPattern.Tunnel.WaterballTunnel;

import java.net.Socket;

public class WaterballTunnelFactory implements TunnelFactory {
    @Override
    public Tunnel createTunnel(Socket client) {
        return new WaterballTunnel(client);
    }
}
