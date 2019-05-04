package anyvr.app.lemon;

import java.nio.ByteOrder;

import anyvr.Spec;
import anyvr.app.lemon.handler.ProtobufIntLengthPrepender;
import anyvr.app.lemon.handler.PlayerVoiceHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ServerHandlerInitializer extends ChannelInitializer<Channel> {

    // Todo
    public static final String path = "/app/logs/";

    public ServerHandlerInitializer() {
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new ReadTimeoutHandler(10));
        p.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 10000, 0, 4, 0, 4, true));
        p.addLast(new ProtobufDecoder(Spec.PlayerVoice.getDefaultInstance()));
        p.addLast(new PlayerVoiceHandler(path, new PlayerStore()));

        p.addLast(new ProtobufIntLengthPrepender());
        p.addLast(new ProtobufEncoder());

    }

}
