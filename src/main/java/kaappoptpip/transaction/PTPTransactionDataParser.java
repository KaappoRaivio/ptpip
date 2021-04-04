package kaappoptpip.transaction;

import kaappoptpip.data.DeviceProp;
import kaappoptpip.data.DevicePropSet;
import kaappoptpip.packet.in.PTPInStream;
import kaappoptpip.packet._out.PTPPacketCmdRequest;

public class PTPTransactionDataParser {
    public static ParsedTransactionData parseTransactionData (PTPCompletedDataTransfer transaction) {
        int opCode = transaction.getInitiatingPacket().getOperationCode();

        switch (opCode) {
            case PTPPacketCmdRequest.OpCodes.GET_DEVICE_INFO:
                return parseDeviceInfoDataset(transaction);
            case PTPPacketCmdRequest.OpCodes.GET_DEVICE_PROP_VALUE:
                return parseDevicePropValue(transaction);
            default:
                throw new RuntimeException("Unknown reponse type " + transaction + "!");
        }
    }

    private static ParsedTransactionData parseDeviceInfoDataset (PTPCompletedDataTransfer transaction) {
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

    private static ParsedTransactionData parseDevicePropValue (PTPCompletedDataTransfer transaction) {
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
