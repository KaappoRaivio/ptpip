package kaappoptpip.packet.out;

import kaappoptpip.packet.PTPPacketType;

import java.io.OutputStream;

abstract public class PTPPacketOut implements Writeable {
    private PTPPacketType type;
    protected PTPOutStream payload = new PTPDataBuffer();


    public PTPPacketOut(PTPPacketType type) {
        this.type = type;
    }

    @Override
    public void writeTo(OutputStream stream) {
        System.out.println("writing");
        System.out.println("Length " + payload.getSize());
        new PTPDataType.UInt32t(payload.getSize() + 8).writeTo(stream);
        new PTPDataType.UInt32t(type.getPayload()).writeTo(stream);
        payload.writeTo(stream);
    }
}
