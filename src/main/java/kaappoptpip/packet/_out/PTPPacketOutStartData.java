package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

import java.io.OutputStream;

public class PTPPacketOutStartData extends PTPDataPacketOut {
    private int transactionID;
    private int uploadSize;

    public PTPPacketOutStartData (int transactionID, int uploadSize) {
        super(PTPPacketType.START_DATA_PACKET);
        this.transactionID = transactionID;
        this.uploadSize = uploadSize;

        rewrite();
    }

    private void rewrite () {
        payload.clear();

        payload.writeUInt32(transactionID);
        payload.writeUInt64(uploadSize);
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
