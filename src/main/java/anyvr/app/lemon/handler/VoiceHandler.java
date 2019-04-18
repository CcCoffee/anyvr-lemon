package anyvr.app.lemon.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;
import anyvr.app.lemon.jni.Opus;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class VoiceHandler extends SimpleChannelInboundHandler<Spec.PlayerVoice> {

    private static final String outPutFileName = "/app/logs/javaUnityDecoded.wav";
    //private static final String encodedFileName = "/Users/josef/Development/C_Language/javaUnityEncoded.wav";
    private OutputStream outputStream;
    //rivate OutputStream encodedStream;

    private static final int MAX_PACKET_SIZE = 4 * 1276;
    private static final int MAX_FRAME_SIZE = 6 * 480;
    private static final int CHANNELS = 1;

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Logger logger = LogManager.getLogger(VoiceHandler.class);
    private long opusDecoder;

    public VoiceHandler(final long opusDecoder) throws IOException {
        this.opusDecoder = opusDecoder;

        Path fileOutput = Paths.get(outPutFileName);
        //Path fileEncoded = Paths.get(encodedFileName);
        outputStream = Files.newOutputStream(fileOutput);
        //encodedStream = Files.newOutputStream(fileEncoded);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.add(incoming);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.remove(incoming);
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Spec.PlayerVoice playerVoice) throws Exception {

        logger.info("UUID: " + playerVoice.getUuid());
        logger.info("Audio: " + Arrays.toString(playerVoice.getVoice().toByteArray()));

        byte[] output = new byte[MAX_FRAME_SIZE * CHANNELS * 2];

        int currentFrameSize = Opus
                .decode(opusDecoder, playerVoice.getVoice().toByteArray(), 0, playerVoice.getVoice().toByteArray().length, output, 0, MAX_FRAME_SIZE, 0);

        outputStream.write(output, 0, currentFrameSize * CHANNELS * 2);

        for (Channel channel : channels) {
            if (ctx.channel() != channel) {
                channel.writeAndFlush(playerVoice);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
