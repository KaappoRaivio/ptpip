package kaappoptpip.connection;

import kaappoptpip.packet.PTPPacketInMatcher;
import kaappoptpip.packet.PTPPacketType;
import kaappoptpip.packet._out.PTPPacketEventInit;
import kaappoptpip.packet._out.PTPPacketInit;
import kaappoptpip.packet._out.PTPPacketOut;
import kaappoptpip.packet.in.PTPPacketIn;
import kaappoptpip.packet.in.PTPTPacketInitCommandAcknowledgement;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class PTPSession implements Closeable {
    private PTPConnection commandConnection;
    private PTPConnection eventConnection;

    public PTPSession (String cameraAddress) throws IOException {
        commandConnection = new PTPConnection(cameraAddress);
        int connectionNumber = initializeCommandConnection();

        eventConnection = new PTPConnection(cameraAddress);
        eventConnection.writePacket(new PTPPacketEventInit(connectionNumber));

        PTPPacketInMatcher matcher = new PTPPacketInMatcher().packetType(PTPPacketType.INIT_EVENT_ACKNOWLEDGEMENT);
        List<PTPPacketIn> packets = eventConnection.getPackets(true, matcher);
        System.out.println(packets);
    }

    private int initializeCommandConnection () throws IOException {
        PTPPacketInit initPacket = new PTPPacketInit(new short[]{0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0xff, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, "testi", "1.1");
        commandConnection.writePacket(initPacket);

        PTPPacketInMatcher matcher = new PTPPacketInMatcher().packetType(PTPPacketType.INIT_COMMAND_ACKNOWLEDGEMENT);
        List<PTPPacketIn> packets = commandConnection.getPackets(true, matcher);
        PTPTPacketInitCommandAcknowledgement response = (PTPTPacketInitCommandAcknowledgement) packets.stream().filter(matcher::matches).findFirst().orElseThrow();

        return response.getConnectionNumber();
    }

    public List<PTPPacketIn> sendCommand (PTPPacketOut packet) {
        commandConnection.writePacket(packet);
        return commandConnection.getPackets(true, new PTPPacketInMatcher().packetTypes(PTPPacketType.getPossibleResponseTypes(packet.getPacketType())).transactionID(packet.getTransactionID()));
    }

    @Override
    public void close() throws IOException {

    }
}
