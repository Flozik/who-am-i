package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class StartAssignCharacters extends AbstractGameState {

	static final int FAILED_ATTEMPTS_SHUFFLED = 5;

	public StartAssignCharacters(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size(), players);
	}

	@Override
	public GameState next() {
		return Optional.of(this)
				.map(StartAssignCharacters::assignCharacters)
				.map(ProcessingQuestion::new)
				.orElseThrow(() -> new GameException("Cannot start game"));
	}

	@Override
	public GameStatus getStatus() {
		return GameStatus.STARTS;
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
			if (areNotEqual(playerToCharacterCopy, playerCharacterShuffled)) {
				break;
			}
		} while (countShuffledCharacters != FAILED_ATTEMPTS_SHUFFLED);

		if (countShuffledCharacters == FAILED_ATTEMPTS_SHUFFLED) {
			playerCharacterShuffled = shiftCharactersBy(playerToCharacterCopy,
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

	private boolean areNotEqual(Map<String, String> oldPlayerCharacter, Map<String, String> playerCharacterShuffled) {
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

		return countEquals == 0;
	}

	private Map<String, String> shiftCharactersBy(Map<String, String> playerToCharacterCopy, int randomShiftNumber) {
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
			CharacterSuggestion suggestion = new CharacterSuggestion();
			suggestion.setCharacter(playerCharacters.getValue());
			suggestion.setNickName(this.players.get(playerCharacters.getKey()).getNickName());
			players.get(playerCharacters.getKey()).suggestCharacter(suggestion);
		}
	}


}
