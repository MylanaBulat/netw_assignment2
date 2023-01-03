import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.HashMap;

public class Controller extends Node{

    static final int SRC_PORT = 50003;
    // static final String SRC_NODE = "controller";
    static final String FORWARD1 = "forwarder1";
    static final String FORWARD2 = "forwarder2";
    static final String FORWARD3 = "forwarder3";
    static final String SERVER_NODE = "server";
    static final String LAPTOP_NODE = "laptop";
    static final int FORWARD1_PORT = 54321;
    static final int FORWARD2_PORT = 54322;
    static final int FORWARD3_PORT = 54333;
    static final int SERVER_PORT = 50001;
    static final int LAPTOP_PORT = 50000;

    SocketAddress forwAddress;

    HashMap <String, String> ServerNodeList = new HashMap<>();
    HashMap <String, Integer> ServerPortList = new HashMap<>();

    HashMap <String, String> LaptopNodeList = new HashMap<>();
    HashMap <String, Integer> LaptopPortList = new HashMap<>();

    Controller (int port) {
        try {
            socket = new DatagramSocket(port);
            listener.go();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceipt(DatagramPacket packet) {
        try {
            PacketContent content = PacketContent.fromDatagramPacket(packet);
            Integer dest = ((PacketIn)content).getDst();
            String currentNode = ((PacketIn)content).getCurrentNode();
            String nextNode;
            int nextPort;

            forwAddress = packet.getSocketAddress();

            if(dest.equals(0x00DDAAAA)){
                System.out.println("Destination in the forwarding table");
                nextNode = ServerNodeList.get(currentNode);
                nextPort = ServerPortList.get(currentNode);
                DatagramPacket response;
                response = new FlowMod(dest, nextNode, nextPort).toDatagrampacket();
                response.setSocketAddress(forwAddress);
                socket.send(response);
                System.out.println("Packet response sent to forwarder");
            }

            if(dest.equals(0x00BBCCDD)){
                System.out.println("Destination in the forwarding table");
                nextNode = LaptopNodeList.get(currentNode);
                nextPort = LaptopPortList.get(currentNode);
                DatagramPacket response;
                response = new FlowMod(dest, nextNode, nextPort).toDatagrampacket();
                response.setSocketAddress(forwAddress);
                socket.send(response);
                System.out.println("Packet response sent to forwarder");
            }

        } catch (java.lang.Exception e) {e.printStackTrace();}
    }

    public synchronized void start() throws Exception {
        ServerNodeList.put(FORWARD1, FORWARD2);
        ServerNodeList.put(FORWARD2, FORWARD3);
        ServerNodeList.put(FORWARD3, SERVER_NODE);

        ServerPortList.put(FORWARD1, FORWARD2_PORT);
        ServerPortList.put(FORWARD2, FORWARD3_PORT);
        ServerPortList.put(FORWARD3, SERVER_PORT);

        LaptopNodeList.put(FORWARD3, FORWARD2);
        LaptopNodeList.put(FORWARD2, FORWARD1);
        LaptopNodeList.put(FORWARD1, LAPTOP_NODE);

        LaptopPortList.put(FORWARD3, FORWARD2_PORT);
        LaptopPortList.put(FORWARD2, FORWARD1_PORT);
        LaptopPortList.put(FORWARD1, LAPTOP_PORT);

        System.out.println("Waiting for contact");
        this.wait();
    }

    public static void main(String[] args) {
        try {
            (new Controller(SRC_PORT)).start();
            System.out.println("Controller completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}
