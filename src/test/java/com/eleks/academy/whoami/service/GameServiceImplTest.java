package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.model.response.PlayerState;
import com.eleks.academy.whoami.model.response.PlayerWithState;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

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
		final GameStatus expectedGameStatus = WAITING_FOR_PLAYERS;

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());

		when(gameRepository.save(any(SynchronousGame.class))).thenReturn(game);

		var gameDetails = gameService.createGame(player, gameRequest);

		var expectedGame = GameDetails.builder()
				.status(expectedGameStatus)
				.players(List.of(new PlayerWithState(new PersistentPlayer(player), PlayerState.READY)))
				.build();

		assertThat(gameDetails)
				.usingRecursiveComparison()
				.ignoringFields(idNaming)
				.isEqualTo(expectedGame);

		assertEquals(game.getId(), gameDetails.getId());
		assertEquals(game.getStatus(), gameDetails.getStatus());

		verify(gameRepository, times(1)).save(any(SynchronousGame.class));
	}

	@Test
	void findByIdAndPlayerTest() {
		final String player = "player";
		final GameStatus expectedGameStatus = WAITING_FOR_PLAYERS;

		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		final String id = game.getId();

		Optional<SynchronousGame> createdGame = Optional.of(game);

		when(gameRepository.findById(id)).thenReturn(createdGame);

		var foundGame = gameService.findByIdAndPlayer(id, player);
		var expectedGame = GameDetails.builder()
				.id(foundGame.get().getId())
				.status(expectedGameStatus)
				.players(List.of(new PlayerWithState(new PersistentPlayer(player), PlayerState.READY)))
				.build();
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

		HttpClientErrorException responseStatusException = assertThrows(HttpClientErrorException.class, () ->
				gameService.suggestCharacter("id", "Player4", suggestion));

		assertEquals("404 Game not found", responseStatusException.getMessage());
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

		HttpClientErrorException responseStatusException = assertThrows(HttpClientErrorException.class, () ->
				gameService.suggestCharacter(id, "Player5", suggestion));

		assertEquals("404 Player not found", responseStatusException.getMessage());
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
		String expectedGame = startGame.get().toString();

		assertEquals(startGame.get().toString(), expectedGame);
	}

}
