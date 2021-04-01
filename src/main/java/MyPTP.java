import kaappoptpip.connection.PTPConnection;
import kaappoptpip.packet.PTPPacketType;
import kaappoptpip.packet.in.PTPPacketIn;
import kaappoptpip.packet.in.PTPTPacketInitAcknowledgement;
import kaappoptpip.packet.out.PTPPacketCmdRequest;
import kaappoptpip.packet.out.PTPPacketEventInit;
import kaappoptpip.packet.out.PTPPacketInit;
import kaappoptpip.packet.out.PTPPacketPing;

import java.io.*;
import java.util.List;

public class MyPTP {
    public static void main(String[] args) throws IOException, InterruptedException {
//        Socket clientSocket = new Socket("192.168.2.1", 15740);
        PTPConnection commandConnection = new PTPConnection("192.168.2.1");
        PTPPacketInit initPacket = new PTPPacketInit(new short[]{0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0xff, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, "testi", "1.1");
        commandConnection.writePacket(initPacket);

        List<PTPPacketIn> packets = commandConnection.getPackets();
        PTPTPacketInitAcknowledgement p = null;
        for (PTPPacketIn packet : packets) {
            PTPPacketType type = packet.getPacketType();
            if (type == PTPPacketType.INIT_COMMAND_ACKNOWLEDGEMENT) {
                System.out.println("pöö");
                p = (PTPTPacketInitAcknowledgement) packet;
            }
        }

        PTPConnection eventConnection = new PTPConnection("192.168.2.1");
        eventConnection.writePacket(new PTPPacketEventInit(p.getConnectionNumber()));

        var responses = eventConnection.getPackets();
        for (PTPPacketIn response : responses) {
            System.out.println(response);
        }

        commandConnection.writePacket(new PTPPacketCmdRequest(0x1002, 1, 0));
        responses = commandConnection.getPackets();
        for (PTPPacketIn response : responses) {
            System.out.println(response);
        }


        while (true) {
            System.out.println("Sending ping");
            eventConnection.writePacket(new PTPPacketPing());
            responses = eventConnection.getPackets();
            for (PTPPacketIn response : responses) {
                System.out.println("Received pong");
                System.out.println(response);
            }

            Thread.sleep(5000);
        }
//        packets = connection.getPackets();
//        for (kaappoptpip.packet.in.PTPInStream packet : packets) {
//            int type = packet.readUInt32();
//            if (kaappoptpip.packet.PTPPacketType.getFromPayload(type) == kaappoptpip.packet.PTPPacketType.INIT_EVENT_ACKNOWLEDGEMENT) {
//                System.out.println("Success!");
//            }
//        }


//        System.out.println(packets);

//        OutputStream out = clientSocket.getOutputStream();

//        ByteArrayOutputStream proxy = new ByteArrayOutputStream();
//        initPacket.writeTo(proxy);
//        out.write(proxy.toByteArray());
//        System.out.println(kaappoptpip.packet.PTPDataBuffer.bytesToHex(proxy.toByteArray()));
//
//
//        InputStream in = clientSocket.getInputStream();
//        byte[] result = new byte[in.available()];
//        System.out.println(in.read(result));
//        System.out.println(kaappoptpip.packet.PTPDataBuffer.bytesToHex(result));

//        ByteArrayOutputStream test = new ByteArrayOutputStream();

    }
}