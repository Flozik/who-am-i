package com.eleks.academy.whoami.programTest;

import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.repository.impl.GameInMemoryRepository;
import com.eleks.academy.whoami.service.GameService;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameTest {
	private final NewGameRequest gameRequest = new NewGameRequest();
	private final GameInMemoryRepository repository = new GameInMemoryRepository();
	private final GameService gameService = new GameServiceImpl(repository);
	private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setMockMvc() {
		gameRequest.setMaxPlayers(4);
	}

	@Test
	void generalGameLoop() {
		final String player = "p1";
		var game = gameService.createGame(player, gameRequest);
		logger.log(Level.INFO, "Current game details: {0}", asJsonString(game));
		final String id = game.getId();

		enrollToGame(id);

		suggestCharacter(id);

		var startedGame = gameService.startGame(id, player);
		logger.log(Level.INFO, "Current game details after start: {0}", asJsonStringOptionalGameDetails(startedGame));

		// TODO: create method firstTurn with parameter id for a game after starting
	}

	private String asJsonString(GameDetails gameDetails) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(gameDetails);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	private String asJsonStringOptionalGameDetails(Optional<GameDetails> op) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(op.get());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private void enrollToGame(String gameId) {
		gameService.enrollToGame(gameId, "p2");
		gameService.enrollToGame(gameId, "p3");
		gameService.enrollToGame(gameId, "p4");

		logger.log(Level.INFO, "Current game details after all Players enrolled to the game {0}",
				asJsonString(GameDetails.of(repository.findById(gameId).orElseThrow())));
	}

	private void suggestCharacter(String gameId) {
		CharacterSuggestion character1 = new CharacterSuggestion();
		CharacterSuggestion character2 = new CharacterSuggestion();
		CharacterSuggestion character3 = new CharacterSuggestion();
		CharacterSuggestion character4 = new CharacterSuggestion();

		character1.setCharacter("C1");
		character2.setCharacter("C2");
		character3.setCharacter("C3");
		character4.setCharacter("C4");

		gameService.suggestCharacter(gameId, "p1", character1);
		gameService.suggestCharacter(gameId, "p2", character2);
		gameService.suggestCharacter(gameId, "p3", character3);
		gameService.suggestCharacter(gameId, "p4", character4);

		logger.log(Level.INFO, "Current game details after all Players suggest the character {0}",
				asJsonString(GameDetails.of(repository.findById(gameId).orElseThrow())));
	}

}
