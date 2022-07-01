package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.GameCharacter;
import com.eleks.academy.whoami.enums.GameStatus;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public final class SuggestingCharacters extends AbstractGameState {

	private final Lock lock = new ReentrantLock();

	private final Map<String, SynchronousPlayer> players;
	private final Map<String, List<GameCharacter>> suggestedCharacters;
	private final Map<String, String> playerCharacterMap;

	public SuggestingCharacters(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size());

		this.players = players;
		this.suggestedCharacters = new HashMap<>(this.players.size());
		this.playerCharacterMap = new HashMap<>(this.players.size());

		for (SynchronousPlayer nextPlayer : this.players.values()) {
			this.playerCharacterMap.put(nextPlayer.getName(), nextPlayer.getCharacter());
		}
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

	// TODO: Consider extracting into {@link GameState}
	private Boolean finished() {
		final var enoughCharacters = Optional.of(this.suggestedCharacters)
				.map(Map::values)
				.stream()
				.mapToLong(Collection::size)
				.sum() >= this.players.size();

		return this.suggestedCharacters.size() > 1
				&& enoughCharacters;
	}

	private GameState suggestCharacter(String player, String character) {
		List<GameCharacter> characters = this.suggestedCharacters.get(player);

		if (Objects.isNull(characters)) {
			final var newCharacters = new ArrayList<GameCharacter>();

			this.suggestedCharacters.put(player, newCharacters);

			characters = newCharacters;
		}

		characters.add(GameCharacter.of(character, player));

		return this;
	}

	@Override
	public Map<String, SynchronousPlayer> getPlayers() {
		return this.players;
	}

	Map<String, String> assignCharacters() {
		Map<String, String> playerToCharacterCopy = new HashMap<>(this.playerCharacterMap);

		Map<String, String> playerCharacterShuffled;
		do {
			playerCharacterShuffled = getRandomCharacter(playerToCharacterCopy);
		} while (!isTwoValueEquals(playerToCharacterCopy, playerCharacterShuffled));

		return playerToCharacterCopy;
	}

	private Map<String, String> getRandomCharacter(Map<String, String> playerCharacter) {
		List<String> key = new ArrayList<>(playerCharacter.keySet());
		List<String> value = new ArrayList<>(playerCharacter.values());

		Collections.shuffle(value);

		playerCharacter = IntStream.range(0, playerCharacter.size()).boxed()
				.collect(Collectors.toMap(key::get, value::get));

		return playerCharacter;
	}

	private boolean isTwoValueEquals(Map<String, String> oldPlayerCharacter, Map<String, String> playerCharacterShuffled) {
		List<String> oldValues = new ArrayList<>(oldPlayerCharacter.values());
		List<String> shuffledValues = new ArrayList<>(playerCharacterShuffled.values());

		int count = 0;
		for (int i = 0; i < oldPlayerCharacter.size(); i++) {
			boolean isTwoValuesEqual = oldValues.get(i).equals(shuffledValues.get(i));
			if (isTwoValuesEqual) {
				count++;
			}
		}
		return count == 0;
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
