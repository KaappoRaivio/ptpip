package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketOutEndData extends PTPDataPacketOut {
    private int transactionID;
    private final byte[] data;

    public PTPPacketOutEndData(int transactionID, byte[] data) {
        super(PTPPacketType.END_DATA_PACKET);
        this.transactionID = transactionID;
        this.data = data;

        rewrite();
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
