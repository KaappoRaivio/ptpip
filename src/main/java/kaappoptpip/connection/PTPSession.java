package kaappoptpip.connection;

import kaappoptpip.PTPRequestOut;
import kaappoptpip.packet.PTPPacketInMatcher;
import kaappoptpip.packet.PTPPacketType;
import kaappoptpip.packet._out.*;
import kaappoptpip.packet.in.PTPPacketCmdResponse;
import kaappoptpip.packet.in.PTPTPacketInitCommandAcknowledgement;
import kaappoptpip.transaction.PTPCompletedTransaction;
import kaappoptpip.transaction.PTPTransactionDataParser;
import kaappoptpip.transaction.ParsedTransactionData;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class PTPSession extends Thread implements Closeable {
    private PTPConnection commandConnection;
    private PTPConnection eventConnection;
    private boolean run = true;

    private int commandTransactionID = 1;
    private int eventTransactionID = 1;

    public PTPSession (String cameraAddress) throws IOException {
        this(cameraAddress, true);
    }

    public PTPSession (String cameraAddress, boolean keepAlive) throws IOException {
        commandConnection = new PTPConnection(cameraAddress);


        int connectionNumber = initializeCommandConnection();
        eventConnection = new PTPConnection(cameraAddress);
        sendEvent(new PTPPacketEventInit(connectionNumber));

        sendCommand(new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.OPEN_SESSION, commandTransactionID, List.of(1)));
        if (keepAlive) {
            start();
        }
    }

    private int initializeCommandConnection () throws IOException {
        PTPPacketInit initPacket = new PTPPacketInit(new short[]{0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0xff, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, "testi", "1.1");

        PTPCompletedTransaction transaction = sendCommand(initPacket);
        PTPTPacketInitCommandAcknowledgement response = (PTPTPacketInitCommandAcknowledgement) transaction.getResponsePacket();
        System.out.println(response);

        return response.getConnectionNumber();
    }


    public PTPCompletedTransaction sendCommand (PTPPacketOut packet) {
        return sendCommand(new PTPRequestOut(packet));
    }

    public PTPCompletedTransaction sendCommand (PTPRequestOut request) {
        if (request.getMainPacket().hasTransactionId()) {
            this.commandTransactionID++;
        }
        int currentTransactionID = this.commandTransactionID;
        for (PTPPacketOut packet : request.getPacketsToWrite()) {
            if (packet.hasTransactionId()) {
                System.out.println("Setting transactionID of " + currentTransactionID + " to packet " + packet + " with existing transactionID of " + packet.getTransactionID());
                packet.setTransactionId(currentTransactionID);
            }
            commandConnection.writePacket(packet);
        }

        PTPPacketOut mainPacket = request.getMainPacket();
        return commandConnection.getPackets(true, new PTPPacketInMatcher().packetTypes(PTPPacketType.getPossibleResponseTypes(mainPacket.getPacketType())).transactionID(mainPacket.getTransactionID()));
    }

    public PTPCompletedTransaction sendEvent (PTPPacketOut packet) {
        return sendEvent(new PTPRequestOut(packet));
    }

    public PTPCompletedTransaction sendEvent (PTPRequestOut request) {
        if (request.getMainPacket().hasTransactionId()) {
            this.eventTransactionID++;
        }
        int currentTransactionID = this.eventTransactionID;
        for (PTPPacketOut packet : request.getPacketsToWrite()) {
            if (packet.hasTransactionId()) {
                System.out.println("Setting transactionID of " + currentTransactionID + " to packet " + packet + " with existing transactionID of " + packet.getTransactionID());
                packet.setTransactionId(currentTransactionID);
            }
            eventConnection.writePacket(packet);
        }

        PTPPacketOut mainPacket = request.getMainPacket();
        return eventConnection.getPackets(true, new PTPPacketInMatcher().packetTypes(PTPPacketType.getPossibleResponseTypes(mainPacket.getPacketType())).transactionID(mainPacket.getTransactionID()));
//
//        eventConnection.writePacket(packet);
//        return eventConnection.getPackets(true, new PTPPacketInMatcher().packetTypes(PTPPacketType.getPossibleResponseTypes(packet.getPacketType())).transactionID(packet.getTransactionID()));
    }

    @Override
    public void run () {
        while (run) {
            PTPCompletedTransaction transaction = sendEvent(new PTPPacketPing());
            if (transaction.getResponsePacket().getPacketType() != PTPPacketType.PONG) {
                throw new RuntimeException("Ping returned " + transaction.getResponsePacket() + ", not PONG!");
            }

//            sendCommand(new PTPPacketCmdRequest(0x90C7, 0xCAFEBABE));



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
        sendCommand(new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.CLOSE_SESSION, 0xCAFEBABE));
        run = false;
        interrupt();

    }

    public void idle (long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public void startLiveView (Function<ParsedTransactionData, Void> onLiveViewFrame) {
        PTPPacketCmdRequest startLiveViewRequest = new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.START_LIVE_VIEW, 2);
        sendCommand(startLiveViewRequest);

        PTPPacketCmdRequest deviceReadyRequest = new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.DEVICE_READY, 2);

        PTPPacketCmdResponse response;
        do {
            PTPCompletedTransaction transaction = sendCommand(deviceReadyRequest);
            response = (PTPPacketCmdResponse) transaction.getResponsePacket();
            System.out.println(response);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (response.getResponseCode() == 0x2019);

        System.out.println("Device ready with code:" + (response.getResponseCode() == 0x2001 ? "OK" : "Fail") + "!");


        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            var getLiveViewImageRequest = new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.GET_LIVE_VIEW_IMAGE, 0);
            PTPCompletedTransaction potentialLiveViewImage = sendCommand(getLiveViewImageRequest);

            PTPPacketCmdResponse potentialImage = (PTPPacketCmdResponse) potentialLiveViewImage.getResponsePacket();
            System.out.println("Acquired live view frame with code:" + (potentialImage.getResponseCode() == 0x2001 ? "OK" : "Fail") + "!");

            var data = PTPTransactionDataParser.parseTransactionData(potentialLiveViewImage.getTransactionData());
            onLiveViewFrame.apply(data);
        }
//        System.out.println(data.getField("focusarea"));

    }
}
