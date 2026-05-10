package FactoryMethodPattern.v1;

import FactoryMethodPattern.Tunnel.Tunnel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ChatRoomServer {
    private final int port;

    public ChatRoomServer(int port) {
        this.port = port;
    }

    public void startServer() throws IOException{
        try(var server = new ServerSocket(port)){
            // 等待下一位客人光臨
            Socket client = server.accept(); //要有人連上socket port才會產生出client物件

            // Tunnel 希望允許開發者客製化(只有這裡是變動部分
            // 又希望遵守OCP能輕鬆抽換
            // ChatRoomServer close
            // Tunnel open 客製化
            // FactoryMethod = TemplateMethod + 面臨的變動之處是創建出來的類型會變動/或連創建甚麼實際類別都不確定
            Tunnel tunnel = createTunnel(client);

            String message = tunnel.message();

            System.out.println(message);
        }
    }

    //留給子類別自行決定如何實作變動部分的Step = Factory method = 一個抽像方法，幫助你延遲抉擇要創建出來的實體類型 = 創健型的樣板方法
    protected abstract Tunnel createTunnel(Socket client);
}
