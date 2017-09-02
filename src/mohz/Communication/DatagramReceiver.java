package mohz.Communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DatagramReceiver {
    private DatagramSocket socket;
    private DatagramPacket packet;

    public DatagramReceiver(int socketNumber, int bufferSize) throws SocketException {
        this.socket = new DatagramSocket(socketNumber);
        byte[] buffer = new byte[bufferSize];
        this.packet = new DatagramPacket(buffer, buffer.length);
    }

    public String getDatagramData() throws IOException {
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }
}
