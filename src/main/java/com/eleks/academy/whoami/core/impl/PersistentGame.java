package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.state.GameFinished;
import com.eleks.academy.whoami.core.state.GameState;
import com.eleks.academy.whoami.core.state.WaitingForPlayers;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.model.response.PlayerWithState;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

import static java.lang.String.format;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersistentGame implements SynchronousGame {

	private final String id;

	private final Queue<GameState> currentState = new LinkedBlockingQueue<>();

	/**
	 * Creates a new game (game room) and makes a first enrolment turn by a current player
	 * so that he won't have to enroll to the game he created
	 *
	 * @param hostPlayer player to initiate a new game
	 */
	public PersistentGame(String hostPlayer, Integer maxPlayers) {
		this.id = format("%d-%d",
				Instant.now().toEpochMilli(),
				Double.valueOf(Math.random() * 999).intValue());

		GameState gameState = new WaitingForPlayers(maxPlayers);
		currentState.add(gameState);
		GameState state = currentState.peek();

		var newPlayer = new PersistentPlayer(hostPlayer);
		((WaitingForPlayers) state).addPlayer(newPlayer);
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return this.applyIfPresent(this.currentState.peek(), gameState -> gameState.findPlayer(player));
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public SynchronousPlayer enrollToGame(String player) {
		var checkState = currentState.peek().getStatus();

		if (checkState.equals(GameStatus.WAITING_FOR_PLAYERS)) {
			GameState state = currentState.peek();

			var newPlayer = new PersistentPlayer(player);
			var addedPlayer = ((WaitingForPlayers) state).addPlayer(newPlayer);

			if (currentState.peek().getPlayersInGame() == state.getMaxPlayers()) {
				currentState.add(currentState.peek().next());
				currentState.remove();
				return addedPlayer;
			}
			return addedPlayer;
		} else {
			throw new RuntimeException(format("Game %s has state %s", this.getId(), this.getStatus()));
		}
	}

	@Override
	public String getTurn() {
		return String.valueOf(this.currentState.peek());
	}

	@Override
	public void askQuestion(String player, String question) {
		if (this.getTurn().equals(player)){
			this.findPlayer(this.getTurn()).ifPresent(p -> p.setQuestion(question));
		}
		else throw new GameException("Please, wait for your turn.");
		// TODO: check state of player if ASKING
	}

	@Override
	public void answerQuestion(String player, Answer answer) {
		// TODO: Implement method
	}

	@Override
	public SynchronousGame start() {
		var checkState = currentState.peek().getStatus();

		if (checkState.equals(GameStatus.SUGGESTING_CHARACTERS)) {
			currentState.add(currentState.peek().next());
			currentState.remove();
			return this;
		} else {
			throw new RuntimeException(format("Game %s has state %s", this.getId(), this.getStatus()));
		}
	}

	@Override
	public boolean isAvailable() {
		return this.currentState.peek() instanceof WaitingForPlayers;
	}

	@Override
	public GameStatus getStatus() {
		return this.applyIfPresent(this.currentState.peek(), GameState::getStatus);
	}

	@Override
	public List<PlayerWithState> getPlayersInGame() {
		return this.currentState.peek().getPlayers().values().stream().map(PlayerWithState::of).toList();
	}

	@Override
	public boolean isFinished() {
		return this.currentState.isEmpty();
	}


	@Override
	public void makeTurn() {

	}

	@Override
	public void changeTurn() {

	}

	@Override
	public void initGame() {

	}

	@Override
	public void play() {
		while (!(this.currentState.peek() instanceof GameFinished)) {
			this.makeTurn();
		}
	}

	@Override
	public void leaveGame(String player) {
		this.currentState.peek().leavePlayer(player);
	}

	private <T, R> R applyIfPresent(T source, Function<T, R> mapper) {
		return this.applyIfPresent(source, mapper, null);
	}

	private <T, R> R applyIfPresent(T source, Function<T, R> mapper, R fallback) {
		return Optional.ofNullable(source)
				.map(mapper)
				.orElse(fallback);
	}

}
