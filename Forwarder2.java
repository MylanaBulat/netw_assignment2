import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;

public class Forwarder2 extends Node {
    static final String FORWARD2 = "forwarder2";
    static final int DEFAULT_SRC_PORT = 54322;
    static final String DST_NODE = "forwarder3";
    static final int DST_PORT = 54323;

    String nextNode;
    int nextPort;
    InetSocketAddress newAddress;
    DatagramPacket testPack;

    HashMap<Integer, String> forwardingTableNode = new HashMap<Integer, String>();
    HashMap<Integer, Integer> forwardingTablePort = new HashMap<Integer, Integer>();

    Forwarder2(int srcPort) {
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

                InetSocketAddress forwarder3Address = new InetSocketAddress(nextNode, nextPort);
                packet.setSocketAddress(forwarder3Address);
                socket.send(packet);
                System.out.println("Packet sent to forwarder 3");
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
        forwardingTableNode.put(0x00DDAAAA, "forwarder3");
        forwardingTablePort.put(0x00DDAAAA, 54333);
        forwardingTableNode.put(0x00BBCCDD, "forwarder1");
        forwardingTablePort.put(0x00BBCCDD, 54321);
        this.wait();
    }

    /*
     *
     */
    public static void main(String[] args) {
        try {
            (new Forwarder2(DEFAULT_SRC_PORT)).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }


}
