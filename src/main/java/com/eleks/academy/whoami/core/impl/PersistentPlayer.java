package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.response.PlayerState;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersistentPlayer implements SynchronousPlayer {

	private final String name;
	private String nickName;
	private String character;
	private PlayerState playerState;

	public PersistentPlayer(String name) {
		this.name = Objects.requireNonNull(name);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getNickName() {
		return this.nickName;
	}

	@Override
	public String getCharacter() {
		return character;
	}

	@Override
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}

	@Override
	public PlayerState getPlayerState() {
		return this.playerState;
	}

	@Override
	public void suggestCharacter(CharacterSuggestion suggestion) {
		this.nickName = suggestion.getNickName();
		this.character = suggestion.getCharacter();
		this.setPlayerState(PlayerState.READY);
	}

}
