import kaappoptpip.PTPRequestOut;
import kaappoptpip.connection.PTPSession;
import kaappoptpip.misc.ImageViewer;
import kaappoptpip.packet.PTPDataBuffer;
import kaappoptpip.packet.PTPPacketInMatcher;
import kaappoptpip.packet.PTPPacketType;
import kaappoptpip.packet._out.*;
import kaappoptpip.packet.in.PTPPacketIn;
import kaappoptpip.transaction.PTPCompletedTransaction;
import kaappoptpip.transaction.PTPTransactionDataParser;
import kaappoptpip.transaction.ParsedTransactionData;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.function.Function;

public class MyPTP {
    public static void main(String[] args) throws IOException {
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        }));
        try (PTPSession session = new PTPSession("192.168.2.1", true)) {


//            PTPDataBuffer ptpDataBuffer = new PTPDataBuffer();
//            ptpDataBuffer.writeUInt16(1100);
//            PTPRequestOut request = new PTPRequestOut(new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.SET_DEVICE_PROP_VALUE, 2, List.of(0x5007)),
//                    PTPPacketOut.packageData(2, ptpDataBuffer.toBytes()));
//            PTPCompletedTransaction transaction = session.sendCommand(request);
//            System.out.println(transaction.getResponsePacket());
//            System.out.println("moi" + PTPTransactionDataParser.parseTransactionData(session.sendCommand(new PTPRequestOut(new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.GET_DEVICE_PROP_VALUE, 2, List.of(0x5008)))).getTransactionData()).getField("value"));
//            System.out.println("moi" + PTPTransactionDataParser.parseTransactionData(session.sendCommand(new PTPRequestOut(new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.GET_DEVICE_PROP_VALUE, 2, List.of(0x5001)))).getTransactionData()).getField("value"));
//            while (true) {
//                session.idle(4000);
//                session.sendCommand(new PTPPacketCmdRequest(0x90C7, 0xCAFEBABE));
//            }

            ImageViewer viewer = new ImageViewer();

            session.startLiveView(parsedTransactionData -> {
                viewer.displayImage(((BufferedImage) parsedTransactionData.getField("image")));
                return null;
            });
//            PTPCompletedTransaction ptpCompletedTransaction = session.sendCommand(startLiveViewRequest);
//            System.out.println(ptpCompletedTransaction.getResponsePacket());
//            System.out.println(ptpCompletedTransaction.getTransactionData());
//            System.out.println("Started live view result: " + PTPTransactionDataParser.parseTransactionData(ptpCompletedTransaction.getTransactionData()));

//            PTPPacketCmdRequest ptpPacketCmdRequest = new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.SET_DEVICE_PROP_VALUE, 2, List.of(0x5007));
//            ptpPacketCmdRequest.attachData(PTPPacketOut.packageData(2, ptpDataBuffer.toBytes()));
//            PTPCompletedTransaction ptpCompletedTransaction = session.sendCommand(ptpPacketCmdRequest);
//            System.out.println(ptpCompletedTransaction.getResponsePacket());
            session.doNothing();
        }

//        System.out.println("Trying to set capture mode");
//        commandConnection.writePacket(new PTPPacketCmdRequest(PTPPacketCmdRequest.OpCodes.SET_DEVICE_PROP_VALUE, 2, List.of(0x5013)));
//        PTPPacketOut.packageData(2, new byte[]{0x11, (byte) 0x80}).forEach(commandConnection::writePacket);
//        responses = commandConnection.getPackets();
//        for (PTPPacketIn response : responses) {
//            System.out.println(response);
//        }

    }
}
