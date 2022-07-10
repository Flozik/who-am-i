package com.eleks.academy.whoami.core.impl;


import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersistentPlayerTest {
	final private PersistentPlayer player = new PersistentPlayer("Taras");

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
		suggestion.setNickName("nickName");
		suggestion.setCharacter("character");

		String expectedCharacter = "character";
		player.suggestCharacter(suggestion);

		assertEquals(player.getCharacter(), expectedCharacter);
	}

}
