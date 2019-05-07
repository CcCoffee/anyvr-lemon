package anyvr.app.lemon.player;

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

public class PlayerStoreServiceTest {

    private final static String voiceFilePath = "tests/";

    @Test
    public void sendMessageToOtherPlayerTest() throws IOException {
        Channel channel = Mockito.mock(Channel.class);
        PlayerStore playerStore = Mockito.spy(PlayerStore.class);

        PlayerStoreService playerStoreService = new PlayerStoreService(playerStore, voiceFilePath);

        UUID playerOneId = UUID.randomUUID();
        UUID playerTwoId = UUID.randomUUID();
        playerStoreService.getPlayer(playerOneId, channel);
        playerStoreService.getPlayer(playerTwoId, channel);

        long currentTimestemp = System.currentTimeMillis();
        int datagramId = 5;
        byte[] voice = new byte[4];
        final Spec.PlayerVoice playerVoice = Spec.PlayerVoice.newBuilder()
                .setUuid(playerOneId.toString())
                .setVoice(ByteString.copyFrom(voice))
                .setTimestamp(currentTimestemp)
                .setDatagramOrderId(datagramId)
                .build();

        playerStoreService.sendMessageToOtherPlayer(playerOneId, playerVoice);

        verify(playerStore, times(1)).findOtherPlayer(playerOneId);
        verify(channel, times(1)).writeAndFlush(playerVoice);
        verify(channel, times(1)).writeAndFlush(any());
    }

    @Test
    public void getPlayerCreateNewTest() throws IOException {
        PlayerStore playerStore = Mockito.spy(PlayerStore.class);
        Channel channel = Mockito.mock(Channel.class);

        PlayerStoreService playerStoreService = new PlayerStoreService(playerStore, voiceFilePath);

        UUID playerOneId = UUID.randomUUID();
        UUID playerTwoId = UUID.randomUUID();
        final Player playerTwo = playerStoreService.getPlayer(playerOneId, channel);
        final Player playerOne = playerStoreService.getPlayer(playerTwoId, channel);

        verify(playerStore, times(2)).add(any());
        verify(playerStore, times(1)).add(playerOne);
        verify(playerStore, times(1)).add(playerTwo);
    }

    @Test
    public void getPlayerAlreadExistTest() throws IOException {
        PlayerStore playerStore = Mockito.spy(PlayerStore.class);
        Channel channel = Mockito.mock(Channel.class);

        PlayerStoreService playerStoreService = new PlayerStoreService(playerStore, voiceFilePath);

        UUID playerOneId = UUID.randomUUID();
        playerStoreService.getPlayer(playerOneId, channel);
        playerStoreService.getPlayer(playerOneId, channel);

        verify(playerStore, times(1)).add(any());
        verify(playerStore, times(1)).getPlayer(playerOneId);
    }
}
