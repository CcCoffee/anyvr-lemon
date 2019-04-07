package anyvr.app.lemon.handler;

import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class VoiceHandler extends SimpleChannelInboundHandler<Spec.PlayerVoice> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Logger logger = LogManager.getLogger(VoiceHandler.class);
    private long opusDecoder;

    public VoiceHandler(final long opusDecoder) {
        this.opusDecoder = opusDecoder;
    }

    public VoiceHandler() {

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
