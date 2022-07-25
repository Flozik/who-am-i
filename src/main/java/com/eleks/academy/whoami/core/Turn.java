package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.action.PlayerAction;

import java.util.List;

public interface Turn {

	void makeTurn(List<SynchronousPlayer> players, boolean samePlayer);

	List<PlayerAction> getCurrentTurn();

	List<List<PlayerAction>> getTurns();

	boolean isQuestionPresent();

	boolean isAnswerer(String player);

	boolean hasTurnEnded();

	boolean calculateAnswers();

	void action(String player, String value);

}
