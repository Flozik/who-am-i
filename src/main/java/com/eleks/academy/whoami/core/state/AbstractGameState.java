package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.enums.GameStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public abstract class AbstractGameState implements GameState {

	protected final int playersInGame;
	protected final int maxPlayers;
	protected final Map<String, SynchronousPlayer> players;

	@Override
	public GameStatus getStatus() {
		return this.getStatus();
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.ofNullable(this.players.get(player));
	}

	@Override
	public Map<String, SynchronousPlayer> getPlayers() {
		return this.players;
	}

	@Override
	public void deletePlayer(String player) {
		this.players.remove(player);
	}

	/**
	 * @return {@code null} as default implementation
	 */
	public String getCurrentTurn() {
		return null;
	}
	// TODO: leaveGame
	// TODO: updateAPI
	// TODO: test AbstractGameState throw child
	// TODO: refactor API tests
}
