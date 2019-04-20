package anyvr.app.lemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import anyvr.app.lemon.handler.VoiceHandler;
import io.netty.channel.Channel;
import lombok.Data;

@Data
public class PlayerStore {
    private final List<Player> players;

    public PlayerStore() {
        this.players = Collections.synchronizedList(new ArrayList<>());
    }

    public void add(Player player) {
        players.add(player);
    }

    public void remove(Channel channel) {
        //check if players find only one uuid
        //            final Optional<Player> player = players.stream().filter((Player currentPlayer) -> {
        //                if (currentPlayer.uuid.equals(uuid)) {
        //                    return true;
        //                } else {
        //                    return false;
        //                }
        //            }).findFirst();
        //
        players.removeIf((Player player) -> player.getChannel().equals(channel));
    }

    public boolean isPlayerAlreadyExist(UUID uuid) {

        return players.stream().anyMatch((Player player) -> player.getUuid().equals(uuid));
    }

    public Optional<Player> findAnotherPlayer(UUID uuid) {

        if (uuid == null) {
            return Optional.empty();
        }

        return players.stream()
                .filter((Player currentPlayer) -> !currentPlayer.getUuid().equals(uuid))
                .findFirst();
    }

    public Optional<Player> getPlayer(UUID uuid) {

        return players
                .stream()
                .filter((Player player) -> player.getUuid().equals(uuid))
                .findFirst();
    }
}
