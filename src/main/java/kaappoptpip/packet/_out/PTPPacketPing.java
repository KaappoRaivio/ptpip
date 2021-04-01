package kaappoptpip.packet._out;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketPing extends PTPPacketOut {
    public PTPPacketPing() {
        super(PTPPacketType.PING);
    }
}
