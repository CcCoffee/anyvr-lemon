package anyvr.app.lemon.handler;

import static com.almondtools.conmatch.datatypes.PrimitiveArrayMatcher.byteArrayContaining;
import static com.almondtools.conmatch.datatypes.PrimitiveArrayMatcher.intArrayContaining;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import anyvr.Spec;
import anyvr.app.lemon.handler.ProtobufIntLengthPrepender;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class ProtobufIntLengthPrependerTest {

    @Test
    public void protobufIntLengthPrependerTest() {

        byte[] audio = new byte[100];
        int counter = 1;
        int lastTimepstemp = (int) System.currentTimeMillis();
        final Spec.PlayerVoice playerVoice = Spec.PlayerVoice
                .newBuilder()
                .setVoice(ByteString.copyFrom(audio))
                .setDatagramOrderId(counter)
                .setUuid("5e49bc12-3b42-4418-b9d6-83214ed2cdd8")
                .setTimestamp(lastTimepstemp)
                .build();
        byte[] exptectedInBytes = Ints.toByteArray(playerVoice.toByteArray().length);
        Collections.reverse(Bytes.asList(exptectedInBytes));

        EmbeddedChannel channel = new EmbeddedChannel(
                new ProtobufIntLengthPrepender(), new ProtobufEncoder());
        channel.writeOutbound(playerVoice);
        channel.finish();

        ByteBuf read = channel.readOutbound();

        assertEquals(playerVoice.toByteArray().length + 4, read.readableBytes());

        byte[] lengthInBytes = new byte[4];
        read.readBytes(lengthInBytes);

        assertThat(lengthInBytes, byteArrayContaining(exptectedInBytes));
    }
}
