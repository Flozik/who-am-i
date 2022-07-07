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

	private final Map<String, SynchronousPlayer> players;
	private final Map<String, String> playerCharacterMap;

	public SuggestingCharacters(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size());

		this.players = players;
		this.playerCharacterMap = new HashMap<>(this.players.size());
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
				.map(then -> new ProcessingQuestion(this.players))
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

	private Boolean finished() {
		this.toPlayerCharacterMap(this.players);

		final var enoughCharacters = Optional.of(this.playerCharacterMap)
				.map(Map::values)
				.stream()
				.mapToLong(Collection::size)
				.sum() >= this.players.size();

		return this.playerCharacterMap.size() > 1
				&& enoughCharacters;
	}

	@Override
	public Map<String, SynchronousPlayer> getPlayers() {
		return this.players;
	}

	Map<String, String> assignCharacters() {
		Map<String, String> playerToCharacterCopy = new HashMap<>(this.playerCharacterMap);

		Map<String, String> playerCharacterShuffled;
		int countShuffledCharacters = 0;

		do {
			playerCharacterShuffled = shuffledCharacters(playerToCharacterCopy);
			countShuffledCharacters++;
			if (countShuffledCharacters == 5) {
				playerCharacterShuffled = moveCharacters(playerToCharacterCopy);
			}
		} while (!isTwoValueEquals(playerToCharacterCopy, playerCharacterShuffled) && countShuffledCharacters != 5);

		return playerCharacterShuffled;
	}

	private Map<String, String> shuffledCharacters(Map<String, String> playerCharacter) {
		List<String> key = new ArrayList<>(playerCharacter.keySet());
		List<String> value = new ArrayList<>(playerCharacter.values());

		Collections.shuffle(value);

		playerCharacter = IntStream.range(0, playerCharacter.size()).boxed()
				.collect(Collectors.toMap(key::get, value::get));

		return playerCharacter;
	}

	private boolean isTwoValueEquals(Map<String, String> oldPlayerCharacter, Map<String, String> playerCharacterShuffled) {
		int count = 0;

		for (Map.Entry<String, String> entry : oldPlayerCharacter.entrySet()) {
			String key = entry.getKey();
			String val1 = entry.getValue();
			String val2 = playerCharacterShuffled.get(key);
			boolean isTwoValuesEqual = val1.equals(val2);

			if (isTwoValuesEqual) {
				count++;
			}
		}

		return count == 0;
	}

	private static Map<String, String> moveCharacters(Map<String, String> playerCharacter) {
		NavigableMap<String, String> tmpPlayerCharacter = new TreeMap<>(playerCharacter);

		for (var element : tmpPlayerCharacter.entrySet()) {
			String nextValue = tmpPlayerCharacter.higherEntry(element.getValue()).toString();
			playerCharacter.put(element.getKey(), nextValue);
		}

		return playerCharacter;
	}

	private void toPlayerCharacterMap(Map<String, SynchronousPlayer> players) {
		for (SynchronousPlayer nextPlayer : players.values()) {
			this.playerCharacterMap.put(nextPlayer.getName(), nextPlayer.getCharacter());
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
