package anyvr.app.lemon.handler;

import java.util.Collections;

import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtobufIntLengthPrepender extends MessageToByteEncoder<ByteBuf> {

    private byte[] toByteArray(int value) {
        return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
    }

    @Override
    protected  void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final ByteBuf out) throws Exception {

        int length = msg.readableBytes();
        byte[] lengthInBytes = toByteArray(length);

        Collections.reverse(Bytes.asList(lengthInBytes));

        out.writeBytes(lengthInBytes);
        out.writeBytes(msg, msg.readerIndex(), length);
    }

}
