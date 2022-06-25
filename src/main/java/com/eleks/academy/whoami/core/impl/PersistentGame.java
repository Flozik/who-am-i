package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.state.GameFinished;
import com.eleks.academy.whoami.core.state.GameState;
import com.eleks.academy.whoami.core.state.SuggestingCharacters;
import com.eleks.academy.whoami.core.state.WaitingForPlayers;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.model.response.PlayerState;
import com.eleks.academy.whoami.model.response.PlayerWithState;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersistentGame implements Game, SynchronousGame {

	private final Lock turnLock = new ReentrantLock();
	private final String id;

	private final Queue<GameState> turns = new LinkedBlockingQueue<>();
	@EqualsAndHashCode.Include
	private final List<PlayerWithState> players;

	/**
	 * Creates a new game (game room) and makes a first enrolment turn by a current player
	 * so that he won't have to enroll to the game he created
	 *
	 * @param hostPlayer player to initiate a new game
	 */
	public PersistentGame(String hostPlayer, Integer maxPlayers) {
		this.id = String.format("%d-%d",
				Instant.now().toEpochMilli(),
				Double.valueOf(Math.random() * 999).intValue());
		this.players = new ArrayList<>(maxPlayers);

		PersistentPlayer player = new PersistentPlayer(hostPlayer);
		PlayerWithState playerWithState = new PlayerWithState(player, PlayerState.READY);
		this.players.add(playerWithState);

		GameState gameState = new WaitingForPlayers(maxPlayers);
		turns.add(gameState);
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return this.players.stream().map(PlayerWithState::getPlayer)
				.filter(synchronousPlayer -> synchronousPlayer.getName().equals(player)).findFirst();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public SynchronousPlayer enrollToGame(String player) {
		PersistentPlayer newPlayer = new PersistentPlayer(player);
		PlayerWithState playerWithState = new PlayerWithState(newPlayer, PlayerState.READY);
		this.players.add(playerWithState);

		var playersMap = convertListToMap(players);
		GameState gameState = new SuggestingCharacters(playersMap);

		turns.remove();
		turns.add(gameState);
		return newPlayer;
	}

	@Override
	public String getTurn() {
		return this.applyIfPresent(this.turns.peek(), GameState::getCurrentTurn);
	}

	@Override
	public void askQuestion(String player, String message) {

	}

	@Override
	public void answerQuestion(String player, Answer answer) {
		// TODO: Implement method
	}

	@Override
	public SynchronousGame start() {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return this.turns.peek() instanceof WaitingForPlayers;
	}

	@Override
	public GameStatus getStatus() {
		return this.applyIfPresent(this.turns.peek(), GameState::getStatus);
	}

	@Override
	public List<PlayerWithState> getPlayersInGame() {
		return this.players;
	}

	@Override
	public boolean isFinished() {
		return this.turns.isEmpty();
	}


	@Override
	public boolean makeTurn() {
		return true;
	}

	@Override
	public void changeTurn() {

	}

	@Override
	public void initGame() {

	}

	@Override
	public void play() {
		while (!(this.turns.peek() instanceof GameFinished)) {
			this.makeTurn();
		}
	}

	private <T, R> R applyIfPresent(T source, Function<T, R> mapper) {
		return this.applyIfPresent(source, mapper, null);
	}

	private <T, R> R applyIfPresent(T source, Function<T, R> mapper, R fallback) {
		return Optional.ofNullable(source)
				.map(mapper)
				.orElse(fallback);
	}

	private Map<String, SynchronousPlayer> convertListToMap(List<PlayerWithState> list) {
		Map<String, SynchronousPlayer> playersMap = list.stream()
				.collect(Collectors.toMap(p -> p.getPlayer().getName(), PlayerWithState::getPlayer));

		return playersMap;
	}

}
