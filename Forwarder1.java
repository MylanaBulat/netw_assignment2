import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Forwarder1 extends Node {
    static final String FORWARD1 = "forwarder1";
    static final int FORWARD1_PORT = 54321;
    static final int CONTROLLER_PORT = 50003;
    static final String CONTROLLER_NODE = "controller";

    String nextNode;
    int nextPort;
    DatagramPacket tempPack;
    InetSocketAddress newAddress;
    InetSocketAddress controllerAddress;

    HashMap<Integer, String> forwardingTableNode = new HashMap<Integer, String>();
    HashMap<Integer, Integer> forwardingTablePort = new HashMap<Integer, Integer>();

    Forwarder1(int port) {
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
                tempPack.setSocketAddress(newAddress);
                socket.send(tempPack);
                System.out.println("Forwarding table filled by controller. ");
            }

            else if(content.getType() == PacketContent.FILECONTENT){
                FileContent fileContent = (FileContent)content;
                // header stores the destination
                Integer dest = fileContent.getDst();

                // case where forwarding table contains the destination
                if(forwardingTableNode.containsKey(dest) || forwardingTablePort.containsKey(dest)){
                    nextNode = forwardingTableNode.get(dest);
                    nextPort = forwardingTablePort.get(dest);
                    newAddress = new InetSocketAddress(nextNode, nextPort);
                    packet.setSocketAddress(newAddress);
                    socket.send(packet);
                    System.out.println("Destination found in forwarding table. Packet sent to forwarder 2");
                }
                // destination not in the forwarding table -> request information from the controller
                else{
                    tempPack = packet;
                    DatagramPacket request;
                    request = new PacketIn(dest, FORWARD1).toDatagrampacket();
                    request.setSocketAddress(controllerAddress);
                    socket.send(request);
                    System.out.println("Packet request sent to controller");
                }
            }

            /**
             *
             */

        } catch (java.lang.Exception e) {e.printStackTrace();}
    }

    public synchronized void start() throws Exception {
        System.out.println("Waiting for contact");
        controllerAddress = new InetSocketAddress(CONTROLLER_NODE, CONTROLLER_PORT);
        this.wait();
    }


    public static void main(String[] args) {
        try {
            (new Forwarder1(FORWARD1_PORT)).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }


}