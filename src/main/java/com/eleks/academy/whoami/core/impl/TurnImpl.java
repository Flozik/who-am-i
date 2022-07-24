package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.action.PlayerAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnImpl implements Turn {

	private List<List<PlayerAction>> turns = new ArrayList<>();

	public TurnImpl(List<SynchronousPlayer> players) {
		SynchronousPlayer currentPlayer = players.get(0);
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
		List<PlayerAction> previousTurn = this.getCurrentTurn();

		if (previousTurn.size() == players.size()) {
			if (!samePlayer) {
				Collections.rotate(previousTurn, 1);
			}
		} else {
			updateAvailablePlayers(previousTurn, players);

			Collections.rotate(previousTurn, 1);
		}
		turns.add(newTurn(previousTurn));
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
	public void action(String player, String value) {
		this.getCurrentTurn().forEach(p -> {
			if (p.getPlayer().equals(player)) {
				p.setValue(value);
			}
		});
	}

	private List<PlayerAction> newTurn(List<PlayerAction> previousTurn) {
		List<PlayerAction> playerActions = new ArrayList<>();
		String firstPlayer = previousTurn.get(0).getPlayer();

		previousTurn.forEach(pA -> {
			if (firstPlayer.equals(pA.getPlayer())) {
				//TODO: value not implemented
				playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.QUESTION, null));
			} else {
				playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.ANSWER, null));
			}
		});
		return playerActions;
	}

	private void updateAvailablePlayers(List<PlayerAction> previousTurn, List<SynchronousPlayer> players) {
		List<PlayerAction> tmpPlayerAction = new ArrayList<>();
		String firstPlayer = previousTurn.get(0).getPlayer();
		players.forEach(p -> {
			if (firstPlayer.equals(p.getName())) {
				tmpPlayerAction.add(new PlayerAction(p.getName(), PlayerAction.Action.QUESTION, null));
			} else {
				tmpPlayerAction.add(new PlayerAction(p.getName(), PlayerAction.Action.ANSWER, null));
			}
		});
		previousTurn.retainAll(tmpPlayerAction);
	}

}
