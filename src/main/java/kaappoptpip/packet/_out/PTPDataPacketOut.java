package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

import java.io.OutputStream;

abstract public class PTPDataPacketOut extends PTPPacketOut {
    public PTPDataPacketOut(PTPPacketType type) {
        super(type);
    }

    public long getAmountOfDataBytesInPacket () {
        return 0;
    };
    @Override
    public int getTransactionID () {
        return -1;
    }


    abstract public boolean hasTransactionId ();
    abstract public void setTransactionId (int transactionId);

    public byte[] getData() {
        return new byte[0];
    };

    @Override
    public void writeTo (OutputStream stream) {
        System.out.println("writing data to stream");
        super.writeTo(stream);
    }
}
