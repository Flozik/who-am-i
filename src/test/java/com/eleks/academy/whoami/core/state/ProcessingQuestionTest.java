package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.action.PlayerAction;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.enums.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessingQuestionTest {

	private final Map<String, SynchronousPlayer> players = new HashMap<>();

	@BeforeEach
	void setPlayers() {
		players.put("player1", new PersistentPlayer("player1"));
		players.put("player2", new PersistentPlayer("player2"));
		players.put("player3", new PersistentPlayer("player3"));
		players.put("player4", new PersistentPlayer("player4"));
	}

	@Test
	void answerSamePlayerTest() {
		ProcessingQuestion question = new ProcessingQuestion(players);
		question.getCurrentTurn().get(0).setValue("Am I hero?");

		for (var player : question.players.entrySet()) {
			player.getValue().setPlayerState(PlayerState.ANSWERING);
		}

		question.answer("player2", "YES");
		question.answer("player3", "NOT_SURE");
		question.answer("player4", "NO");

		assertThat(question.getCurrentTurn())
				.containsExactlyInAnyOrder(new PlayerAction("player1", PlayerAction.Action.QUESTION, null),
						new PlayerAction("player2", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player3", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player4", PlayerAction.Action.ANSWER, null));
	}

	@Test
	void answerAnotherPlayerTest() {
		ProcessingQuestion question = new ProcessingQuestion(players);
		question.getCurrentTurn().get(0).setValue("Am I hero?");

		for (var player : question.players.entrySet()) {
			player.getValue().setPlayerState(PlayerState.ANSWERING);
		}

		question.answer("player2", "YES");
		question.answer("player3", "NO");
		question.answer("player4", "NO");

		assertThat(question.getCurrentTurn())
				.containsExactlyInAnyOrder(new PlayerAction("player4", PlayerAction.Action.QUESTION, null),
						new PlayerAction("player1", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player2", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player3", PlayerAction.Action.ANSWER, null));
	}

	@Test
	void answerYouCantAnswerTest() {
		ProcessingQuestion question = new ProcessingQuestion(players);
		question.getCurrentTurn().get(0).setValue("Am I hero?");

		GameException responseStatusException = assertThrows(GameException.class, () ->
				question.answer("player1", "YES"));

		assertEquals("You can't answer", responseStatusException.getMessage());
	}

	@Test
	void answerNoQuestionForAnswerTest() {
		ProcessingQuestion question = new ProcessingQuestion(players);

		GameException responseStatusException = assertThrows(GameException.class, () ->
				question.answer("player1", "YES"));

		assertEquals("No question for answer", responseStatusException.getMessage());
	}

	@Test
	void answerNotAllPlayersTest() {
		ProcessingQuestion question = new ProcessingQuestion(players);
		question.getCurrentTurn().get(0).setValue("Am I hero?");

		for (var player : question.players.entrySet()) {
			player.getValue().setPlayerState(PlayerState.ANSWERING);
		}

		question.answer("player2", "YES");
		question.answer("player3", "NOT_SURE");

		assertThat(question.getCurrentTurn())
				.containsExactlyInAnyOrder(new PlayerAction("player1", PlayerAction.Action.QUESTION, "Am I hero?"),
						new PlayerAction("player2", PlayerAction.Action.ANSWER, "YES"),
						new PlayerAction("player3", PlayerAction.Action.ANSWER, "NOT_SURE"),
						new PlayerAction("player4", PlayerAction.Action.ANSWER, null));
	}

	@Test
	void askTest() {
		ProcessingQuestion question = new ProcessingQuestion(players);

		question.ask("player1", "Am I Hero?");

		var player1 = new PersistentPlayer("player1");
		player1.setPlayerState(PlayerState.WAITING_FOR_ANSWERS);
		var player2 = new PersistentPlayer("player2");
		player2.setPlayerState(PlayerState.ANSWERING);
		var player3 = new PersistentPlayer("player3");
		player3.setPlayerState(PlayerState.ANSWERING);
		var player4 = new PersistentPlayer("player4");
		player4.setPlayerState(PlayerState.ANSWERING);

		Map<String, SynchronousPlayer> playerMap = new HashMap<>();
		playerMap.put("player1", player1);
		playerMap.put("player2", player2);
		playerMap.put("player3", player3);
		playerMap.put("player4", player4);

		assertEquals(question.players, playerMap);
	}

	@Test
	void answerTest() {
		ProcessingQuestion question = new ProcessingQuestion(players);

		question.ask("player1", "Am I human?");

		question.answer("player2", "YES");
		question.answer("player3", "NO");

		var player1 = new PersistentPlayer("player1");
		player1.setPlayerState(PlayerState.WAITING_FOR_ANSWERS);
		var player2 = new PersistentPlayer("player2");
		player2.setPlayerState(PlayerState.WAITING_FOR_QUESTION);
		var player3 = new PersistentPlayer("player3");
		player3.setPlayerState(PlayerState.WAITING_FOR_QUESTION);
		var player4 = new PersistentPlayer("player4");
		player4.setPlayerState(PlayerState.ANSWERING);

		Map<String, SynchronousPlayer> playerMap = new HashMap<>();
		playerMap.put("player1", player1);
		playerMap.put("player2", player2);
		playerMap.put("player3", player3);
		playerMap.put("player4", player4);

		assertEquals(question.players, playerMap);
	}

}
