package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.action.PlayerAction;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.TurnImpl;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.enums.PlayerState;
import com.eleks.academy.whoami.enums.VotingOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ProcessingQuestion extends AbstractGameState {

	private Turn turn;
	List<String> answers = new ArrayList<>();

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

	public void ask(String player, PlayerAction.Action question) {
		throw new GameException("Not implemented");
	}

	public void answer(String player, String value) {
		if (turn.getCurrentTurn().get(0).getValue() != null) {
			if (isAnswerState()) {
				turn.action(player, value);
				answers.add(value);
				if (answers.size() == players.size() - 1) {
					turn.makeTurn(players.values().stream().toList(), collectAnswers());
					answers.clear();
					updatePlayersState(this.getCurrentTurn(), players);
				}
			} else {
				throw new GameException("You can't answer");
			}
		} else {
			throw new GameException("No question for answer");
		}
	}

	private boolean isAnswerState() {
		return players.entrySet()
				.stream()
				.anyMatch(p -> p.getValue().getPlayerState().equals(PlayerState.ANSWERING));
	}

	private boolean collectAnswers() {
		var yes = answers.stream()
				.filter(a -> a.equals(VotingOptions.YES.toString())).count();
		var notSure = answers.stream()
				.filter(a -> a.equals(VotingOptions.NOT_SURE.toString())).count();
		yes += notSure;
		var no = answers.stream().filter(a -> a.equals(VotingOptions.NO.toString())).count();
		return yes > no;
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

}
