package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.model.request.CharacterSuggestion;

public interface SynchronousPlayer {

	String getName();

	String getNickName();

	String getCharacter();

	void suggestCharacter(CharacterSuggestion suggestion);

}
