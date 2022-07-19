package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.action.PlayerAction;
import com.eleks.academy.whoami.enums.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class TurnImpl implements Turn {

	private SynchronousPlayer currentPlayer;
	List<PlayerAction> playerActions;
	List<List<PlayerAction>> turns = new ArrayList<>();

	@Override
	public void init(List<SynchronousPlayer> players) {
		this.currentPlayer = players.stream().findAny().orElse(players.get(0));
		this.playerActions = new ArrayList<>(players.size());

		players.forEach(p -> {
			if (currentPlayer.equals(p)) {
				playerActions.add(new PlayerAction(p.getName(), PlayerAction.Action.ANSWER.toString()));
				p.setPlayerState(PlayerState.ASKING);
			} else {
				playerActions.add(new PlayerAction(p.getName(), PlayerAction.Action.QUESTION.toString()));
				p.setPlayerState(PlayerState.WAITING_FOR_QUESTION);
			}
		});
	}

	@Override
	public void makeTurn(List<SynchronousPlayer> players, boolean samePlayer) {

	}

	@Override
	public List<PlayerAction> getCurrentTurn() {
		return this.playerActions;
	}

	@Override
	public List<List<PlayerAction>> getTurns() {
		return null;
	}

	@Override
	public String getCurrentPlayer() {
		return this.currentPlayer.getName();
	}

	@Override
	public void saveQuestion(String player, String question) {

	}

}
