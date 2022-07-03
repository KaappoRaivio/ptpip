package kaappoptpip.transaction;

import kaappoptpip.data.DeviceProp;
import kaappoptpip.data.DevicePropSet;
import kaappoptpip.packet.in.PTPInStream;
import kaappoptpip.packet._out.PTPPacketCmdRequest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class PTPTransactionDataParser {


    public static ParsedTransactionData parseTransactionData(PTPCompletedDataTransfer transaction) {
        int opCode = transaction.getInitiatingPacket().getOperationCode();

        switch (opCode) {
            case PTPPacketCmdRequest.OpCodes.GET_DEVICE_INFO:
                return parseDeviceInfoDataset(transaction);
            case PTPPacketCmdRequest.OpCodes.GET_DEVICE_PROP_VALUE:
                return parseDevicePropValue(transaction);
            case PTPPacketCmdRequest.OpCodes.GET_LIVE_VIEW_IMAGE:
                return parseLiveViewImage(transaction);
            default:
                throw new RuntimeException("Unknown reponse type " + transaction + "!");
        }
    }

    private static ParsedTransactionData parseLiveViewImage(PTPCompletedDataTransfer transaction) {
        System.out.println("Stream length: " + transaction.getTransactionData().getStreamLength());
        PTPInStream transactionData = transaction.getTransactionData();

        ParsedTransactionData data = new ParsedTransactionData();
//        transactionData.readBytes(376);
        data.add("displayareasize", transactionData.readUInt32());
        data.add("liveviewareasize", transactionData.readUInt32());

        data.add("imageWidth", transactionData.readUInt16());
        data.add("imageHeight", transactionData.readUInt16());
        transactionData.readUInt16();
        transactionData.readUInt16();
        data.add("displayAreaWidth", transactionData.readUInt16());
        data.add("displayAreaHeight", transactionData.readUInt16());
        transactionData.readUInt16();
        transactionData.readUInt16();
        transactionData.readUInt16();
        transactionData.readUInt16();
        transactionData.readUInt16();
        transactionData.readUInt16();
        transactionData.readUInt32();
        data.add("focusarea", transactionData.readUInt8());
        data.add("rotationdirection", transactionData.readUInt8());
        transactionData.readUInt8();

        transactionData.readUInt8();
        transactionData.readUInt32();
        transactionData.readUInt16();

        transactionData.readUInt16();

        transactionData.readUInt8();
        transactionData.readUInt8();

        transactionData.readUInt32();
        transactionData.readUInt32();
        transactionData.readUInt32();

        transactionData.readUInt32();
        transactionData.readUInt8();
        transactionData.readUInt8();

        int numberOfFaces = transactionData.readUInt8();
        data.add("numberOfFaces", numberOfFaces);
        transactionData.readUInt8();
        transactionData.readBytes(numberOfFaces * (2 + 2 + 2 + 2));

        transactionData.readUInt8();
        transactionData.readUInt8();
        transactionData.readUInt8();
        transactionData.readUInt8();

        transactionData.readUInt8();
        transactionData.readUInt8();
        transactionData.readUInt8();

        transactionData.readUInt8();

        transactionData.readBytes(24);

        int paddingLeftToRead = 376 - (transactionData.getStreamLength() - transactionData.left()) + 8;
        System.out.println("Stuff" + paddingLeftToRead);
        transactionData.readBytes(paddingLeftToRead);


        var imageData = transactionData.readAllBytes();

        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        data.add("image", img);

//        System.out.println(Arrays.toString(imageData));


//        System.out.println(transactionData.getStreamLength());
        System.out.println(data);
        return data;
    }

    private static ParsedTransactionData parseDeviceInfoDataset(PTPCompletedDataTransfer transaction) {
        PTPInStream transactionData = transaction.getTransactionData();

        System.out.println("DEVICEINFORDATASET: ");


        ParsedTransactionData data = new ParsedTransactionData();

        data.add("standardVersion", transactionData.readUInt16());
        data.add("vendorExtensionID", transactionData.readUInt32());
        data.add("vendorExtensionVersion", transactionData.readUInt16());
        data.add("vendorExtensionString", transactionData.readWChar(true));
        data.add("functionalMode", transactionData.readUInt16());
        data.add("operationsSupported", transactionData.readArrayOfUInt16());
        data.add("eventSupported", transactionData.readArrayOfUInt16());
        data.add("devicePropertiesSupported", transactionData.readArrayOfUInt16());
        data.add("captureFormatsSupported", transactionData.readArrayOfUInt16());
        data.add("imageFormatsSupported", transactionData.readArrayOfUInt16());
        data.add("manufacturer", transactionData.readWChar(true));
        data.add("model", transactionData.readWChar(true));
        data.add("version", transactionData.readWChar(true));
        data.add("serialNumber", transactionData.readWChar(true));

        System.out.println(data);
        return data;
    }

    private static ParsedTransactionData parseDevicePropValue(PTPCompletedDataTransfer transaction) {
        PTPInStream transactionData = transaction.getTransactionData();
        PTPPacketCmdRequest initiatingPacket = transaction.getInitiatingPacket();

        int devicePropCode = initiatingPacket.getParameters().get(0);

        DeviceProp<?> prop = DevicePropSet.DEFAULT_PROPSET.getProp(devicePropCode);

        String devicePropValueString = prop.parseDataString(transactionData);
//        int devicePropValue = transactionData.readUInt16();

        ParsedTransactionData data = new ParsedTransactionData();
        data.add("prop", prop);
        data.add("value", devicePropValueString);

        return data;
    }
}
