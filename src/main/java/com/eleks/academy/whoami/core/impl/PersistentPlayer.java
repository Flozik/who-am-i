package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersistentPlayer implements SynchronousPlayer {

	@EqualsAndHashCode.Include
	private final String name;
	private String nickName;
	private String character;

	public PersistentPlayer(String name, String nickName) {
		this.name = Objects.requireNonNull(name);
		this.nickName = Objects.requireNonNull(nickName);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getNickName() {
		return this.nickName;
	}

	private void setNickName(String nickName) {
		this.nickName = nickName;
	}

	private void setCharacter(String character) {
		this.character = character;
	}

	@Override
	public String getCharacter() {
		return character;
	}

	@Override
	public String suggestCharacterAndNickName(CharacterSuggestion suggestion) {
		if (suggestion.getCharacter() != null) {
			setNickName(suggestion.getNickName());
			setCharacter(suggestion.getCharacter());
		} else {
			throw new IllegalStateException("Character has already been suggested!");
		}
		return suggestion.getCharacter();
	}

}
