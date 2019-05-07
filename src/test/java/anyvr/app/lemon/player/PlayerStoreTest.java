package anyvr.app.lemon.player;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import anyvr.app.lemon.handler.ChannelTest;
import anyvr.app.lemon.jni.OpusDecoder;
import io.netty.channel.Channel;

public class PlayerStoreTest {

    private PlayerStore playerStore;

    @BeforeEach
    public void setup() {
        playerStore = new PlayerStore();
    }

    @Test
    public void removePlayer() {
        final UUID playerOneId = UUID.randomUUID();
        final UUID playerTwoId = UUID.randomUUID();
        Channel playerChannelOne = new ChannelTest();
        Channel playerChannelTwo = new ChannelTest();

        Player playerOne = new Player(playerChannelOne, playerOneId, new OpusDecoder(), null, null);
        Player playerTwo = new Player(playerChannelTwo, playerTwoId, new OpusDecoder(), null, null);
        playerStore.add(playerOne);
        playerStore.add(playerTwo);

        playerStore.remove(playerChannelOne);
        assertThat(playerStore.size(), equalTo(1));
        playerStore.getPlayer(playerTwoId).ifPresent(player -> assertThat(player.getPlayerId(), is(equalTo(playerTwoId))));
    }

    @Test
    public void isPlayerAlreadyExist() {
        final UUID playerOneId = UUID.randomUUID();
        final UUID playerTwoId = UUID.randomUUID();
        Channel playerChannelOne = new ChannelTest();

        Player playerOne = new Player(playerChannelOne, playerOneId, null, null, null);
        playerStore.add(playerOne);

        assertThat(playerStore.isPlayerAlreadyExist(playerOneId), is(equalTo(true)));
        assertThat(playerStore.isPlayerAlreadyExist(playerTwoId), is(equalTo(false)));
    }

    @Test
    public void findAnotherPlayerNormalTest() {
        final UUID playerOneId = UUID.randomUUID();
        final UUID playerTwoId = UUID.randomUUID();
        Channel playerChannelOne = new ChannelTest();
        Channel playerChannelTwo = new ChannelTest();

        Player playerOne = new Player(playerChannelOne, playerOneId, null, null, null);
        Player playerTwo = new Player(playerChannelTwo, playerTwoId, null, null, null);
        playerStore.add(playerOne);
        playerStore.add(playerTwo);

        assertThat(playerStore.findOtherPlayer(playerOneId).get().getPlayerId(), is(equalTo(playerTwoId)));
        assertThat(playerStore.findOtherPlayer(playerTwoId).get().getPlayerId(), is(equalTo(playerOneId)));
    }

    @Test
    public void findAnotherPlayerIsNotExist() {
        final UUID playerOneId = UUID.randomUUID();
        Channel playerChannelOne = new ChannelTest();

        Player playerOne = new Player(playerChannelOne, playerOneId, null, null, null);
        playerStore.add(playerOne);

        assertThat(playerStore.findOtherPlayer(playerOneId).isPresent(), is(equalTo(false)));
    }

    @Test
    public void findAnotherPlayerUuidIsNullTest() {

        assertThat(playerStore.findOtherPlayer(null).isPresent(), is(equalTo(false)));
    }

}
