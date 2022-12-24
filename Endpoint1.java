import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 *
 * Endpoint 1 class
 *
 * An instance accepts user input
 *
 */

public class Endpoint1 extends Node {
    static final int DEFAULT_SRC_PORT = 50000;
    static final int DEFAULT_DST_PORT = 54321;
    static final String DEFAULT_SRC_NODE = "laptop";
    static final String DEFAULT_DST_NODE = "forwarder1";
    InetSocketAddress laptopAddress;
    InetSocketAddress dstAddress;

    /**
     * Constructor
     * Attempts to create socket at given port and create an InetSocketAddress for the destinations
     */
    Endpoint1(String dstHost, int dstPort, int srcPort){
        try{
            dstAddress = new InetSocketAddress(dstHost, dstPort);
            socket = new DatagramSocket(srcPort);
            listener.go();

        }
        catch(java.lang.Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            PacketContent content = PacketContent.fromDatagramPacket(packet);
            System.out.println("OK - onReceipt received ack packet");
            System.out.println(content.toString());
            this.notify();
        }
        catch(java.lang.Exception e){e.printStackTrace();}
    }

    /**
     * Sender Method
     */

    public synchronized void start() throws Exception{

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter input: ");
        String message = scanner.nextLine();  // Read user input
        System.out.println("Your message is: " + message);
        Integer src = 0x00BBCCDD;
        Integer dst = 0x00DDAAAA;

        FileContent fileContent;
        DatagramPacket packet;

        fileContent = new FileContent(dst, src, message);
        packet = fileContent.toDatagrampacket();
        laptopAddress = new InetSocketAddress(DEFAULT_SRC_NODE, DEFAULT_SRC_PORT);
        packet.setSocketAddress(dstAddress);
        socket.send(packet);
        System.out.println("Packet sent to server");
        this.wait();
    }

    /**
     * Test method
     * Sends a packet to a given address
     */
    public static void main(String[] args) {
        try {
            (new Endpoint1(DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();
            System.out.println("Laptop completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }

}
