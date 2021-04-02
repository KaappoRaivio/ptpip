package kaappoptpip.packet._out;

import kaappoptpip.packet.*;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

abstract public class PTPPacketOut implements Writeable {
    private PTPPacketType type;
    protected PTPOutStream payload = new PTPDataBuffer();


    public PTPPacketOut(PTPPacketType type) {
        this.type = type;
    }

    public boolean startsTransaction () {
        return false;
    }

    public boolean endsTransaction () {
        return false;
    }

    @Override
    public void writeTo(OutputStream stream) {
        new PTPDataTypes.UInt32t(payload.getSize() + 8).writeTo(stream);
        new PTPDataTypes.UInt32t(type.getPayload()).writeTo(stream);
        payload.writeTo(stream);
    }

    public static List<PTPDataPacketOut> packageData (int transactionID, byte[] data) {
        int length = data.length;

        List<PTPDataPacketOut> packets = new ArrayList<>();
        packets.add(new PTPPacketOutStartData(transactionID, length));
        packets.add(new PTPPacketOutEndData(transactionID, data));

        return packets;
    }

    public PTPPacketType getPacketType() {
        return type;
    }

    public int getTransactionID() {
        return -1;
    }
}
