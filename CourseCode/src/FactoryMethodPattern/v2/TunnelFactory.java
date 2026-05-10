package FactoryMethodPattern.v2;

import FactoryMethodPattern.Tunnel.Tunnel;

import java.net.Socket;

public interface TunnelFactory {
    Tunnel createTunnel(Socket client);
}
