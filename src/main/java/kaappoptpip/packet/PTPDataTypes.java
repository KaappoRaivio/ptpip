package kaappoptpip.packet;

import kaappoptpip.data.PTPDataType;
import kaappoptpip.misc.ByteUtils;
import kaappoptpip.packet.in.PTPInStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.IntStream;

public class PTPDataTypes {

    public static class UInt64t implements Writeable, PTPDataType<Long> {
        public static final UInt64t TYPE = new UInt64t(0);

        private final long value;
        public UInt64t(long value) {
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {
            try {
                stream.write(ByteUtils.toLittleEndian(value));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Long parseData (PTPInStream inStream) {
            return inStream.readUInt64();
        }

        @Override
        public Class<Long> getTypeClass () {
            return Long.class;
        }

        @Override
        public String toString () {
            if (this == TYPE) {
                return "UInt64t";
            } else {
                return "UInt64t{" + value + "}";
            }
        }
    }

    public static class UInt32t implements Writeable, PTPDataType<Integer> {
        public static final UInt32t TYPE = new UInt32t(0);
        private final int value;

        public UInt32t(int value) {
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {
            var bytes = ByteUtils.toLittleEndian(value);
            try {
                stream.write(Arrays.copyOfRange(bytes, 0, 4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Integer parseData (PTPInStream inStream) {
            return inStream.readUInt32();
        }

        @Override
        public Class<Integer> getTypeClass () {
            return Integer.class;
        }

        @Override
        public String toString () {
            if (this == TYPE) {
                return "UInt32t";
            } else {
                return "UInt32t{" + value + "}";
            }
        }
    }



    public static class UInt16t implements Writeable, PTPDataType<Integer> {
        public static final UInt16t TYPE = new UInt16t(0);
        private final int value;

        public UInt16t(int value) {
            if (value > 65536) throw new RuntimeException(value + " is too big to fit in UInt8t!");
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {
            var bytes = ByteUtils.toLittleEndian(value);
            try {
                stream.write(Arrays.copyOfRange(bytes, 0, 2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Integer parseData (PTPInStream inStream) {
            return inStream.readUInt16();
        }

        @Override
        public Class<Integer> getTypeClass () {
            return Integer.class;
        }

        @Override
        public String toString () {
            if (this == TYPE) {
                return "UInt16t";
            } else {
                return "UInt16t{" + value + "}";
            }
        }
    }

    public static class UInt8t implements Writeable, PTPDataType<Integer> {
        public static final UInt8t TYPE = new UInt8t(0);

        private final int value;

        public UInt8t(int value) {
            if (value > 256) throw new RuntimeException(value + " is too big to fit in UInt8t!");
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {
            var bytes = ByteUtils.toLittleEndian(value);
            try {
                stream.write(Arrays.copyOfRange(bytes, 0, 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Integer parseData (PTPInStream inStream) {
            return inStream.readUInt8();
        }

        @Override
        public Class<Integer> getTypeClass () {
            return Integer.class;
        }

        @Override
        public String toString () {
            if (this == TYPE) {
                return "UInt8t";
            } else {
                return "UInt8t{" + value + "}";
            }
        }
    }

    public static class WChar implements Writeable, PTPDataType<String> {
        public static final WChar TYPE = new WChar("");
        private final String value;

        public WChar(String value) {
            this.value = value;
        }

        @Override
        public void writeTo(OutputStream stream) {

            char[] chars = value.toCharArray();
            IntStream.range(0, chars.length).mapToObj(i -> chars[i]).forEach(character -> new UInt16t(character).writeTo(stream));

            new UInt16t(0).writeTo(stream);
        }

        @Override
        public String parseData (PTPInStream inStream) {
            return inStream.readWChar(true);
        }

        @Override
        public Class<String> getTypeClass () {
            return String.class;
        }

        @Override
        public String toString () {
            if (this == TYPE) {
                return "WChar";
            } else {
                return "Wchar{" + value + "}";
            }
        }
    }


}
