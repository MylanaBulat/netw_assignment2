import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;

public class Forwarder3 extends Node {
    static final String FORWARD3 = "forwarder3";
    static final int DEFAULT_SRC_PORT = 54333;
    static final String DST_NODE = "server";
    static final int DST_PORT = 50001;

    String nextNode;
    int nextPort;
    InetSocketAddress newAddress;
    DatagramPacket testPack;

    HashMap<Integer, String> forwardingTableNode = new HashMap<Integer, String>();
    HashMap<Integer, Integer> forwardingTablePort = new HashMap<Integer, Integer>();

    Forwarder3(int srcPort) {
        try {
            socket = new DatagramSocket(srcPort);
            listener.go();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onReceipt(DatagramPacket packet) {
        try {
            System.out.println("Received packet");
            PacketContent content = PacketContent.fromDatagramPacket(packet);


            if(content.getType() == PacketContent.FILECONTENT){
                FileContent fileContent = (FileContent)content;
                Integer dest = fileContent.getDst();
                nextNode = forwardingTableNode.get(dest);
                nextPort = forwardingTablePort.get(dest);

                InetSocketAddress endpoint2DstAddress = new InetSocketAddress(nextNode, nextPort);
                packet.setSocketAddress(endpoint2DstAddress);
                socket.send(packet);
                System.out.println("Packet sent to server");
            }


            /**
             *
             */

        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void start() throws Exception {
        System.out.println("Waiting for contact");
        forwardingTableNode.put(0x00DDAAAA, "server");
        forwardingTablePort.put(0x00DDAAAA, 50001);
        forwardingTableNode.put(0x00BBCCDD, "forwarder2");
        forwardingTablePort.put(0x00BBCCDD, 54322);
        this.wait();
    }

    /*
     *
     */
    public static void main(String[] args) {
        try {
            (new Forwarder1(DEFAULT_SRC_PORT)).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }


}
