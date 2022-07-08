package com.eleks.academy.whoami.core.impl;


import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistentPlayerTest {
	final private PersistentPlayer player = new PersistentPlayer("Taras", "nickName");

	@Test
	void getNameTest() {
		String fakeName = "Jesus";
		String originalName = player.getName();

		assertThat(originalName).isNotEqualTo(fakeName);

		String trueName = "Taras";
		assertThat(originalName).isEqualTo(trueName);
	}
	@Test
	void checkIfCharacterAdding(){
		var getNull = player.getCharacter();
		assertThat(getNull).isEqualTo(null);

		CharacterSuggestion suggestion = new CharacterSuggestion();
		suggestion.setCharacter("character");

		var setChar = player.suggestCharacterAndNickName(suggestion);
		var getChar = player.getCharacter();
		assertEquals(getChar, setChar);
	}

}
