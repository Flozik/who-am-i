package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.action.PlayerAction;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.enums.PlayerState;
import com.eleks.academy.whoami.enums.VotingOptions;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.model.response.PlayerWithState;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.eleks.academy.whoami.enums.GameStatus.WAITING_FOR_PLAYERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

	@Mock
	private GameRepository gameRepository;

	@InjectMocks
	private GameServiceImpl gameService;

	private final NewGameRequest gameRequest = new NewGameRequest();

	@BeforeEach
	public void setMockMvc() {
		gameRequest.setMaxPlayers(4);
	}

	@Test
	void findAvailableGamesTest() {
		final String player = "player";

		SynchronousGame synchronousGame = new PersistentGame(player, gameRequest.getMaxPlayers());
		when(gameRepository.findAllAvailable(player)).thenReturn(List.of(synchronousGame));

		assertThat(gameService.findAvailableGames(player))
				.usingRecursiveFieldByFieldElementComparatorOnFields("status")
				.containsOnly(GameLight.builder()
						.id("some id")
						.status(WAITING_FOR_PLAYERS)
						.build());
	}

	@Test
	void createGameTest() {
		final String idNaming = "id";
		final String player = "player";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());

		when(gameRepository.save(any(SynchronousGame.class))).thenReturn(game);

		var gameDetails = gameService.createGame(player, gameRequest);

		assertThat(gameDetails)
				.usingRecursiveComparison()
				.ignoringFields(idNaming)
				.isEqualTo(GameDetails.of(new PersistentGame(player, gameRequest.getMaxPlayers())));

		assertEquals(game.getId(), gameDetails.getId());
		assertEquals(game.getStatus(), gameDetails.getStatus());

		verify(gameRepository, times(1)).save(any(SynchronousGame.class));
	}

	@Test
	void findByIdAndPlayerTest() {
		final String player = "player";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		final String id = game.getId();

		Optional<SynchronousGame> createdGame = Optional.of(game);

		when(gameRepository.findById(id)).thenReturn(createdGame);

		var foundGame = gameService.findByIdAndPlayer(id, player);
		var expectedGame = GameDetails.builder()
				.id(id)
				.status(WAITING_FOR_PLAYERS)
				.players(List.of(new PlayerWithState(new PersistentPlayer(player))))
				.build();
		expectedGame.getPlayers().get(0).getPlayer().setPlayerState(PlayerState.NOT_READY);

		Optional<GameDetails> expectedGameOp = Optional.of(expectedGame);

		assertEquals(foundGame, expectedGameOp);

		verify(gameRepository, times(1)).findById(id);
	}

	@Test
	void findByIdAndPlayerFailTest() {
		final String player = "player";
		final String fakeId = "12345";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		final String id = game.getId();

		Optional<SynchronousGame> createdGame = Optional.of(game);

		when(gameRepository.findById(id)).thenReturn(createdGame);

		var foundGame = gameService.findByIdAndPlayer(id, player);
		var fakeGame = gameService.findByIdAndPlayer(fakeId, player);

		assertFalse(fakeGame.isPresent());

		verify(gameRepository, times(1)).findById(id);
	}

	@Test
	void enrollToGameTest() {
		final String player = "player";
		final String newPlayer = "newPlayer";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		Optional<SynchronousGame> createdGame = Optional.of(game);
		final String id = game.getId();

		when(gameRepository.findById(id)).thenReturn(createdGame);

		var enrolledPlayer = gameService.enrollToGame(id, newPlayer);
		var expectedPlayer = new PersistentPlayer(newPlayer);
		expectedPlayer.setPlayerState(PlayerState.NOT_READY);

		assertEquals(enrolledPlayer, expectedPlayer);
	}

	@Test
	void suggestCharacterWhenGameIsNotFoundTest() {
		final String player = "Player1";
		CharacterSuggestion suggestion = new CharacterSuggestion();
		suggestion.setCharacter("Bet Monkey");

		SynchronousGame game = new PersistentGame(player, 4);
		game.enrollToGame("Player2");
		game.enrollToGame("Player3");

		when(gameRepository.findById(eq("id"))).thenReturn(Optional.of(game));

		ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
				gameService.suggestCharacter("id", "Player4", suggestion));

		assertEquals("404 NOT_FOUND \"Game not found or not available.\"", responseStatusException.getMessage());
	}

	@Test
	void suggestCharacterWhenPLayerIsNotFoundTest() {
		final String player = "Player1";
		CharacterSuggestion suggestion = new CharacterSuggestion();
		suggestion.setCharacter("Bet Monkey");

		SynchronousGame game = new PersistentGame(player, 4);
		final String id = game.getId();
		game.enrollToGame("Player2");
		game.enrollToGame("Player3");
		game.enrollToGame("Player4");

		when(gameRepository.findById(id)).thenReturn(Optional.of(game));

		ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
				gameService.suggestCharacter(id, "Player5", suggestion));

		assertEquals("404 NOT_FOUND \"Player not found\"", responseStatusException.getMessage());
	}

	@Test
	void startGameTest() {
		final String player = "player1";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		final String id = game.getId();

		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(eq(id))).thenReturn(op);

		gameService.enrollToGame(id, "player2");
		gameService.enrollToGame(id, "player3");
		gameService.enrollToGame(id, "player4");

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

		gameService.suggestCharacter(id, player, suggestion1);
		gameService.suggestCharacter(id, "player2", suggestion2);
		gameService.suggestCharacter(id, "player3", suggestion3);
		gameService.suggestCharacter(id, "player4", suggestion4);

		var startGame = gameService.startGame(id, player);

		List<PlayerWithState> listPlayerWithState = new ArrayList<>();
		listPlayerWithState.add(new PlayerWithState(game.findPlayer(player).get()));
		listPlayerWithState.add(new PlayerWithState(game.findPlayer("player2").get()));
		listPlayerWithState.add(new PlayerWithState(game.findPlayer("player3").get()));
		listPlayerWithState.add(new PlayerWithState(game.findPlayer("player4").get()));

		var expectedGame = GameDetails.builder()
				.id(id)
				.status(GameStatus.IN_PROGRESS)
				.currentTurn(List.of(new PlayerAction("player1", PlayerAction.Action.QUESTION, null),
						new PlayerAction("player2", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player3", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player4", PlayerAction.Action.ANSWER, null)))
				.players(listPlayerWithState)
				.build();

		assertThat(startGame).isEqualTo(expectedGame);
	}

	@Test
	void answerQuestionTest() {
		final String id = "12345";
		final String player = "player1";

		SynchronousGame mockedGame = mock(SynchronousGame.class);

		when(gameRepository.findById(id)).thenReturn(Optional.of(mockedGame));
		when(mockedGame.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
		when(mockedGame.findPlayer(eq(player))).thenReturn(Optional.of(new PersistentPlayer(player)));

		gameService.answerQuestion(id, player, "YES");

		verify(mockedGame).answerQuestion(player, VotingOptions.YES);
	}

	@Test
	void answerQuestionNotFoundGameTest() {
		ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
				gameService.answerQuestion("12345", "player1", "YES"));

		assertEquals("404 NOT_FOUND \"Game not found or not available.\"", responseStatusException.getMessage());
	}

	@Test
	void answerQuestionNotFoundPlayerTest() {
		final String id = "12345";
		final String player = "player1";

		SynchronousGame mockedGame = mock(SynchronousGame.class);

		when(gameRepository.findById(id)).thenReturn(Optional.of(mockedGame));
		when(mockedGame.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

		ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
				gameService.answerQuestion(id, player, "YES"));

		assertEquals("404 NOT_FOUND \"Player not found\"", responseStatusException.getMessage());
	}

	@Test
	void historyTest() {
		final String id = "12345";
		final String player = "player1";
		var list = List.of(List
				.of(new PlayerAction(player, PlayerAction.Action.QUESTION, null),
						new PlayerAction("player2", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player3", PlayerAction.Action.ANSWER, null),
						new PlayerAction("player4", PlayerAction.Action.ANSWER, null)));

		SynchronousGame mockedGame = mock(SynchronousGame.class);

		when(gameRepository.findById(id)).thenReturn(Optional.of(mockedGame));
		when(mockedGame.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
		when(mockedGame.findPlayer(player)).thenReturn(Optional.of(new PersistentPlayer("player")));
		when(mockedGame.history()).thenReturn(list);

		assertEquals(list, gameService.history(id, player));
	}

	@Test
	void historyNofFoundGameTest() {
		ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
				gameService.history("12345", "player1"));

		assertEquals("404 NOT_FOUND \"Game not found or not available.\"", responseStatusException.getMessage());
	}

	@Test
	void historyNotFoundPlayerTest() {
		final String id = "12345";
		final String player = "player1";

		SynchronousGame mockedGame = mock(SynchronousGame.class);

		when(gameRepository.findById(id)).thenReturn(Optional.of(mockedGame));
		when(mockedGame.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

		ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
				gameService.history(id, player));

		assertEquals("404 NOT_FOUND \"Player not found\"", responseStatusException.getMessage());
	}

	@Test
	void leaveGameWaitingForPlayersTest() {
		final String player = "player1";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		final String id = game.getId();

		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(eq(id))).thenReturn(op);

		gameService.enrollToGame(id, "player2");
		gameService.enrollToGame(id, "player3");

		gameService.leaveGame(id, player);

		assertEquals(2, game.getPlayersInGame().size());
	}

	@Test
	void leaveGameSuggestCharacterTest() {
		final String player = "player1";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		final String id = game.getId();

		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(eq(id))).thenReturn(op);

		gameService.enrollToGame(id, "player2");
		gameService.enrollToGame(id, "player3");
		gameService.enrollToGame(id, "player4");

		gameService.leaveGame(id, player);

		verify(gameRepository).deleteById(eq(id));
	}

	@Test
	void leaveGameNotFoundGameTest() {
		final String player = "player1";

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		final String id = game.getId();

		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(eq(id))).thenReturn(op);

		gameService.enrollToGame(id, "player2");
		gameService.enrollToGame(id, "player3");
		gameService.enrollToGame(id, "player4");

		ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
				gameService.leaveGame("1", player));

		assertEquals("404 NOT_FOUND \"Game not found or not available.\"", responseStatusException.getMessage());
	}

}
