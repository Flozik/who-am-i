package com.eleks.academy.whoami.programTest;

import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.repository.impl.GameInMemoryRepository;
import com.eleks.academy.whoami.service.GameService;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameTest {
	private final NewGameRequest gameRequest = new NewGameRequest();
	private final GameInMemoryRepository repository = new GameInMemoryRepository();
	private final GameService gameService = new GameServiceImpl(repository);
	private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@BeforeEach
	public void setMockMvc() {
		gameRequest.setMaxPlayers(4);
	}

	@Test
	void generalGameLoop() {
		for (int i = 0; i <= 100; i++) {
			final String player = "p1";
			var game = gameService.createGame(player, gameRequest);
			logger.log(Level.INFO, "Current game details:\n{0}", game);
			final String id = game.getId();

			enrollToGame(id);

			suggestCharacter(id);

			var startedGame = gameService.startGame(id, player);
			logger.log(Level.INFO, "Current game details after start:\n{0}", startedGame);
		}

		// TODO: create method firstTurn with parameter id for a game after starting
	}

	private void enrollToGame(String gameId) {
		gameService.enrollToGame(gameId, "p2");
		gameService.enrollToGame(gameId, "p3");
		gameService.enrollToGame(gameId, "p4");

		logger.log(Level.INFO, "Current game details after all Players enrolled to the game:\n{0}",
				GameDetails.of(repository.findById(gameId).orElseThrow()));
	}

	private void suggestCharacter(String gameId) {
		CharacterSuggestion character1 = new CharacterSuggestion();
		CharacterSuggestion character2 = new CharacterSuggestion();
		CharacterSuggestion character3 = new CharacterSuggestion();
		CharacterSuggestion character4 = new CharacterSuggestion();

		character1.setCharacter("C1");
		character2.setCharacter("C1");
		character3.setCharacter("C3");
		character4.setCharacter("C4");

		gameService.suggestCharacter(gameId, "p1", character1);
		gameService.suggestCharacter(gameId, "p2", character2);
		gameService.suggestCharacter(gameId, "p3", character3);
		gameService.suggestCharacter(gameId, "p4", character4);

		logger.log(Level.INFO, "Current game details after all Players suggest the character:\n{0}",
				GameDetails.of(repository.findById(gameId).orElseThrow()));
	}

}
