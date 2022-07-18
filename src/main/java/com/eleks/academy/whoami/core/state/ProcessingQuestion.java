package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.enums.PlayerState;

import java.util.Map;

// TODO: Implement makeTurn(...) and next() methods, pass a turn to next player
public final class ProcessingQuestion extends AbstractGameState {

	private final String currentPlayer;

	public ProcessingQuestion(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size(), players);

		this.currentPlayer = players.keySet()
				.stream()
				.findAny()
				.orElse(null);

		updatePlayerStates();
	}

	@Override
	public GameState next() {
		throw new GameException("Not implemented");
	}

	@Override
	public GameStatus getStatus() {
		return GameStatus.IN_PROGRESS;
	}

	@Override
	public String getCurrentTurn() {
		return this.currentPlayer;
	}

	public void updatePlayerStates() {
		for (var eachPlayer : players.keySet()) {
			if (currentPlayer.equals(players.get(eachPlayer).getName())) {
				players.get(eachPlayer).setPlayerState(PlayerState.ASKING);
			} else {
				players.get(eachPlayer).setPlayerState(PlayerState.WAITING_FOR_QUESTION);
			}
		}
	}

}
