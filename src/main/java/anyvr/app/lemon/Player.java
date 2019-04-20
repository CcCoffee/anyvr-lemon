package anyvr.app.lemon;

import java.io.OutputStream;
import java.util.UUID;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Player {
    private final Channel channel;
    private final UUID uuid;
    private final long audioDecoder;
    private final OutputStream audioFile;
    private final Object lock = new Object();
}
