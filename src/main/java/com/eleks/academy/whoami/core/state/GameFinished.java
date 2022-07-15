package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;

import java.util.Map;

public final class GameFinished extends AbstractGameState {

	// TODO: implement in future
	final Map<String, SynchronousPlayer> players;

	public GameFinished(int playersInGame, int maxPlayers, Map<String, SynchronousPlayer> players) {
		super(playersInGame, maxPlayers, players);
		this.players = players;
	}

	@Override
	public GameState next() {
		return null;
	}

}
