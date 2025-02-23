package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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

			CharacterSuggestion suggestion1 = new CharacterSuggestion();
			suggestion1.setCharacter("Character1");
			suggestion1.setNickName("NickName1");
			CharacterSuggestion suggestion2 = new CharacterSuggestion();
			suggestion2.setCharacter("Character2");
			suggestion2.setNickName("NickName2");
			CharacterSuggestion suggestion3 = new CharacterSuggestion();
			suggestion3.setCharacter("Character3");
			suggestion3.setNickName("NickName3");
			CharacterSuggestion suggestion4 = new CharacterSuggestion();
			suggestion4.setCharacter("Character4");
			suggestion4.setNickName("NickName4");
			CharacterSuggestion suggestion5 = new CharacterSuggestion();
			suggestion5.setCharacter("Character5");
			suggestion5.setNickName("NickName5");
			CharacterSuggestion suggestion6 = new CharacterSuggestion();
			suggestion6.setCharacter("Character6");
			suggestion6.setNickName("NickName6");
			CharacterSuggestion suggestion7 = new CharacterSuggestion();
			suggestion7.setCharacter("Character7");
			suggestion7.setNickName("NickName7");
			CharacterSuggestion suggestion8 = new CharacterSuggestion();
			suggestion8.setCharacter("Character8");
			suggestion8.setNickName("NickName8");

			player1.suggestCharacter(suggestion1);
			player2.suggestCharacter(suggestion2);
			player3.suggestCharacter(suggestion3);
			player4.suggestCharacter(suggestion4);
			player5.suggestCharacter(suggestion5);
			player6.suggestCharacter(suggestion6);
			player7.suggestCharacter(suggestion7);
			player8.suggestCharacter(suggestion8);

			players.put("Player1", player1);
			players.put("Player2", player2);
			players.put("Player3", player3);
			players.put("Player4", player4);

			var characters = new Start(players);

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

				String expectedNickName = players.get(entry.getKey()).getNickName();
				String resultNickName = result.get(entry.getKey()).getNickName();

				assertNotEquals(val1, val2);
				assertEquals(expectedNickName, resultNickName);
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

			CharacterSuggestion suggestion1 = new CharacterSuggestion();
			suggestion1.setCharacter("Character1");
			suggestion1.setNickName("NickName1");
			CharacterSuggestion suggestion2 = new CharacterSuggestion();
			suggestion2.setCharacter("Character2");
			suggestion2.setNickName("NickName2");
			CharacterSuggestion suggestion3 = new CharacterSuggestion();
			suggestion3.setCharacter("Character3");
			suggestion3.setNickName("NickName3");
			CharacterSuggestion suggestion4 = new CharacterSuggestion();
			suggestion4.setCharacter("Character4");
			suggestion4.setNickName("NickName4");
			CharacterSuggestion suggestion5 = new CharacterSuggestion();
			suggestion5.setCharacter("Character5");
			suggestion5.setNickName("NickName5");
			CharacterSuggestion suggestion6 = new CharacterSuggestion();
			suggestion6.setCharacter("Character6");
			suggestion6.setNickName("NickName6");
			CharacterSuggestion suggestion7 = new CharacterSuggestion();
			suggestion7.setCharacter("Character7");
			suggestion7.setNickName("NickName7");
			CharacterSuggestion suggestion8 = new CharacterSuggestion();
			suggestion8.setCharacter("Character8");
			suggestion8.setNickName("NickName8");

			player1.suggestCharacter(suggestion1);
			player2.suggestCharacter(suggestion2);
			player3.suggestCharacter(suggestion3);
			player4.suggestCharacter(suggestion4);
			player5.suggestCharacter(suggestion5);
			player6.suggestCharacter(suggestion6);
			player7.suggestCharacter(suggestion7);
			player8.suggestCharacter(suggestion8);

			players.put("Player1", player1);
			players.put("Player2", player2);
			players.put("Player3", player3);
			players.put("Player4", player4);

			var characters = new Start(players);

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

	@Test
	void reSuggestNickNameTest() {
		CharacterSuggestion suggestion = new CharacterSuggestion();
		suggestion.setNickName("first NickName");

		CharacterSuggestion suggestion2 = new CharacterSuggestion();
		suggestion2.setNickName("second NickName");

		final String player = "Player1";
		Map<String, SynchronousPlayer> players = new HashMap<>(4);
		SynchronousPlayer player1 = new PersistentPlayer("Player1");
		SynchronousPlayer player2 = new PersistentPlayer("Player2");
		SynchronousPlayer player3 = new PersistentPlayer("Player3");
		SynchronousPlayer player4 = new PersistentPlayer("Player4");

		players.put("Player1", player1);
		players.put("Player2", player2);
		players.put("Player3", player3);
		players.put("Player4", player4);

		SuggestingCharacters suggesting = new SuggestingCharacters(players);

		suggesting.setCharacters(player, suggestion);

		ResponseStatusException exception = assertThrows(
				ResponseStatusException.class, () ->
						suggesting.setCharacters(player, suggestion2)
		);
		assertEquals("403 FORBIDDEN \"You have already set your nickname!\"", exception.getMessage());

	}

	@Test
	void reSuggestCharacterTest() {
		CharacterSuggestion suggestion = new CharacterSuggestion();
		suggestion.setCharacter("first Character");

		CharacterSuggestion suggestion2 = new CharacterSuggestion();
		suggestion2.setCharacter("first Character");



		final String player = "Player1";
		Map<String, SynchronousPlayer> players = new HashMap<>(4);
		SynchronousPlayer player1 = new PersistentPlayer("Player1");
		SynchronousPlayer player2 = new PersistentPlayer("Player2");
		SynchronousPlayer player3 = new PersistentPlayer("Player3");
		SynchronousPlayer player4 = new PersistentPlayer("Player4");

		players.put("Player1", player1);
		players.put("Player2", player2);
		players.put("Player3", player3);
		players.put("Player4", player4);

		SuggestingCharacters suggesting = new SuggestingCharacters(players);

		suggesting.setCharacters(player, suggestion);

		ResponseStatusException exception = assertThrows(
				ResponseStatusException.class, () ->
						suggesting.setCharacters(player, suggestion2)
		);
		assertEquals("403 FORBIDDEN \"You have already suggest a character!\"", exception.getMessage());

	}
}
