package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.action.PlayerAction;

import java.util.List;

public interface Turn {

	void makeTurn(List<SynchronousPlayer> players, boolean samePlayer);

	List<PlayerAction> getCurrentTurn();

	List<List<PlayerAction>> getTurns();

	void action(String player, String value);

}
