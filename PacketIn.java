import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketIn extends PacketContent{
    Integer destination;
    Integer currentNode;

    PacketIn(Integer destination, Integer currentNode) {
        type = PACKETIN;
        this.destination = destination;
        this.currentNode = currentNode;
    }

    protected PacketIn(ObjectInputStream oin) {
        try {
            type = PACKETIN;
            destination = oin.readInt();
            currentNode = oin.readInt();
        }
        catch(Exception e) {e.printStackTrace();}
    }

    @Override
    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
            oout.writeInt(destination);
            oout.writeInt(currentNode);
        }
        catch(Exception e) {e.printStackTrace();}


    }

    @Override
    public String toString() {
        return "Destination address: " + destination + " - Source address: " + currentNode ;
    }

    public Integer getSource() {
        return currentNode;
    }

    public Integer getDst() {
        return destination;
    }
}
