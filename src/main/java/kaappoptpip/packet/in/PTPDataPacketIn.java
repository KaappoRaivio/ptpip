package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

abstract public class PTPDataPacketIn extends PTPPacketIn {
    PTPDataPacketIn(PTPPacketType packetType, PTPInStream packetContent) {
        super(packetType, packetContent);
    }

    abstract public long getAmountOfDataBytesInPacket ();
    abstract public int getTransactionID ();
    public abstract byte[] getData();
}
