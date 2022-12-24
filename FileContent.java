import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents information sent in the packet
 */

public class FileContent extends PacketContent{

    Integer dst;    //destination iD encoded in Hexadecimal 0x00DDAAAA
    Integer source;     // source iD encoded in Hexadecimal 0x00BBCCDD
    String message;     // data I am trying to send - amount to deposit in the account

    /**
     * Constructor that takes in information about a file.
     * @param destination Id in the hexadecimal form
     * @param source Id in the hexadecimal form
     * @param message the amount of money we want to deposit
     */
    FileContent(Integer destination, Integer source, String message){
        type = FILECONTENT;
        this.dst = destination;
        this.source = source;
        this.message = message;
    }

    /**
     * Constructs an object out of a datagram packet.
     * packet Packet that contains information about a file.
     */
    protected FileContent(ObjectInputStream oin) {
        try {
            type = FILECONTENT;
            dst = oin.readInt();
            source = oin.readInt();
            message = oin.readUTF();
        }
        catch(Exception e) {e.printStackTrace();}
    }

    @Override
    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
            oout.writeInt(dst);
            oout.writeInt(source);
            oout.writeUTF(message);
        }
        catch(Exception e) {e.printStackTrace();}
    }



    @Override
    public String toString() {
        return "Destination address: " + dst + " - Source address: " + source + " Message: " + message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getSource() {
        return source;
    }

    public Integer getDst() {
        return dst;
    }
}
