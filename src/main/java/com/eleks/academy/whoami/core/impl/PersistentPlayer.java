package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;

@EqualsAndHashCode
public class PersistentPlayer implements SynchronousPlayer {

	private final String name;
	private String nickName;
	private String character;
	private String question;
	private List<String> questions;

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

	@Override
	public void setQuestion(String question) {
		this.question = question;
		this.questions.add(question);
	}

	@Override
	public String getQuestion(String question) {
		return this.question = question;
	}


}
