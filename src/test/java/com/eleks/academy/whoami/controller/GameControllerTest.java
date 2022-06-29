package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.configuration.GameControllerAdvice;
import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.eleks.academy.whoami.enums.GameStatus.WAITING_FOR_PLAYERS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

	private final GameServiceImpl gameService = mock(GameServiceImpl.class);
	private final GameController gameController = new GameController(gameService);
	private final NewGameRequest gameRequest = new NewGameRequest();
	private MockMvc mockMvc;

	@BeforeEach
	public void setMockMvc() {
		mockMvc = MockMvcBuilders.standaloneSetup(gameController)
				.setControllerAdvice(new GameControllerAdvice()).build();
		gameRequest.setMaxPlayers(5);
	}

	@Test
	void findAvailableGames() throws Exception {
		this.mockMvc.perform(
						MockMvcRequestBuilders.get("/games")
								.header("X-Player", "player"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotHaveJsonPath());
	}

	@Test
	void createGame() throws Exception {
		GameDetails gameDetails = new GameDetails();
		gameDetails.setId("12613126");
		gameDetails.setStatus(WAITING_FOR_PLAYERS);

		when(gameService.createGame(eq("player"), any(NewGameRequest.class))).thenReturn(gameDetails);

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"maxPlayers\": 4\n" +
										"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value("12613126"))
				.andExpect(jsonPath("status").value("WAITING_FOR_PLAYERS"));
	}

	@Test
	void createGameFailedWithException() throws Exception {
		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"maxPlayers\": null\n" +
										"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"message\":\"Validation failed!\"," +
						"\"details\":[\"maxPlayers must not be null\"]}"));
	}

	@Test
	void suggestCharacter() throws Exception {
		doNothing().when(gameService).suggestCharacter(eq("1234"), eq("player"), any(CharacterSuggestion.class));

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/1234/characters")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"character\": \" char\"\n" +
										"}"))
				.andExpect(status().isOk());
		verify(gameService, times(1)).suggestCharacter(eq("1234"), eq("player"), any(CharacterSuggestion.class));
	}

	@Test
	void findByIdTest() throws Exception {
		final String id = "1234";
		final String player = "player";

		GameDetails gameDetails = new GameDetails();
		gameDetails.setId(id);
		Optional<GameDetails> op = Optional.of(gameDetails);

		var expectedResponse = "{\"id\":\"1234\",\"status\":null,\"currentTurn\":null,\"players\":null}";

		when(gameService.findByIdAndPlayer(eq(id), eq(player))).thenReturn(op);

		this.mockMvc.perform(
						MockMvcRequestBuilders.get("/games/{id}", id)
								.header("X-Player", player)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(expectedResponse));

		verify(gameService, times(1)).findByIdAndPlayer(id, player);
	}

	@Test
	void findByIdNoGameTest() throws Exception {
		final String id = "1234";
		final String fakeId = "12345";
		final String player = "player";

		GameDetails gameDetails = new GameDetails();
		gameDetails.setId(id);
		Optional<GameDetails> op = Optional.of(gameDetails);

		when(gameService.findByIdAndPlayer(eq(id), eq(player))).thenReturn(op);

		this.mockMvc.perform(
						MockMvcRequestBuilders.get("/games/{id}", fakeId)
								.header("X-Player", player)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));

		verify(gameService, times(1)).findByIdAndPlayer(fakeId, player);
	}

	@Test
	void enrollToGameTest() throws Exception {
		final String hostPlayer = "hostPlayer";
		final String newPlayer = "newPlayer";

		SynchronousGame game = new PersistentGame(hostPlayer, gameRequest.getMaxPlayers());
		final String id = game.getId();

		var player1 = new PersistentPlayer(newPlayer);
		var expectedResponse = "{\"name\":\"newPlayer\",\"character\":null,\"question\":null,\"guess\":null}";

		when(gameService.enrollToGame(id, newPlayer)).thenReturn(player1);

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/{id}/players", id)
								.header("X-Player", newPlayer)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(expectedResponse));

		verify(gameService, times(1)).enrollToGame(id, newPlayer);
	}

}
