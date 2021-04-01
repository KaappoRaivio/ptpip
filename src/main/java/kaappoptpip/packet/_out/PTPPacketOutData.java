package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketOutData extends PTPDataPacketOut {
    public PTPPacketOutData (int transactionID, byte[] data) {
        super(PTPPacketType.DATA_PACKET);

        payload.writeBytes(data);
    }

}
