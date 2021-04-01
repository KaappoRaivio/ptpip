package kaappoptpip.connection;

import kaappoptpip.misc.ByteUtils;
import kaappoptpip.packet.in.PTPInStreamReader;
import kaappoptpip.packet.in.PTPPacketIn;
import kaappoptpip.packet.in.PTPDataPacketIn;
import kaappoptpip.packet.in.PTPPacketInStartData;
import kaappoptpip.packet._out.PTPPacketCmdRequest;
import kaappoptpip.packet._out.PTPPacketOut;
import kaappoptpip.transaction.CompletedPTPTransaction;
import kaappoptpip.transaction.PTPTransactionDataParser;
import kaappoptpip.transaction.PTPTransactionManager;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class PTPConnection {
    private Socket socket;
    private OutputStream connectionOut;
    private PTPInStreamReader connectionIn;
    private PTPTransactionManager transactionManager;

    public PTPConnection(String cameraAddress) throws IOException {
        socket = new Socket(cameraAddress, 15740);
        connectionOut = socket.getOutputStream();
        connectionIn = new PTPInStreamReader(socket.getInputStream());
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

    public List<PTPPacketIn> getPackets() {
        return getPackets(true, -1);
    }

    public List<PTPPacketIn> getPackets (boolean block, int transactionID) {
        synchronized (connectionIn.lock) {
            if (block) {
                try {
                    connectionIn.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            List<PTPPacketIn> packets = connectionIn.getPackets().stream().map(packet -> PTPPacketIn.getPacket(packet.readUInt32(), packet)).collect(Collectors.toList());

            for (PTPPacketIn packet : packets) {
                if (packet.startsTransactionResponse()) {
                    System.out.println("Starting transaction");
                    PTPPacketInStartData ptpPacketInStartData = (PTPPacketInStartData) packet;
                    transactionManager.addDataToTransaction(ptpPacketInStartData.getTransactionID(), ptpPacketInStartData);
                } else if (packet.isPartOfTransaction()) {
                    PTPDataPacketIn cast = (PTPDataPacketIn) packet;
                    transactionManager.addDataToTransaction(cast.getTransactionID(), cast);
                } else if (packet.endsTransactionResponse()) {
                    System.out.println("Ending transaction");
                    PTPDataPacketIn cast = (PTPDataPacketIn) packet;
                    transactionManager.addDataToTransaction(cast.getTransactionID(), cast);

                    if (transactionManager.haveTransactionsCompleted()) {
                        System.out.println("COMPLETED TRANSACTION!!!!!!!!!!!!!!!!!!!!");
                        List<CompletedPTPTransaction> completeTransactions = transactionManager.getCompleteTransactions();
                        completeTransactions.forEach(PTPTransactionDataParser::parseTransactionData);
                    }
                }
            }

            return packets;
        }
    }
}
