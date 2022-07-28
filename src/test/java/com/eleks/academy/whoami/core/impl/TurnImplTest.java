package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.action.PlayerAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnImplTest {

	private final List<SynchronousPlayer> players = new ArrayList<>();

	@BeforeEach
	void setTurnImpl() {
		players.add(new PersistentPlayer("player1"));
		players.add(new PersistentPlayer("player2"));
		players.add(new PersistentPlayer("player3"));
		players.add(new PersistentPlayer("player4"));
	}

	@Test
	void turnImplConstructorTest() {
		assertThat(new TurnImpl(players).getCurrentTurn())
				.containsExactlyInAnyOrder(new PlayerAction("player1", PlayerAction.Action.QUESTION, null),
						new PlayerAction("player2", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player3", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player4", PlayerAction.Action.ANSWER, null));
	}

	@Test
	void makeTurnSamePlayerTest() {
		Turn turn = new TurnImpl(players);
		List<PlayerAction> beforeMakeTurn = turn.getCurrentTurn();

		turn.makeTurn(players, true);

		List<PlayerAction> afterMakeTurn = turn.getCurrentTurn();

		assertEquals(beforeMakeTurn, afterMakeTurn);
	}

	@Test
	void makeTurnAnotherPlayerTest() {
		Turn turn = new TurnImpl(players);

		List<PlayerAction> expectedMakeTurn = new ArrayList<>();
		expectedMakeTurn.add(new PlayerAction("player4", PlayerAction.Action.QUESTION, null));
		expectedMakeTurn.add(new PlayerAction("player1", PlayerAction.Action.ANSWER, null));
		expectedMakeTurn.add(new PlayerAction("player2", PlayerAction.Action.ANSWER, null));
		expectedMakeTurn.add(new PlayerAction("player3", PlayerAction.Action.ANSWER, null));

		turn.makeTurn(players, false);

		assertEquals(expectedMakeTurn, turn.getCurrentTurn());
	}

	@Test
	void makeTurnNewListOfPlayersTest() {
		Turn turn = new TurnImpl(players);

		final List<SynchronousPlayer> newPlayers = new ArrayList<>();
		newPlayers.add(new PersistentPlayer("player1"));
		newPlayers.add(new PersistentPlayer("player2"));
		newPlayers.add(new PersistentPlayer("player4"));

		List<PlayerAction> expectedMakeTurn = new ArrayList<>();
		expectedMakeTurn.add(new PlayerAction("player4", PlayerAction.Action.QUESTION, null));
		expectedMakeTurn.add(new PlayerAction("player1", PlayerAction.Action.ANSWER, null));
		expectedMakeTurn.add(new PlayerAction("player2", PlayerAction.Action.ANSWER, null));

		turn.makeTurn(newPlayers, false);

		assertEquals(expectedMakeTurn, turn.getCurrentTurn());
	}

	@Test
	void actionTest() {
		Turn turn = new TurnImpl(players);

		turn.action("player1", "Am I hero?");

		assertEquals(turn.getTurns().get(0).get(0).getValue(), "Am I hero?");
	}

	@Test
	void resetTurnOneTurnTest() {
		Turn turn = new TurnImpl(players);

		final List<SynchronousPlayer> newPlayers = new ArrayList<>();
		newPlayers.add(new PersistentPlayer("player1"));
		newPlayers.add(new PersistentPlayer("player2"));
		newPlayers.add(new PersistentPlayer("player4"));

		List<PlayerAction> expectedMakeTurn = new ArrayList<>();
		expectedMakeTurn.add(new PlayerAction("player1", PlayerAction.Action.QUESTION, null));
		expectedMakeTurn.add(new PlayerAction("player2", PlayerAction.Action.ANSWER, null));
		expectedMakeTurn.add(new PlayerAction("player4", PlayerAction.Action.ANSWER, null));

		turn.resetTurn(newPlayers);

		assertEquals(expectedMakeTurn, turn.getCurrentTurn());
	}

	@Test
	void resetTurnMoreOneTurnTest() {
		Turn turn = new TurnImpl(players);

		turn.action("player1", "Am I human?");
		turn.action("player2", "YES");
		turn.action("player3", "YES");
		turn.action("player4", "YES");

		turn.makeTurn(players, true);

		final List<SynchronousPlayer> newPlayers = new ArrayList<>();
		newPlayers.add(new PersistentPlayer("player1"));
		newPlayers.add(new PersistentPlayer("player2"));
		newPlayers.add(new PersistentPlayer("player4"));

		turn.resetTurn(newPlayers);

		assertEquals(List.of(
				List.of(new PlayerAction("player1", PlayerAction.Action.QUESTION, "Am I human?"),
						new PlayerAction("player2", PlayerAction.Action.ANSWER, "YES"),
						new PlayerAction("player3", PlayerAction.Action.ANSWER, "YES"),
						new PlayerAction("player4", PlayerAction.Action.ANSWER, "YES")),
				List.of(new PlayerAction("player4", PlayerAction.Action.QUESTION, null),
						new PlayerAction("player1", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player2", PlayerAction.Action.ANSWER, null))), turn.getTurns());
	}

}
