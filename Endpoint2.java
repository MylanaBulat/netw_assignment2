import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Endpoint2 extends Node{
    static final int DEFAULT_SRC_PORT = 50001;
    static final int DEFAULT_DST_PORT = 54321;
    static final String DEFAULT_DST_NODE = "forwarder1";
    static final String SRC_NODE ="server";
    InetSocketAddress dstAddress;  // CP (Forwarder3 address)

    Endpoint2(int port) {
        try {
            socket = new DatagramSocket(port);
            listener.go();
        }
        catch(java.lang.Exception e) {e.printStackTrace();}
    }

    @Override
    public void onReceipt(DatagramPacket packet) {
        try{
            System.out.println("Packet received");
            PacketContent content= PacketContent.fromDatagramPacket(packet);

            if (content.getType()==PacketContent.FILECONTENT) {
                FileContent receivedPacket = (FileContent)content;
                System.out.println(receivedPacket.getMessage());
                Integer dst = receivedPacket.getSource();
                Integer src = 0x00DDAAAA;
                String data = "Received";

                FileContent fileContent;
                DatagramPacket response;
                fileContent = new FileContent(dst, src, data);
                response = fileContent.toDatagrampacket();
                response.setSocketAddress(dstAddress);
                socket.send(response);
                System.out.println("Ack packet sent");
            }
            else {
                System.out.println("Error: wrong packet type received");
            }

        }
        catch (Exception e) {e.printStackTrace();}
    }

    public synchronized void start() throws Exception {
        dstAddress = new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_DST_PORT);
        System.out.println("Waiting for contact");
        this.wait();
    }

    /*
     *
     */
    public static void main(String[] args) {
        try {
            (new Endpoint2(DEFAULT_SRC_PORT)).start();
            //System.out.println("...");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}
