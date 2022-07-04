package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SuggestingCharacterTest {

	@Test
	void assignCharactersTest() {
		for (int i = 0; i <=10; i++) {
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
			System.out.println(result);
			System.out.println(inputPlayersAndCharacters);

			for (Map.Entry<String, String> entry : inputPlayersAndCharacters.entrySet()) {
				String key = entry.getKey();
				String val1 = entry.getValue();
				String val2 = result.get(key);

				assertNotEquals(val1, val2);
			}
		}
	}

}
