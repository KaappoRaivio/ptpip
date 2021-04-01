package kaappoptpip.packet.in;

import kaappoptpip.packet.PTPPacketType;

abstract public class PTPPacketIn {
    protected PTPPacketType packetType;
    protected PTPInStream packetContent;

    protected PTPPacketIn (PTPPacketType packetType, PTPInStream packetContent) {
        this.packetType = packetType;
        this.packetContent = packetContent;
    }
    public PTPPacketType getPacketType () {
        return packetType;
    }

    public static PTPPacketIn getPacket (int packetType, PTPInStream packetContent) {
        PTPPacketType type = PTPPacketType.getFromPayload(packetType);

        switch (type) {
            case INIT_COMMAND_ACKNOWLEDGEMENT:
                return new PTPTPacketInitAcknowledgement(packetContent);
            default:
                return new PTPDebugPacket(type, packetContent);
        }
    }

    public boolean isSuitableForRepresenting (int packetType) {
        return this.packetType.getPayload() == packetType;
    }

    abstract public int size ();
}
