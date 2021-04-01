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

        payload.writeUInt32(transactionID);
        payload.writeUInt64(uploadSize);
    }
}
