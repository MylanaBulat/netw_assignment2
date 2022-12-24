import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;

public class Forwarder1 extends Node {
    static final String FORWARD1 = "forwarder1";
    static final int DEFAULT_SRC_PORT = 54321;
    static final String DST_NODE = "server";
    static final int DST_PORT = 50001;

    String nextNode;
    int nextPort;
    InetSocketAddress newAddress;
    DatagramPacket cpacket;

    HashMap<Integer, String> forwardingTableNode = new HashMap<Integer, String>();
    HashMap<Integer, Integer> forwardingTablePort = new HashMap<Integer, Integer>();

    Forwarder1(int srcPort) {
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


            if(content.getType() == PacketContent.FLOWMOD){
                FlowMod ack = (FlowMod)content;
                Integer dest = ack.getDst();
                String node = ack.getNode();
                int port = ack.getPort();

                forwardingTableNode.put(dest, node);
                forwardingTablePort.put(dest, port);
                newAddress = new InetSocketAddress(node, port);
                cpacket.setSocketAddress(newAddress);
                socket.send(cpacket);
            }


            if(content.getType() == PacketContent.FILECONTENT){
                FileContent fileContent = (FileContent)content;
                Integer dest = fileContent.getDst();
                String nextNode = forwardingTableNode.get(dest);
                int nextPort = forwardingTablePort.get(dest);

                InetSocketAddress endpoint2DstAddress = new InetSocketAddress(nextNode, nextPort);
                packet.setSocketAddress(endpoint2DstAddress);
                socket.send(packet);
                System.out.println("Packet sent to server");
             }


            /**
             *
             */
            //if(content.getType() == PacketContent.FILECONTENT){
                FileContent fileContent = (FileContent)content;
                //getting the destination node stored in the header
                Integer dst = fileContent.getDst();

                //else
                //temp.setSocketAddress(nextAddress);
                //temp = packet;
                //DatagramPacket request;
                //request = new PacketIn(destination, currentNode)
            //}

        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void start() throws Exception {
        System.out.println("Waiting for contact");
        forwardingTableNode.put(0x00DDAAAA, "server");
        forwardingTablePort.put(0x00DDAAAA, 50001);
        forwardingTableNode.put(0x00BBCCDD, "laptop");
        forwardingTablePort.put(0x00BBCCDD, 50000);
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