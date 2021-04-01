package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketPong extends PTPPacketIn{
    protected PTPPacketPong(PTPInStream packetContent) {
        super(PTPPacketType.PONG, packetContent);
    }

    @Override
    public String toString() {
        return "PTPPacketPong";
    }

    @Override
    public int size() {
        return 0;
    }
}
