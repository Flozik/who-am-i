package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.enums.GameStatus;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class SuggestingCharacters extends AbstractGameState {

	private final Lock lock = new ReentrantLock();
	static final int FAILED_ATTEMPTS_SHUFFLED = 5;

	private final Map<String, SynchronousPlayer> players;

	public SuggestingCharacters(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size());

		this.players = players;
	}

	/**
	 * Randomly assigns characters to players and returns a next stage
	 * or throws {@link GameException} in case {@link this#finished()} returns {@code false}
	 *
	 * @return next {@link ProcessingQuestion} stage
	 */
	@Override
	public GameState next() {
		return Optional.of(this)
				.filter(SuggestingCharacters::finished)
				.map(SuggestingCharacters::assignCharacters)
				.map(ProcessingQuestion::new)
				.orElseThrow(() -> new GameException("Cannot start game"));
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.ofNullable(this.players.get(player));
	}

	@Override
	public GameStatus getStatus() {
		return GameStatus.SUGGESTING_CHARACTERS;
	}

	private boolean finished() {
		return this.players.values().stream()
				.map(SynchronousPlayer::getCharacter)
				.filter(Objects::nonNull)
				.toList().size() == this.getMaxPlayers();
	}

	@Override
	public Map<String, SynchronousPlayer> getPlayers() {
		return this.players;
	}

	Map<String, SynchronousPlayer> assignCharacters() {
		final Map<String, String> playerToCharacterCopy = this.players.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, player -> player.getValue().getCharacter()));

		Map<String, String> playerCharacterShuffled;
		int countShuffledCharacters = 0;

		do {
			playerCharacterShuffled = shuffledCharacters(playerToCharacterCopy);
			countShuffledCharacters++;
			if (atLeastOneEqual(playerToCharacterCopy, playerCharacterShuffled)) {
				break;
			}
		} while (countShuffledCharacters != FAILED_ATTEMPTS_SHUFFLED);

		if (countShuffledCharacters == FAILED_ATTEMPTS_SHUFFLED) {
			playerCharacterShuffled = moveCharacters(playerToCharacterCopy,
					new Random().nextInt(playerToCharacterCopy.size() - 1) + 1);
		}

		fillPlayersWithShuffledCharacters(this.players, playerCharacterShuffled);

		return this.players;
	}

	private Map<String, String> shuffledCharacters(Map<String, String> playerCharacter) {
		List<String> key = new ArrayList<>(playerCharacter.keySet());
		List<String> value = new ArrayList<>(playerCharacter.values());

		Collections.shuffle(value);

		playerCharacter = IntStream.range(0, playerCharacter.size()).boxed()
				.collect(Collectors.toMap(key::get, value::get));

		return playerCharacter;
	}

	private boolean atLeastOneEqual(Map<String, String> oldPlayerCharacter, Map<String, String> playerCharacterShuffled) {
		int countEquals = 0;

		for (Map.Entry<String, String> entry : oldPlayerCharacter.entrySet()) {
			String key = entry.getKey();
			String val1 = entry.getValue();
			String val2 = playerCharacterShuffled.get(key);
			boolean isTwoValuesEqual = val1.equals(val2);

			if (isTwoValuesEqual) {
				countEquals++;
			}
		}

		return countEquals <= 0;
	}

	private Map<String, String> moveCharacters(Map<String, String> playerToCharacterCopy, int randomShiftNumber) {
		List<String> key = new ArrayList<>(playerToCharacterCopy.keySet());
		List<String> value = new ArrayList<>(playerToCharacterCopy.values());

		Collections.rotate(value, randomShiftNumber);

		playerToCharacterCopy = IntStream.range(0, playerToCharacterCopy.size()).boxed()
				.collect(Collectors.toMap(key::get, value::get));

		return playerToCharacterCopy;
	}

	private void fillPlayersWithShuffledCharacters(Map<String, SynchronousPlayer> players,
												   Map<String, String> playerCharacterShuffled) {
		for (var playerCharacters : playerCharacterShuffled.entrySet()) {
			players.get(playerCharacters.getKey()).setCharacter(playerCharacters.getValue());
		}
	}

	private <T> BiFunction<List<T>, T, T> cyclicNext() {
		return (list, item) -> {
			final var index = list.indexOf(item);

			return Optional.of(index)
					.filter(i -> i + 1 < list.size())
					.map(i -> list.get(i + 1))
					.orElseGet(() -> list.get(0));
		};
	}

}
