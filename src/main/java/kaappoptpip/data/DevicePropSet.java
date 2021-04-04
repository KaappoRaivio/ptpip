package kaappoptpip.data;

import kaappoptpip.packet.PTPDataTypes;

import java.util.Map;
import java.util.Optional;

public class DevicePropSet {
    public static final DevicePropSet DEFAULT_PROPSET = new DevicePropSet(Map.ofEntries(
            Map.entry(0x5001, new DeviceProp<>(0x5001, PTPDataTypes.UInt8t.TYPE, "BatteryLevel", true)),
            Map.entry(0x5003, new DeviceProp<>(0x5003, PTPDataTypes.WChar.TYPE, "ImageSize")),
            Map.entry(0x5004, new DeviceProp<>(0x5004, PTPDataTypes.UInt8t.TYPE, "CompressionSetting", false, Map.ofEntries(
                    Map.entry(0x00, "JPEG (BASIC)"),
                    Map.entry(0x01, "JPEG (NORMAL)"),
                    Map.entry(0x02, "JPEG (FINE)"),
                    Map.entry(0x04, "RAW"),
                    Map.entry(0x05, "RAW + JPEG (BASIC)"),
                    Map.entry(0x06, "RAW + JPEG (NORMAL)"),
                    Map.entry(0x07, "RAW + JPEG (FINE)")
            ))),
            Map.entry(0x5005, new DeviceProp<>(0x5005, PTPDataTypes.UInt16t.TYPE, "WhiteBalance", false, Map.ofEntries(
                    Map.entry(0x0002, "Auto"),
                    Map.entry(0x0004, "Sunny"),
                    Map.entry(0x0005, "Fluorescent"),
                    Map.entry(0x0006, "Flash"),
                    Map.entry(0x0007, "Cloudy"),
                    Map.entry(0x8010, "Shade"),
                    Map.entry(0x8012, "Custom"),
                    Map.entry(0x8013, "Preset")
            ))),
            Map.entry(0x5007, new DeviceProp<>(0x5007, PTPDataTypes.UInt16t.TYPE, "Fnumber")),
            Map.entry(0x5008, new DeviceProp<>(0x5008, PTPDataTypes.UInt32t.TYPE, "FocalLength", true)),
            Map.entry(0x500A, new DeviceProp<>(0x500A, PTPDataTypes.UInt16t.TYPE, "FocusMode", true, Map.ofEntries(
                    Map.entry(0x0001, "Manual focus"),
                    Map.entry(0x8010, "Single AF servo"),
                    Map.entry(0x8011, "Continuous AF servo"),
                    Map.entry(0x8012, "AF servo mode automatic switching"),
                    Map.entry(0x8013, "Constant AF servo")
            ))),
            Map.entry(0x500B, new DeviceProp<>(0x500B, PTPDataTypes.UInt16t.TYPE, "ExposureMeteringMode",false, Map.ofEntries(
                    Map.entry(0x2, "Center-weighted metering"),
                    Map.entry(0x3, "Matrix metering"),
                    Map.entry(0x4, "Spot metering")
            ))),
            Map.entry(0x500C, new DeviceProp<>(0x500C, PTPDataTypes.UInt16t.TYPE, "FlashMode", false, Map.ofEntries(
                    Map.entry(0x0002, "Flash off"),
                    Map.entry(0x0004, "Red-eye reduction"),
                    Map.entry(0x8010, "Front curtain sync"),
                    Map.entry(0x8011, "Slow sync"),
                    Map.entry(0x8012, "Rear curtain sync"),
                    Map.entry(0x8013, "Red-ey reduction slow sync")
            ))),
            Map.entry(0x500D, new DeviceProp<>(0x500D, PTPDataTypes.UInt32t.TYPE, "ExposureTime", false, Map.ofEntries(
                    Map.entry(0xFFFFFFFF, "Bulb shooting"),
                    Map.entry(0xFFFFFFFD, "Time shooting")
            ))),
            Map.entry(0x500E, new DeviceProp<>(0x500E, PTPDataTypes.UInt16t.TYPE, "ExposureProgramMode", false, Map.ofEntries(
                    Map.entry(0x0001, "[M] Manual"),
                    Map.entry(0x0002, "[P] Program auto"),
                    Map.entry(0x0003, "[A] Aperture priority auto"),
                    Map.entry(0x0004, "[S] Shutter priority auto"),
                    Map.entry(0x8010, "[Scene mode] AUTO"),
                    Map.entry(0x8016, "[Scene mode] Flash-off AUTO"),
                    Map.entry(0x8018, "[Scene mode] SCENE"),
                    Map.entry(0x8019, "Special effect mode] EFFECTS"),
                    Map.entry(0x8050, "[User mode] U1"),
                    Map.entry(0x8051, "[User mode] U2")
            ))),
            Map.entry(0x500F, new DeviceProp<>(0x500F, PTPDataTypes.UInt16t.TYPE, "ExposureIndex"))

    ));


    private final Map<Integer, DeviceProp<?>> propsSet;

    public DevicePropSet(Map<Integer, DeviceProp<?>> propsSet) {
        this.propsSet = propsSet;
    }

    public DeviceProp<?> getProp (int propCode) {
        return Optional.ofNullable(propsSet.get(propCode)).orElseThrow();
    }

    public static void main (String[] args) {
        DeviceProp<?> prop = DEFAULT_PROPSET.getProp(0x500E);
//        Object x = prop.getDataType().parseData();
//        prop.getDataType().parseData()
//        System.out.println(prop.getClass());
    }
}
