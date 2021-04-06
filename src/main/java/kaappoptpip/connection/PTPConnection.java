package kaappoptpip.connection;

import kaappoptpip.misc.ByteUtils;
import kaappoptpip.packet.PTPPacketInMatcher;
import kaappoptpip.packet.in.PTPInStreamReader;
import kaappoptpip.packet.in.PTPPacketIn;
import kaappoptpip.packet.in.PTPDataPacketIn;
import kaappoptpip.packet.in.PTPPacketInStartData;
import kaappoptpip.packet._out.PTPPacketCmdRequest;
import kaappoptpip.packet._out.PTPPacketOut;
import kaappoptpip.transaction.PTPCompletedDataTransfer;
import kaappoptpip.transaction.PTPCompletedTransaction;
import kaappoptpip.transaction.PTPTransactionManager;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PTPConnection {
    private final Socket socket;
    private final OutputStream connectionOut;
    private final PTPInStreamReader connectionIn;
    private final PTPTransactionManager transactionManager;


    public PTPConnection(String cameraAddress) throws IOException {
        socket = new Socket(cameraAddress, 15740);
        connectionOut = socket.getOutputStream();
        connectionOut.flush();
        InputStream inputStream = socket.getInputStream();
        connectionIn = new PTPInStreamReader(inputStream);
        connectionIn.start();

        transactionManager = new PTPTransactionManager();
    }

    public void writePacket (PTPPacketOut packet) {
        ByteArrayOutputStream proxy = new ByteArrayOutputStream();
        packet.writeTo(proxy);
        System.out.println("==> OUTGOING PACKET " + ByteUtils.bytesToHex(proxy.toByteArray()));
        try {
            connectionOut.write(proxy.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (packet.startsTransaction()) {
            PTPPacketCmdRequest cast = (PTPPacketCmdRequest) packet;
            transactionManager.startTransaction(cast.getTransactionID(), cast);
        }
    }

    public PTPCompletedTransaction getPackets (boolean block, PTPPacketInMatcher matcher) {
        List<PTPPacketIn> buffer = new ArrayList<>();

        int retries = 0;
        while (true) {
            synchronized (connectionIn.lock) {
                if (block) {
                    try {
                        connectionIn.lock.wait(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                List<PTPPacketIn> packets = connectionIn.getPackets().stream().map(packet -> PTPPacketIn.getPacket(packet.readUInt32(), packet)).collect(Collectors.toList());
                if (packets.size() == 0) {
                    if (retries++ > 5) {
                        throw new RuntimeException("Connection timed out!");
                    } else {
                        System.out.println("retrying!");
                    }
                }

                for (PTPPacketIn packet : packets) {
                    if (packet.startsTransactionResponse()) {
                        PTPPacketInStartData ptpPacketInStartData = (PTPPacketInStartData) packet;
                        transactionManager.addDataToTransaction(ptpPacketInStartData.getTransactionID(), ptpPacketInStartData);
                    } else if (packet.isPartOfTransaction()) {
                        PTPDataPacketIn cast = (PTPDataPacketIn) packet;
                        transactionManager.addDataToTransaction(cast.getTransactionID(), cast);
                    } else if (packet.endsTransactionResponse()) {
                        PTPDataPacketIn cast = (PTPDataPacketIn) packet;
                        transactionManager.addDataToTransaction(cast.getTransactionID(), cast);
                    }
                }

                buffer.addAll(packets);

                PTPPacketIn matchingPacket = buffer.stream().filter(matcher::matches).findFirst().orElse(null);

                if (matchingPacket != null) {
                    if (transactionManager.hasCompletedTransactionFor(matchingPacket)) {
                        return new PTPCompletedTransaction(matchingPacket, transactionManager.getCompleteTransactionFor(matchingPacket), buffer);
                    } else {
                        return new PTPCompletedTransaction(matchingPacket, null, buffer);
                    }
                };
            }
        }

    }
}
