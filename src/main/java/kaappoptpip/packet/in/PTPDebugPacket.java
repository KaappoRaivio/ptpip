package kaappoptpip.packet.in;

import kaappoptpip.misc.ByteUtils;
import kaappoptpip.packet.PTPPacketType;
import kaappoptpip.packet.in.PTPInStream;
import kaappoptpip.packet.in.PTPPacketIn;

public class PTPDebugPacket extends PTPPacketIn {
    private byte[] buffer;

    protected PTPDebugPacket(PTPPacketType packetType, PTPInStream packetContent) {
        super(packetType, packetContent);
        buffer = packetContent.readAllBytes();
    }

    @Override
    public String toString() {
        return "kaappoptpip.packet.in.PTPDebugPacket with type " + packetType + " and content " + ByteUtils.bytesToHex(buffer);
    }

    @Override
    public boolean isSuitableForRepresenting(int packetType) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
