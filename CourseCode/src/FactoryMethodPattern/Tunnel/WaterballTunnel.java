package FactoryMethodPattern.Tunnel;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class WaterballTunnel implements Tunnel{
    public static final String WATERBALL = "水球好棒!";

    private final InputStream in;
    private final Socket client;

    public WaterballTunnel(Socket client) {
        try {
            in = client.getInputStream();
            this.client = client;
        }catch (IOException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public String message() throws IOException {
        String message = acceptMessage();
        disconnect();
        return message;
    }

    @Override
    public void disconnect() throws IOException {
        if(!client.isClosed()){
            client.close();
        }
    }

    private String acceptMessage() throws IOException {
        byte[] messagePacket = new byte[1024];
        int length = in.read(messagePacket); //讀取Client傳入內容

        //判斷符合協定
        //自行實作通訊協定 ex:每1024byte，先喊一句 水球好棒!
        if(length == -1 || !WATERBALL.equals(new String(messagePacket, 0, WATERBALL.length()))){
            throw new IllegalStateException("invalid data");
        }

        //將協定外的部分傳回
        return new String(messagePacket, WATERBALL.length(), length - WATERBALL.length());
    }
}
