package anyvr.app.lemon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.app.lemon.netty.UdpServerChannel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.DefaultEventLoop;

public class VoiceStreamingServer {

    private static Logger logger = LogManager.getLogger(VoiceStreamingServer.class);

    public static void main(String[] args) {

        ServerBootstrap bootStrap = new ServerBootstrap();
        bootStrap.group(new DefaultEventLoop())
                .childHandler(new ServerHandlerInitializer());
        bootStrap.channel(UdpServerChannel.class);

        int port = Integer.parseInt(args[1]);

        logger.info("SoundNettyServer is running on (" + args[0] + "," + port + ")");
        bootStrap.bind(args[0],
                port).syncUninterruptibly();
    }
}
