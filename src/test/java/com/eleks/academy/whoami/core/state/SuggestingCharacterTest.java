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
		for (int i = 0; i <= 100; i++) {
			Map<String, SynchronousPlayer> players = new HashMap<>(4);
			SynchronousPlayer player1 = new PersistentPlayer("Player1");
			SynchronousPlayer player2 = new PersistentPlayer("Player2");
			SynchronousPlayer player3 = new PersistentPlayer("Player3");
			SynchronousPlayer player4 = new PersistentPlayer("Player4");
			SynchronousPlayer player5 = new PersistentPlayer("Player1");
			SynchronousPlayer player6 = new PersistentPlayer("Player2");
			SynchronousPlayer player7 = new PersistentPlayer("Player3");
			SynchronousPlayer player8 = new PersistentPlayer("Player4");

			player1.setCharacter("Character1");
			player2.setCharacter("Character2");
			player3.setCharacter("Character3");
			player4.setCharacter("Character4");
			player5.setCharacter("Character1");
			player6.setCharacter("Character2");
			player7.setCharacter("Character3");
			player8.setCharacter("Character4");

			players.put("Player1", player1);
			players.put("Player2", player2);
			players.put("Player3", player3);
			players.put("Player4", player4);

			var characters = new SuggestingCharacters(players);

			Map<String, SynchronousPlayer> inputPlayersAndCharacters = new HashMap<>(4);
			inputPlayersAndCharacters.put("Player1", player5);
			inputPlayersAndCharacters.put("Player2", player6);
			inputPlayersAndCharacters.put("Player3", player7);
			inputPlayersAndCharacters.put("Player4", player8);

			var result = characters.assignCharacters();

			for (Map.Entry<String, SynchronousPlayer> entry : inputPlayersAndCharacters.entrySet()) {
				String key = entry.getKey();
				String val1 = entry.getValue().getCharacter();
				String val2 = result.get(key).getCharacter();

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
			SynchronousPlayer player5 = new PersistentPlayer("Player1");
			SynchronousPlayer player6 = new PersistentPlayer("Player2");
			SynchronousPlayer player7 = new PersistentPlayer("Player3");
			SynchronousPlayer player8 = new PersistentPlayer("Player4");

			player1.setCharacter("Character1");
			player2.setCharacter("Character1");
			player3.setCharacter("Character1");
			player4.setCharacter("Character4");
			player5.setCharacter("Character1");
			player6.setCharacter("Character1");
			player7.setCharacter("Character1");
			player8.setCharacter("Character4");

			players.put("Player1", player1);
			players.put("Player2", player2);
			players.put("Player3", player3);
			players.put("Player4", player4);

			var characters = new SuggestingCharacters(players);

			final Map<String, SynchronousPlayer> playerCharacterMap = new HashMap<>();
			playerCharacterMap.put("Player1", player5);
			playerCharacterMap.put("Player2", player6);
			playerCharacterMap.put("Player3", player7);
			playerCharacterMap.put("Player4", player8);

			var result = characters.assignCharacters();

			String enteredPlayer = playerCharacterMap.get("Player4").getCharacter();
			String movedPlayer = result.get("Player4").getCharacter();

			assertNotEquals(enteredPlayer, movedPlayer);
		}
	}

}
