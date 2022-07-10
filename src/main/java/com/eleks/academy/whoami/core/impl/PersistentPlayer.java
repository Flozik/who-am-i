package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersistentPlayer implements SynchronousPlayer {

	@EqualsAndHashCode.Include
	private final String name;
	@EqualsAndHashCode.Include
	private String nickName;
	@EqualsAndHashCode.Include
	private String character;

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
	public void suggestCharacter(CharacterSuggestion suggestion) {
		this.nickName = suggestion.getNickName();
		this.character = suggestion.getCharacter();
	}

}
