package anyvr.app.lemon.player;

import java.io.OutputStream;
import java.util.UUID;

import anyvr.app.lemon.jni.OpusDecoder;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Player {
    private final Object lock = new Object();
    private final Channel channel;
    private final UUID playerId;
    private final OpusDecoder opusDecoder;
    private final OutputStream voiceFile;
    private final String voiceFileName;
    private int lastDatagramId;
    private long lastTimestamp;
}
