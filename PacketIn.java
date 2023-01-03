import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketIn extends PacketContent{
    Integer destination;
    String currentNode;

    PacketIn(Integer destination, String currentNode) {
        type = PACKETIN;
        this.destination = destination;
        this.currentNode = currentNode;
    }

    protected PacketIn(ObjectInputStream oin) {
        try {
            type = PACKETIN;
            destination = oin.readInt();
            currentNode = oin.readUTF();
        }
        catch(Exception e) {e.printStackTrace();}
    }

    @Override
    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
            oout.writeInt(destination);
            oout.writeUTF(currentNode);
        }
        catch(Exception e) {e.printStackTrace();}


    }

    @Override
    public String toString() {
        return "Destination address: " + destination + " - Source address: " + currentNode ;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public Integer getDst() {
        return destination;
    }
}
