package kaappoptpip.connection;

import kaappoptpip.packet.PTPPacketInMatcher;
import kaappoptpip.packet.PTPPacketType;
import kaappoptpip.packet._out.*;
import kaappoptpip.packet.in.PTPPacketIn;
import kaappoptpip.packet.in.PTPTPacketInitCommandAcknowledgement;
import kaappoptpip.transaction.PTPCompletedTransaction;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class PTPSession extends Thread implements Closeable {
    private PTPConnection commandConnection;
    private PTPConnection eventConnection;
    private boolean run = true;

    public PTPSession (String cameraAddress) throws IOException {
        this(cameraAddress, true);
    }

    public PTPSession (String cameraAddress, boolean keepAlive) throws IOException {
        commandConnection = new PTPConnection(cameraAddress);


        int connectionNumber = initializeCommandConnection();
        eventConnection = new PTPConnection(cameraAddress);

        System.out.println("Moi");
        sendEvent(new PTPPacketEventInit(connectionNumber));

        sendCommand(new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.OPEN_SESSION, 1, List.of(1)));
        if (keepAlive) {
            start();
        }
    }

    private int initializeCommandConnection () throws IOException {
        PTPPacketInit initPacket = new PTPPacketInit(new short[]{0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0xff, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, "testi", "1.1");
        commandConnection.writePacket(initPacket);

        PTPPacketInMatcher matcher = new PTPPacketInMatcher().packetType(PTPPacketType.INIT_COMMAND_ACKNOWLEDGEMENT);
        PTPCompletedTransaction transaction = commandConnection.getPackets(true, matcher);
        PTPTPacketInitCommandAcknowledgement response = (PTPTPacketInitCommandAcknowledgement) transaction.getResponsePacket();
        System.out.println(response);

        return response.getConnectionNumber();
    }

    public PTPCompletedTransaction sendCommand (PTPPacketOut packet) {
        commandConnection.writePacket(packet);
        return commandConnection.getPackets(true, new PTPPacketInMatcher().packetTypes(PTPPacketType.getPossibleResponseTypes(packet.getPacketType())).transactionID(packet.getTransactionID()));
    }

    public PTPCompletedTransaction sendEvent (PTPPacketOut packet) {
        eventConnection.writePacket(packet);
        return eventConnection.getPackets(true, new PTPPacketInMatcher().packetTypes(PTPPacketType.getPossibleResponseTypes(packet.getPacketType())).transactionID(packet.getTransactionID()));
    }

    @Override
    public void run () {
        while (run) {
            PTPCompletedTransaction transaction = sendEvent(new PTPPacketPing());
            if (transaction.getResponsePacket().getPacketType() != PTPPacketType.PONG) {
                throw new RuntimeException("Ping returned " + transaction.getResponsePacket() + ", not PONG!");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close () throws IOException {
        System.out.println("Closing!");
        run = false;
        interrupt();

    }

    public void doNothing () {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
