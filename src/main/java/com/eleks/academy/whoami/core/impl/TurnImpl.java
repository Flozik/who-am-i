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
		//TODO: use method rotate from Collections
		List<PlayerAction> playerActions = new ArrayList<>(players.size());
		List<PlayerAction> previousTurn = turns.get(turns.size() - 1);

		if (previousTurn.size() == players.size()) {
			if (samePlayer) {
				previousTurn.forEach(pA -> {
					if (pA.getAction().equals(PlayerAction.Action.QUESTION)) {
						//TODO: value not implemented
						playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.QUESTION, null));
					} else {
						playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.ANSWER, null));
					}
				});
			} else {
				Collections.rotate(previousTurn, 1);
				var firstPlayer = previousTurn.get(0).getPlayer();
				previousTurn.forEach(pA -> {
					if (firstPlayer.equals(pA.getPlayer())) {
						playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.QUESTION, null));
					} else {
						playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.ANSWER, null));
					}
				});
			}
		} else {
			/*players.forEach(p -> {
				playerActions.add(new PlayerAction(p.getName(), PlayerAction.Action.QUESTION, null));
			});*/
			Collections.rotate(previousTurn, 1);
			previousTurn.forEach(pA -> {
				playerActions.add(new PlayerAction(pA.getPlayer(), PlayerAction.Action.ANSWER, null));
			});
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

}
