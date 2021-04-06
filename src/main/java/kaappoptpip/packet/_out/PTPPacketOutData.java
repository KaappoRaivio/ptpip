package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketOutData extends PTPDataPacketOut {
    private int transactionID;
    private final byte[] data;

    public PTPPacketOutData (int transactionID, byte[] data) {
        super(PTPPacketType.DATA_PACKET);
        this.transactionID = transactionID;
        this.data = data;


    }

    private void rewrite () {
        payload.clear();

        payload.writeUInt32(transactionID);
        payload.writeBytes(data);
    }

    @Override
    public boolean hasTransactionId () {
        return true;
    }

    @Override
    public void setTransactionId (int transactionID) {
        this.transactionID = transactionID;
        rewrite();
    }
}
