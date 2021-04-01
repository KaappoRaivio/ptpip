package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketOutEndData extends PTPDataPacketOut{
    public PTPPacketOutEndData(int transactionID, byte[] data) {
        super(PTPPacketType.END_DATA_PACKET);

        payload.writeUInt32(transactionID);
        payload.writeBytes(data);
    }
}
