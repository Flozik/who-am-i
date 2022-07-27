package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.action.PlayerAction;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.TurnImpl;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.enums.PlayerState;

import java.util.List;
import java.util.Map;

public final class ProcessingQuestion extends AbstractGameState {

	private Turn turn;

	public ProcessingQuestion(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size(), players);
		this.turn = new TurnImpl(players.values().stream().toList());
		updatePlayersState(this.getCurrentTurn(), players);
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
	public List<PlayerAction> getCurrentTurn() {
		return this.turn.getCurrentTurn();
	}

	public List<List<PlayerAction>> getTurns() {
		return this.turn.getTurns();
	}

	public void ask(String player, String question) {
		if (!turn.isAsking(player)) throw new GameException("Please, wait for your turn");
		turn.action(player, question);
		updateAsking(players);
	}

	public void answer(String player, String value) {
		if (!turn.isQuestionPresent()) throw new GameException("No question for answer");
		if (!turn.isAnswerer(player)) throw new GameException("You can't answer");
		turn.action(player, value);
		updateAnswer(player, players);
		if (turn.hasTurnEnded()) {
			turn.makeTurn(players.values().stream().toList(), turn.calculateAnswers());
			updatePlayersState(this.getCurrentTurn(), players);
		}
	}

	private void updatePlayersState(List<PlayerAction> playerActions, Map<String, SynchronousPlayer> players) {
		String askingPlayer = playerActions
				.stream()
				.filter(action -> action.getAction().equals(PlayerAction.Action.QUESTION))
				.map(PlayerAction::getPlayer)
				.findFirst()
				.orElseThrow(() -> new GameException("Cannot find asking player"));

		for (var player : players.entrySet()) {
			if (player.getValue().getName().equals(askingPlayer)) {
				player.getValue().setPlayerState(PlayerState.ASKING);
			} else {
				player.getValue().setPlayerState(PlayerState.WAITING_FOR_QUESTION);
			}
		}
	}

	private void updateAsking(Map<String, SynchronousPlayer> players) {
		for (var playerEntry : players.entrySet()) {
			if (playerEntry.getValue().getPlayerState().equals(PlayerState.ASKING)) {
				playerEntry.getValue().setPlayerState(PlayerState.WAITING_FOR_ANSWERS);
			} else {
				playerEntry.getValue().setPlayerState(PlayerState.ANSWERING);
			}
		}
	}

	private void updateAnswer(String player, Map<String, SynchronousPlayer> players) {
		for (var playerEntry : players.entrySet()) {
			if (playerEntry.getValue().getName().equals(player)) {
				playerEntry.getValue().setPlayerState(PlayerState.WAITING_FOR_QUESTION);
			}
		}
	}

}
