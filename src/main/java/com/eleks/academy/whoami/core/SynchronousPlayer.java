package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.response.PlayerState;

public interface SynchronousPlayer {

	String getName();

	String getNickName();

	String getCharacter();

	void setPlayerState(PlayerState playerState);

	PlayerState getPlayerState();

	void suggestCharacter(CharacterSuggestion suggestion);

}
