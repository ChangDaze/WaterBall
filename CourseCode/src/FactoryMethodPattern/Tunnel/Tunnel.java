package FactoryMethodPattern.Tunnel;

import java.io.IOException;

public interface Tunnel {
    String message() throws IOException;

    void disconnect() throws IOException;
}
