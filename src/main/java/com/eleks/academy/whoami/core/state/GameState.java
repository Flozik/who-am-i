package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.action.PlayerAction;
import com.eleks.academy.whoami.enums.GameStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GameState {

	GameState next();

	/**
	 * @param player which we want to find
	 *
	 * @return synchronousPlayer
	 */
	Optional<SynchronousPlayer> findPlayer(String player);

	/**
	 * Used for presentation purposes only
	 *
	 * @return list with players
	 */
	Map<String, SynchronousPlayer> getPlayers();

	/**
	 *
	 * @param player which we want to remove from game
	 */
	void leavePlayer(String player);

	/**
	 * Used for presentation purposes only
	 *
	 * @return the status of the current state to show to players
	 */
	GameStatus getStatus();

	/**
	 * Used for presentation purposes only
	 *
	 * @return a player, whose turn is now
	 * or {@code null} if state does not take turns (e.g. {@link SuggestingCharacters})
	 */
	List<PlayerAction> getCurrentTurn();

	/**
	 * Used for presentation purposes only
	 *
	 * @return the count of the players
	 */
	int getPlayersInGame();

	/**
	 * Used for presentation purposes only
	 *
	 * @return the maximum allowed count of the players
	 */
	int getMaxPlayers();

}
