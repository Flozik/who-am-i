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
	void answerTest() {
		ProcessingQuestion question = new ProcessingQuestion(players);
		question.getCurrentTurn().get(0).setValue("Am I hero?");

		for (var q : question.players.entrySet()) {
			q.getValue().setPlayerState(PlayerState.ANSWERING);
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

}
