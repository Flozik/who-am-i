package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.enums.GameStatus;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class SuggestingCharacters extends AbstractGameState {

	public SuggestingCharacters(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size(), players);
	}

	@Override
	public GameState next() {
		Optional.of(this)
				.filter(SuggestingCharacters::finished)
				.orElseThrow(() -> new GameException("Cannot start game"));
		return new StartAssignCharacters(this.players);
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

}
