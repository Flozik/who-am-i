package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.action.PlayerAction;

import java.util.List;

public interface Turn {

	void init(List<SynchronousPlayer> players);

	void makeTurn(List<SynchronousPlayer> players, boolean samePlayer);

	List<PlayerAction> getCurrentTurn();

	List<List<PlayerAction>> getTurns();

	void saveQuestion(String player, String question);

	String getCurrentPlayer();

}
