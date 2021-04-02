package kaappoptpip.transaction;

import kaappoptpip.packet.in.PTPInStream;
import kaappoptpip.packet._out.PTPPacketCmdRequest;

import java.util.List;

public class PTPTransactionDataParser {
    public static ParsedTransactionData parseTransactionData (CompletedPTPTransaction transaction) {
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

    private static ParsedTransactionData parseDeviceInfoDataset (CompletedPTPTransaction transaction) {
        PTPInStream transactionData = transaction.getTransactionData();

        System.out.println("DEVICEINFORDATASET: ");
        int standardVersion = transactionData.readUInt16();
        int vendorExtensionID = transactionData.readUInt32();
        int vendorExtensionVersion = transactionData.readUInt16();
        String vendorExtensionString = transactionData.readWChar(true);
        int functionalMode = transactionData.readUInt16();

        System.out.println(standardVersion);
        System.out.println(vendorExtensionID);
        System.out.println(vendorExtensionVersion);
        System.out.println(vendorExtensionString);
        System.out.println(functionalMode);

        List<Integer> operationsSupported = transactionData.readArrayOfUInt16();
        List<Integer> eventsSupported = transactionData.readArrayOfUInt16();
        List<Integer> devicePropertiesSupported = transactionData.readArrayOfUInt16();
        List<Integer> captureFormatsSupported = transactionData.readArrayOfUInt16();
        List<Integer> imageFormatsSupported = transactionData.readArrayOfUInt16();

        System.out.println(operationsSupported);
        System.out.println(eventsSupported);
        System.out.println(devicePropertiesSupported + ", " + devicePropertiesSupported.size());
        System.out.println(captureFormatsSupported);
        System.out.println(imageFormatsSupported);


        String manufacturer = transactionData.readWChar(true);
        String model = transactionData.readWChar(true);
        String version = transactionData.readWChar(true);
        String serialNumber = transactionData.readWChar(true);


        System.out.println(manufacturer);
        System.out.println(model);
        System.out.println(version);
        System.out.println(serialNumber);

        return null;
    }

    private static ParsedTransactionData parseDevicePropValue (CompletedPTPTransaction transaction) {
        PTPInStream transactionData = transaction.getTransactionData();
        PTPPacketCmdRequest initiatingPacket = transaction.getInitiatingPacket();

        int devicePropCode = initiatingPacket.getParameters().get(0);
        int devicePropValue = transactionData.readUInt16();


        System.out.println("DEVICEPROPVALUE");

        return null;
    }
}
