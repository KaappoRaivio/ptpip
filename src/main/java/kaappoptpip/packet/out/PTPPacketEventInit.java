package kaappoptpip.packet.out;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketEventInit extends PTPPacketOut {
    public PTPPacketEventInit(int connectionNumber) {
        super(PTPPacketType.INIT_EVENT_REQUEST);
        payload.writeUInt32(connectionNumber);
    }
}
