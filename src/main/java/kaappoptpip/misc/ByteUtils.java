package kaappoptpip.misc;

public class ByteUtils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static byte[] toLittleEndian (long data) {
        byte byte1 = (byte) (data >> 0 & 0xff);
        byte byte2 = (byte) (data >> 8 & 0xff);
        byte byte3 = (byte) (data >> 16 & 0xff);
        byte byte4 = (byte) (data >> 24 & 0xff);
        byte byte5 = (byte) (data >> 32 & 0xff);
        byte byte6 = (byte) (data >> 40 & 0xff);
        byte byte7 = (byte) (data >> 48 & 0xff);
        byte byte8 = (byte) (data >> 56 & 0xff);

        return new byte[]{byte1, byte2, byte3, byte4, byte5, byte6, byte7, byte8};
    }

    public static byte[] toLittleEndian (int data) {
        byte byte1 = (byte) (data >> 0 & 0xff);
        byte byte2 = (byte) (data >> 8 & 0xff);
        byte byte3 = (byte) (data >> 16 & 0xff);
        byte byte4 = (byte) (data >> 24 & 0xff);

        return new byte[]{byte1, byte2, byte3, byte4};
    }

    public static String bytesToHex (byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            builder.append(byteToHex(bytes[i]));
            builder.append(" ");
        }
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
//            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
//        }
        return builder.toString();
//        return new String(hexChars);
    }

    public static String byteToHex(byte _byte) {
        return String.valueOf(HEX_ARRAY[_byte >> 4 & 0xf]) + HEX_ARRAY[_byte & 0xf];
    }
}
