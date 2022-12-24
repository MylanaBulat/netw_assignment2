import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FlowMod extends PacketContent{
    Integer dest;
    String node;
    int port;

    FlowMod(Integer dest, String node, int port) {
        type = FLOWMOD;
        this.dest = dest;
        this.node = node;
        this.port = port;
    }

    protected FlowMod(ObjectInputStream oin) {
        try {
            type = FLOWMOD;
            dest = oin.readInt();
            node = oin.readUTF();
            port = oin.readInt();
        }
        catch(Exception e) {e.printStackTrace();}
    }

    @Override
    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
            oout.writeInt(dest);
            oout.writeUTF(node);
            oout.writeInt(port);
        }
        catch(Exception e) {e.printStackTrace();}


    }

    @Override
    public String toString() {
        return "Destination address: " + dest + " - Source node: " + node + "Port: "+ port;
    }

    public String getNode() {
        return node;
    }

    public Integer getDst() {
        return dest;
    }

    public int getPort() {
        return port;
    }
}
