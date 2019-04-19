package anyvr.app.lemon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.app.lemon.jni.Opus;
import anyvr.app.lemon.netty.UdpServerChannel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.DefaultEventLoop;

public class VoiceStreamingServer {

    private static Logger logger = LogManager.getLogger(VoiceStreamingServer.class);
    private static final int SAMPLE_RATE = 24000;
    private static final int CHANNELS = 1;

    public static void main(String[] args) {

        final long decoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

        if (decoder == 0) {
            logger.error("Creating Decoder Error");
        }

        ServerBootstrap bootStrap = new ServerBootstrap();
        bootStrap.group(new DefaultEventLoop())
                .childHandler(new ServerHandlerInitializer(decoder));
        bootStrap.channel(UdpServerChannel.class);

        int port = Integer.parseInt(args[1]);

        logger.info("SoundNettyServer is running on (" + args[0] + "," + port + ")");
        bootStrap.bind(args[0],
                port).syncUninterruptibly();
    }
}
