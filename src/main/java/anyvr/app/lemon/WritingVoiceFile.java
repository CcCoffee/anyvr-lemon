package anyvr.app.lemon;

import java.io.IOException;
import java.io.OutputStream;

public class WritingVoiceFile implements IWritingVoiceFile {

    @Override
    public void write(final OutputStream outputStream, final byte[] voice) throws IOException {
        outputStream.write(voice);
    }
}
