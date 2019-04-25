package anyvr.app.lemon;

import java.util.UUID;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AudioVoice {
    private final UUID id;
    private final long timestemps;
    private final byte[] voice;
}
