package anyvr.app.lemon;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import anyvr.Spec;
import com.google.protobuf.ByteString;
import io.netty.channel.Channel;

public class PlayerVoiceServiceTest {

    private final static String voiceFilePath = "tests/";

    @Test
    public void sendMessageToOtherPlayerTest() throws IOException {
        Channel channel = Mockito.mock(Channel.class);
        PlayerStore playerStore = Mockito.spy(PlayerStore.class);

        PlayerVoiceService playerVoiceService = new PlayerVoiceService(playerStore, voiceFilePath);

        UUID playerOneId = UUID.randomUUID();
        UUID playerTwoId = UUID.randomUUID();
        playerVoiceService.getPlayer(playerOneId, channel);
        playerVoiceService.getPlayer(playerTwoId, channel);

        long currentTimestemp = System.currentTimeMillis();
        int datagramId = 5;
        byte[] voice = new byte[4];
        final Spec.PlayerVoice playerVoice = Spec.PlayerVoice.newBuilder()
                .setUuid(playerOneId.toString())
                .setVoice(ByteString.copyFrom(voice))
                .setTimestamp(currentTimestemp)
                .setDatagramOrderId(datagramId)
                .build();

        playerVoiceService.sendMessageToOtherPlayer(playerOneId, playerVoice);

        verify(playerStore, times(1)).findAnotherPlayer(playerOneId);
        verify(channel, times(1)).writeAndFlush(playerVoice);
        verify(channel, times(1)).writeAndFlush(any());
    }

    @Test
    public void getPlayerCreateNewTest() throws IOException {
        PlayerStore playerStore = Mockito.spy(PlayerStore.class);
        Channel channel = Mockito.mock(Channel.class);

        PlayerVoiceService playerVoiceService = new PlayerVoiceService(playerStore, voiceFilePath);

        UUID playerOneId = UUID.randomUUID();
        UUID playerTwoId = UUID.randomUUID();
        final Player playerTwo = playerVoiceService.getPlayer(playerOneId, channel);
        final Player playerOne = playerVoiceService.getPlayer(playerTwoId, channel);

        verify(playerStore, times(2)).add(any());
        verify(playerStore, times(1)).add(playerOne);
        verify(playerStore, times(1)).add(playerTwo);
    }

    @Test
    public void getPlayerAlreadExistTest() throws IOException {
        PlayerStore playerStore = Mockito.spy(PlayerStore.class);
        Channel channel = Mockito.mock(Channel.class);

        PlayerVoiceService playerVoiceService = new PlayerVoiceService(playerStore, voiceFilePath);

        UUID playerOneId = UUID.randomUUID();
        playerVoiceService.getPlayer(playerOneId, channel);
        playerVoiceService.getPlayer(playerOneId, channel);

        verify(playerStore, times(1)).add(any());
        verify(playerStore, times(1)).getPlayer(playerOneId);
    }
}
