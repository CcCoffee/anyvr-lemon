package anyvr.app.lemon;

import java.io.IOException;
import java.io.OutputStream;

public interface IWritingVoiceFile {
    void write(OutputStream outputStream, byte[] voice) throws IOException;
}
