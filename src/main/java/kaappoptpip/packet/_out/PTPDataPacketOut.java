package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

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
    };
    public byte[] getData() {
        return new byte[0];
    };
}
