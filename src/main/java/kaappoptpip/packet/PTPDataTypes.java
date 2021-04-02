package kaappoptpip.packet;

import kaappoptpip.data.PTPDataType;
import kaappoptpip.misc.ByteUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.IntStream;

public class PTPDataTypes {

    public static class UInt64t implements Writeable, PTPDataType {
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
    }

    public static class UInt32t implements Writeable, PTPDataType {
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
    }



    public static class UInt16t implements Writeable, PTPDataType {
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
    }

    public static class UInt8t implements Writeable, PTPDataType {
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
    }

    public static class WChar implements Writeable, PTPDataType {
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
    }


}
