package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SuggestingCharacterTest {

	@Test
	void assignCharactersTest() {
		for (int i = 0; i <= 100; i++) {
			Map<String, SynchronousPlayer> players = new HashMap<>(4);
			SynchronousPlayer player1 = new PersistentPlayer("Player1", "nickName1");
			SynchronousPlayer player2 = new PersistentPlayer("Player2", "nickName2");
			SynchronousPlayer player3 = new PersistentPlayer("Player3", "nickName3");
			SynchronousPlayer player4 = new PersistentPlayer("Player4", "nickName4");
			SynchronousPlayer player5 = new PersistentPlayer("Player1", "nickName5");
			SynchronousPlayer player6 = new PersistentPlayer("Player2", "nickName6");
			SynchronousPlayer player7 = new PersistentPlayer("Player3", "nickName7");
			SynchronousPlayer player8 = new PersistentPlayer("Player4", "nickName8");

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

			player1.suggestCharacterAndNickName(suggestion1);
			player2.suggestCharacterAndNickName(suggestion2);
			player3.suggestCharacterAndNickName(suggestion3);
			player4.suggestCharacterAndNickName(suggestion4);
			player5.suggestCharacterAndNickName(suggestion5);
			player6.suggestCharacterAndNickName(suggestion6);
			player7.suggestCharacterAndNickName(suggestion7);
			player8.suggestCharacterAndNickName(suggestion8);

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
			SynchronousPlayer player1 = new PersistentPlayer("Player1", "nickName1");
			SynchronousPlayer player2 = new PersistentPlayer("Player2", "nickName2");
			SynchronousPlayer player3 = new PersistentPlayer("Player3", "nickName3");
			SynchronousPlayer player4 = new PersistentPlayer("Player4", "nickName4");
			SynchronousPlayer player5 = new PersistentPlayer("Player1", "nickName5");
			SynchronousPlayer player6 = new PersistentPlayer("Player2", "nickName6");
			SynchronousPlayer player7 = new PersistentPlayer("Player3", "nickName7");
			SynchronousPlayer player8 = new PersistentPlayer("Player4", "nickName8");

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

			player1.suggestCharacterAndNickName(suggestion1);
			player2.suggestCharacterAndNickName(suggestion2);
			player3.suggestCharacterAndNickName(suggestion3);
			player4.suggestCharacterAndNickName(suggestion4);
			player5.suggestCharacterAndNickName(suggestion5);
			player6.suggestCharacterAndNickName(suggestion6);
			player7.suggestCharacterAndNickName(suggestion7);
			player8.suggestCharacterAndNickName(suggestion8);

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
