import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Endpoint2 extends Node{
    static final int SERVER_PORT = 50001;
    static final int FORWARD3_PORT = 54333;
    static final String FORWARD3 = "forwarder3";
    static final String SERVER_NODE ="server";
    InetSocketAddress f3Address;  // CP (Forwarder3 address)

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
                response.setSocketAddress(f3Address);
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
        f3Address = new InetSocketAddress(FORWARD3, FORWARD3_PORT);
        System.out.println("Waiting for contact");
        this.wait();
    }

    /*
     *
     */
    public static void main(String[] args) {
        try {
            (new Endpoint2(SERVER_PORT)).start();
            //System.out.println("...");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}
