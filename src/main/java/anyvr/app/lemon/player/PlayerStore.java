package anyvr.app.lemon.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import anyvr.app.lemon.player.Player;
import io.netty.channel.Channel;

public class PlayerStore {
    private final List<Player> players;

    public PlayerStore() {
        this.players = Collections.synchronizedList(new ArrayList<>());
    }

    public void add(Player player) {
        players.add(player);
    }

    public void remove(Channel channel) {
        players.removeIf((Player player) -> {
            if(player.getChannel().equals(channel)) {
                player.getOpusDecoder().destroyOpusDecoder();
                return true;
            } else {
                return false;
            }
        });
    }

    public boolean isPlayerAlreadyExist(UUID playerId) {
        return players.stream()
                .anyMatch((Player player) -> player.getPlayerId().equals(playerId));
    }

    public Optional<Player> findAnotherPlayer(UUID playedrId) {

        if (playedrId == null) {
            return Optional.empty();
        }

        return players.stream()
                .filter((Player currentPlayer) -> !currentPlayer.getPlayerId().equals(playedrId))
                .findFirst();
    }

    public Optional<Player> getPlayer(UUID playerId) {

        return players
                .stream()
                .filter((Player player) -> player.getPlayerId().equals(playerId))
                .findFirst();
    }

    public int size() {
        return players.size();
    }
}
