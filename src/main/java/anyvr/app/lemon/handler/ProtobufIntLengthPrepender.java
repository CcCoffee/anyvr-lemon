package anyvr.app.lemon.handler;

import java.util.Collections;

import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

public class ProtobufIntLengthPrepender extends MessageToByteEncoder<ByteBuf> {

    private final int PROTO_BUF_SIZE = 4;

    private byte[] toByteArray(int value) {
        return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final ByteBuf out) throws Exception {

        int length = msg.readableBytes();
        byte[] lengthInByte = toByteArray(length);

        Collections.reverse(Bytes.asList(lengthInByte));
        out.writeBytes(lengthInByte);
        out.writeBytes(msg, msg.readerIndex(), length);
    }

}
