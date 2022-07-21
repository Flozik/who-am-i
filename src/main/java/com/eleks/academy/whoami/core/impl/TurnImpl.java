package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.action.PlayerAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TurnImpl implements Turn {

	private List<List<PlayerAction>> turns = new ArrayList<>();

	public TurnImpl(List<SynchronousPlayer> players) {
		SynchronousPlayer currentPlayer = players.get(new Random().nextInt(players.size()));
		List<PlayerAction> playerActions = new ArrayList<>(players.size());

		players.forEach(p -> {
			if (currentPlayer.equals(p)) {
				playerActions.add(new PlayerAction(p.getName(), PlayerAction.Action.QUESTION, null));
			} else {
				playerActions.add(new PlayerAction(p.getName(), PlayerAction.Action.ANSWER, null));
			}
		});
		turns.add(playerActions);
	}

	@Override
	public void makeTurn(List<SynchronousPlayer> players, boolean samePlayer) {
		List<PlayerAction> playerActions = new ArrayList<>(players.size());
		List<PlayerAction> previousTurn = turns.get(turns.size() - 1);

		if (previousTurn.size() == players.size()) {
			if (samePlayer) {
				saveNewTurn(playerActions, previousTurn);
			} else {
				Collections.rotate(previousTurn, 1);

				saveNewTurn(playerActions, previousTurn);
			}
		} else {
			updateAvailablePlayers(previousTurn, players);

			Collections.rotate(previousTurn, 1);

			saveNewTurn(playerActions, previousTurn);
		}
		turns.add(playerActions);
	}

	@Override
	public List<PlayerAction> getCurrentTurn() {
		return this.turns.get(turns.size() - 1);
	}

	@Override
	public List<List<PlayerAction>> getTurns() {
		return this.turns; //TODO: history
	}

	@Override
	public void action(String player, PlayerAction action, String question) {

	}

	private void saveNewTurn(List<PlayerAction> playerActions, List<PlayerAction> previousTurn) {
		String firstPlayer = previousTurn.get(0).getPlayer();

		previousTurn.forEach(pA -> {
			if (firstPlayer.equals(pA.getPlayer())) {
				//TODO: value not implemented
				playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.QUESTION, null));
			} else {
				playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.ANSWER, null));
			}
		});
	}

	private void updateAvailablePlayers(List<PlayerAction> previousTurn, List<SynchronousPlayer> players) {
		List<PlayerAction> tmpPlayerAction = new ArrayList<>();
		var firstPlayer = previousTurn.get(0).getAction();
		players.forEach(p -> {
			if (firstPlayer.equals(PlayerAction.Action.QUESTION)) {
				tmpPlayerAction.add(new PlayerAction(p.getName(), PlayerAction.Action.QUESTION, null));
			} else {
				tmpPlayerAction.add(new PlayerAction(p.getName(), PlayerAction.Action.ANSWER, null));
			}
		});
		previousTurn.retainAll(tmpPlayerAction);
	}

}
