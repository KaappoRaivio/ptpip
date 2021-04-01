package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

public class PTPPacketInitEventAcknowledgement extends PTPPacketIn{
    protected PTPPacketInitEventAcknowledgement(PTPInStream packetContent) {
        super(PTPPacketType.INIT_EVENT_ACKNOWLEDGEMENT, packetContent);
    }

    @Override
    public String toString() {
        return "PTPInitEventAcknowledment";
    }

    @Override
    public int size() {
        return 0;
    }
}
