import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AckPacket extends PacketContent {
    String info;

    /**
     * Constructor
     */
    AckPacket(String info) {
        type= ACKPACKET;
        this.info = info;
    }

    protected AckPacket(ObjectInputStream oin) {
        try {
            type= ACKPACKET;
            info= oin.readUTF();
        }
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Writes the content into an ObjectOutputStream
     *
     */
    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
            oout.writeUTF(info);
        }
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Returns the content of the packet as String.
     *
     * @return Returns the content of the packet as String.
     */
    public String toString() {
        return "ACK:" + info;
    }

    /**
     * Returns the info contained in the packet.
     *
     * @return Returns the info contained in the packet.
     */
    public String getPacketInfo() {
        return info;
    }
}
