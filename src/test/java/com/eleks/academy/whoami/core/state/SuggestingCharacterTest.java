package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SuggestingCharacterTest {

	@Test
	void assignCharactersTest() {
		for (int i = 0; i <= 100; i++) {
			Map<String, SynchronousPlayer> players = new HashMap<>(4);
			SynchronousPlayer player1 = new PersistentPlayer("Player1");
			SynchronousPlayer player2 = new PersistentPlayer("Player2");
			SynchronousPlayer player3 = new PersistentPlayer("Player3");
			SynchronousPlayer player4 = new PersistentPlayer("Player4");

			player1.setCharacter("Character1");
			player2.setCharacter("Character2");
			player3.setCharacter("Character3");
			player4.setCharacter("Character4");

			players.put("Player1", player1);
			players.put("Player2", player2);
			players.put("Player3", player3);
			players.put("Player4", player4);

			var characters = new SuggestingCharacters(players);
			characters.next();

			Map<String, String> inputPlayersAndCharacters = new HashMap<>(4);
			inputPlayersAndCharacters.put("Player1", "Character1");
			inputPlayersAndCharacters.put("Player2", "Character2");
			inputPlayersAndCharacters.put("Player3", "Character3");
			inputPlayersAndCharacters.put("Player4", "Character4");

			var result = characters.assignCharacters();

			for (Map.Entry<String, String> entry : inputPlayersAndCharacters.entrySet()) {
				String key = entry.getKey();
				String val1 = entry.getValue();
				String val2 = result.get(key);

				assertNotEquals(val1, val2);
			}
		}
	}

	@Test
	void moveCharactersTest() {
		for (int i = 0; i <= 100; i++) {
			Map<String, SynchronousPlayer> players = new HashMap<>(4);
			SynchronousPlayer player1 = new PersistentPlayer("Player1");
			SynchronousPlayer player2 = new PersistentPlayer("Player2");
			SynchronousPlayer player3 = new PersistentPlayer("Player3");
			SynchronousPlayer player4 = new PersistentPlayer("Player4");

			player1.setCharacter("Character1");
			player2.setCharacter("Character2");
			player3.setCharacter("Character3");
			player4.setCharacter("Character4");

			players.put("Player1", player1);
			players.put("Player2", player2);
			players.put("Player3", player3);
			players.put("Player4", player4);

			var characters = new SuggestingCharacters(players);

			final Map<String, String> playerCharacterMap = new HashMap<>();

			for (SynchronousPlayer nextPlayer : players.values()) {
				playerCharacterMap.put(nextPlayer.getName(), nextPlayer.getCharacter());
			}

			var randomShiftNumber = new Random()
					.nextInt((playerCharacterMap.size() - 1) + 1);

			var movedCharacters = characters.moveCharacters(playerCharacterMap, randomShiftNumber);

			for (Map.Entry<String, String> entry : playerCharacterMap.entrySet()) {
				String key = entry.getKey();
				String val1 = entry.getValue();
				String val2 = movedCharacters.get(key);

				assertNotEquals(val1, val2);
			}
		}
	}

}
