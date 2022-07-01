package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SuggestingCharacterTest {

	@Test
	void assignCharactersTest() {
		Map<String, SynchronousPlayer> players = new HashMap<>(4);
		players.put("Player1", new PersistentPlayer("Player1"));
		players.put("Player2", new PersistentPlayer("Player2"));
		players.put("Player3", new PersistentPlayer("Player3"));
		players.put("Player4", new PersistentPlayer("Player4"));
		var characters = new SuggestingCharacters(players);

		Map<String, String> inputPlayersAndCharacters = new HashMap<>(4);
		inputPlayersAndCharacters.put("Player1", "Character1");
		inputPlayersAndCharacters.put("Player2", "Character2");
		inputPlayersAndCharacters.put("Player3", "Character3");
		inputPlayersAndCharacters.put("Player4", "Character4");

		var result = characters.assignCharacters();

		assertNotEquals(inputPlayersAndCharacters, result);
	}

}
