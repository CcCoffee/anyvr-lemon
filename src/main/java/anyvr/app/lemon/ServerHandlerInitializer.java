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

    private static final int READ_TIME_OUT_IN_SECONDS = 5;
    private static final String VOICE_FILE_PATH = "/app/";

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new ReadTimeoutHandler(READ_TIME_OUT_IN_SECONDS));
        p.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 10000, 0, 4, 0, 4, true));
        p.addLast(new ProtobufDecoder(Spec.PlayerVoice.getDefaultInstance()));
        p.addLast(new PlayerVoiceHandler(VOICE_FILE_PATH, new PlayerStore(), new VoiceFileWriter()));

        p.addLast(new ProtobufIntLengthPrepender());
        p.addLast(new ProtobufEncoder());
    }

}
