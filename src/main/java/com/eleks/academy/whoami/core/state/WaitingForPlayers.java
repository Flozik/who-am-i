package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.enums.GameStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.eleks.academy.whoami.enums.GameStatus.WAITING_FOR_PLAYERS;

public final class WaitingForPlayers extends AbstractGameState {

	public WaitingForPlayers(int maxPlayers) {
		super(0, maxPlayers, new HashMap<>(maxPlayers));
	}

	@Override
	public GameState next() {
		return new SuggestingCharacters(this.players);
	}

	@Override
	public GameStatus getStatus() {
		return WAITING_FOR_PLAYERS;
	}

	@Override
	public int getPlayersInGame() {
		return this.players.size();
	}

	public SynchronousPlayer addPlayer(SynchronousPlayer player) {
		players.put(player.getName(), player);
		return player;
	}

}
