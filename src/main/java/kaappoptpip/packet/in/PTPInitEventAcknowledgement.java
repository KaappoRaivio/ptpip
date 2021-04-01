package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

public class PTPInitEventAcknowledgement extends PTPPacketIn{
    protected PTPInitEventAcknowledgement(PTPInStream packetContent) {
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
