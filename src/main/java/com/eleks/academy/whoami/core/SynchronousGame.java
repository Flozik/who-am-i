package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.action.PlayerAction;
import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.response.PlayerWithState;

import java.util.List;
import java.util.Optional;

public interface SynchronousGame {

	Optional<SynchronousPlayer> findPlayer(String player);

	String getId();

	SynchronousPlayer enrollToGame(String player);

	void setCharacters(String player, CharacterSuggestion characters);

	List<PlayerWithState> getPlayersInGame();

	GameStatus getStatus();

	boolean isAvailable();

	boolean isAvailableToSuggestCharacter();

	List<PlayerAction> getTurn();

	void askQuestion(String player, String message);

	void answerQuestion(String player, Answer answer);

	SynchronousGame start();

	void leaveGame(String player);
}
